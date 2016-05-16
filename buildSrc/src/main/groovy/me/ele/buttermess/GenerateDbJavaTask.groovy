package me.ele.buttermess

import com.android.build.gradle.api.ApkVariant
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

public class GenerateDbJavaTask extends DefaultTask {

  @Input
  ApkVariant apkVariant

  Map<String, String> mapping = new HashMap<>();

  @TaskAction
  def taskAction() {
    apkVariant.mappingFile.eachLine { String line ->
      if (!line.startsWith(' ')) {
        String[] keyValue = line.split("->");
        String key = keyValue[0].trim()
        String value = keyValue[1].subSequence(0, keyValue[1].length() - 1).trim()
        this.mapping.put(key, value)
      }
    }

    Map<String, String> viewInjectorMap = new HashMap<>();
    apkVariant.mappingFile.eachLine { String line ->
      if (!line.startsWith(" ")) {
        String[] keyValue = line.split("->");
        String key = keyValue[0].trim()
        String value = keyValue[1].subSequence(0, keyValue[1].length() - 1).trim()
        if (key.endsWith('$$ViewInjector')) {
          int index = key.indexOf('$$ViewInjector')
          String orgInjectedClassName = key.substring(0, index)
          String mappedInjectedClassName = this.mapping.get(orgInjectedClassName)
          viewInjectorMap.put(mappedInjectedClassName, value)
        }
      }
    }

    def source = new JavaFileTemplate(
        ['packageName': Util.packageName(apkVariant), 'map': viewInjectorMap]).getContent()

    def outputFile = Util.outputJavaFile(project, apkVariant)
    if (!outputFile.isFile()) {
      outputFile.delete()
      outputFile.parentFile.mkdirs()
    }

    outputFile.text = source
  }
}