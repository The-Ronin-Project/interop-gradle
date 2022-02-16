package com.projectronin.interop.gradle

import org.gradle.api.Project
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.unbrokendome.gradle.plugins.testsets.dsl.TestSetContainer

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
        assertNotNull(project.plugins.findPlugin("org.unbroken-dome.test-sets"))
    }

    @Test
    fun `includes it testSets`() {
        val testSets = project.getExtension<TestSetContainer>("testSets")
        assertNotNull(testSets.getByName("it"))
    }
}
