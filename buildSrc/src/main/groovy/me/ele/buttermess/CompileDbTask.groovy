package me.ele.buttermess

import com.android.build.gradle.api.ApkVariant
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.compile.JavaCompile

class CompileDbTask extends DefaultTask {


    @Input
    ApkVariant apkVariant


    @TaskAction
    def taskAction() {
        def javac = apkVariant.javaCompiler
        String taskName = "butter${apkVariant.name.capitalize()}InnerJavac"
        String proguardDir = "${project.buildDir}/intermediates/transforms/proguard/${apkVariant.flavorName}"
        String jarPath = Util.findFileInDir("main.jar", proguardDir)

        project.task(type: JavaCompile, overwrite: true, taskName) { JavaCompile jc ->
            jc.source Util.outputJavaFile(project, apkVariant)
            jc.destinationDir javac.destinationDir
            jc.classpath = project.files(
                    new File(((JavaCompile) javac).options.bootClasspath), javac.classpath, new File(jarPath))
            jc.sourceCompatibility javac.sourceCompatibility
            jc.targetCompatibility javac.targetCompatibility
        }
        project.tasks.getByName(taskName).execute()
    }


}