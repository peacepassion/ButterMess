package me.ele.buttermess

import com.android.build.gradle.api.ApkVariant
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

class AddDbToJarTask extends DefaultTask {


    @Input
    ApkVariant apkVariant


    @TaskAction
    def taskAction() {
        File[] files = new File[1]
        files[0] = new File(Util.outclassPath(apkVariant))
        String proguardDir = "${project.buildDir}/intermediates/transforms/proguard/${apkVariant.flavorName}"
        String jarPath = Util.findFileInDir("main.jar", proguardDir)
        Util.addFilesToExistingZip(new File(jarPath), files, Util.classEntryName(apkVariant))
    }


}