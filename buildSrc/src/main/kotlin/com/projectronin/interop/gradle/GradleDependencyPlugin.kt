package com.projectronin.interop.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class GradleDependencyPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val precompiledPluginUpdates =
            project.tasks.create("precompiledPluginUpdate", PrecompiledPluginUpdatesTask::class.java)

        project.afterEvaluate {
            precompiledPluginUpdates.projectDir = project.rootProject.projectDir
            precompiledPluginUpdates.repositories =
                project.repositories.mapNotNull {
                    if (it is org.gradle.api.artifacts.repositories.MavenArtifactRepository) {
                        if (it.url.scheme == "http" || it.url.scheme == "https") {
                            val url = it.url.toString().dropLastWhile { char -> char == '/' }
                            com.releaseshub.gradle.plugin.artifacts.fetch.MavenArtifactRepository(it.name, url)
                        } else {
                            null
                        }
                    } else {
                        null
                    }
                }.distinctBy { it.url }
        }
    }
}
