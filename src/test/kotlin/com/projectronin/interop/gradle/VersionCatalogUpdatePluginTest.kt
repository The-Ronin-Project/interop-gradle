package com.projectronin.interop.gradle

import org.gradle.api.Project
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class VersionCatalogUpdatePluginTest {
    private lateinit var project: Project

    @BeforeEach
    fun setup() {
        project = getProject()
        project.pluginManager.apply("com.projectronin.interop.gradle.version-catalog-update")
    }

    @Test
    fun `includes version-catalog-update plugin`() {
        assertNotNull(project.plugins.findPlugin("nl.littlerobots.version-catalog-update"))
    }

    @Test
    fun `includes versions plugin `() {
        assertNotNull(project.plugins.findPlugin("com.github.ben-manes.versions"))
    }

    @Test
    fun `dependencyUpdates exists`() {
        val task = project.tasks.named("dependencyUpdates").get()
        assertNotNull(task)
    }
}
