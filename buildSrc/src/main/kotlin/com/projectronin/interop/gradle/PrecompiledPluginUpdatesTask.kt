package com.projectronin.interop.gradle

import com.releaseshub.gradle.plugin.artifacts.fetch.MavenArtifactRepository
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import java.io.File

open class PrecompiledPluginUpdatesTask : DefaultTask() {
    @get:Internal
    lateinit var projectDir: File

    @get:Internal
    lateinit var repositories: List<MavenArtifactRepository>

    @TaskAction
    fun update() {
        // Determine the scripts that we need to process.
        val scripts = File("src/main/kotlin").walk().mapNotNull {
            if (it.extension == "kts") it else null
        }

        // Extract the artifacts present in our scripts identified above.
        val extractor =
            com.releaseshub.gradle.plugin.dependencies.BasicDependenciesExtractor(scripts.map { it.toString() }
                .toList())
        val artifacts = extractor.extractArtifacts(projectDir)

        // Build our list of upgrades.
        val upgrades = com.releaseshub.gradle.plugin.artifacts.fetch.ArtifactUpgradeHelper.getArtifactsUpgrades(
            artifacts.getAllArtifacts(),
            repositories,
            false
        )
            .filter {
                // Only consider the artifacts that are PENDING_UPGRADE, meaning they have an update
                it.artifactUpgradeStatus == com.releaseshub.gradle.plugin.artifacts.ArtifactUpgradeStatus.PENDING_UPGRADE
            }
            .map {
                // Create our actual dependency versions in the form "groupId:artifactId:version". The default toString to an ArtifactUpgrade already includes "groupId:artifactId"
                DependencyUpgrade(
                    current = "$it:${it.fromVersion}",
                    new = "$it:${it.toVersion}"
                )
            }

        if (upgrades.isEmpty()) {
            return
        }

        scripts.forEach { script ->
            // Read each script
            val original = script.readText()
            var content = original
            upgrades.forEach { upgrade ->
                // Attempt to replace each upgrade we found
                val newContent = content.replace(upgrade.current, upgrade.new)
                if (newContent != content) {
                    // If the content changed, then the upgrade happened
                    content = newContent
                }
            }

            if (original != content) {
                // If the content changed, at least one upgrade happened, so we need to write it back out to the script
                script.writeText(content)
            }
        }
    }

    data class DependencyUpgrade(
        val current: String,
        val new: String
    )
}
