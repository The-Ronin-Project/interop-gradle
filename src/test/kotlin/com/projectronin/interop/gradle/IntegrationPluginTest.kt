package com.projectronin.interop.gradle

import org.gradle.api.Project
import org.gradle.testing.base.TestingExtension
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class IntegrationPluginTest {
    private lateinit var project: Project

    @BeforeEach
    fun setup() {
        project = getProject()
        project.pluginManager.apply("com.projectronin.interop.gradle.integration")
    }

    @Test
    fun `includes interop junit plugin`() {
        assertNotNull(project.plugins.findPlugin("com.projectronin.interop.gradle.junit"))
    }

    @Test
    fun `includes test sets plugin`() {
        assertNotNull(project.plugins.findPlugin("jvm-test-suite"))
    }

    @Test
    fun `includes it testSets`() {
        assertNotNull(project.tasks.getByName("it"))

        val testing = project.extensions.getByName("testing") as TestingExtension
        assertNotNull(testing.suites.findByName("it"))
    }
}
