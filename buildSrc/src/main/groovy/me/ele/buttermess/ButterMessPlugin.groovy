package me.ele.buttermess

import com.android.build.gradle.api.ApkVariant
import org.gradle.api.Plugin
import org.gradle.api.Project

public class ButterMessPlugin implements Plugin<Project> {

    // for self proguard
    public static final DB_FILE = 'bmdb'

    @Override
    void apply(Project project) {
        project.afterEvaluate {
            if (project.plugins.findPlugin('com.android.application')) {
                project.android.applicationVariants.all { ApkVariant variant ->
                    if (variant.buildType.minifyEnabled) {

                        String taskName = "transformClassesAndResourcesWithProguardFor${variant.name.capitalize()}"
                        def proguardTask = project.tasks.findByName(taskName)
                        if (!proguardTask) {
                            return
                        }
                        proguardTask.doFirst {
                            new File(Util.outclassPath(variant)).delete()
                            Util.outputJavaFile(project, variant).delete()
                        }

                        proguardTask.doLast {

                            //generate source file
                            def generateJavaFileTask = project.tasks.create(
                                    name: "generate${variant.name.capitalize()}ButterJava",
                                    type: GenerateDbJavaTask) {
                                apkVariant variant
                            }
                            generateJavaFileTask.execute()

                            //compile mapper class
                            def compileMapperTask = project.tasks.create(
                                    name: "butter${variant.name.capitalize()}Javac",
                                    type: CompileDbTask
                            ) {
                                apkVariant = variant
                            }
                            compileMapperTask.execute()

                            //add mapper class into main.jar
                            def addMapperToMainJarTask = project.tasks.create(
                                    name: "butter${variant.name.capitalize()}ToMainJar",
                                    type: AddDbToJarTask
                            ) {
                                apkVariant = variant
                            }
                            addMapperToMainJarTask.execute()
                        }
                    }

                }
            }

        }
    }


}