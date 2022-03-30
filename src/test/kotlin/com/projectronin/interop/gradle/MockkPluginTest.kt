package com.projectronin.interop.gradle

import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MockkPluginTest {
    private lateinit var project: Project

    @BeforeEach
    fun setup() {
        project = getProject()
        project.pluginManager.apply("com.projectronin.interop.gradle.mockk")
    }

    @Test
    fun `includes interop junit plugin`() {
        assertNotNull(project.plugins.findPlugin("com.projectronin.interop.gradle.junit"))
    }

    @Test
    fun `includes mockk dependencies`() {
        val testSourceSet = project.sourceSets().getByName(SourceSet.TEST_SOURCE_SET_NAME)
        testSourceSet.compileClasspath.assertHasJars("mockk")
    }
}
