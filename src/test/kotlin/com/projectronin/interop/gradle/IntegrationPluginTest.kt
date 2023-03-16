package com.projectronin.interop.gradle

import gradle.kotlin.dsl.accessors._6a32400db1764bf085cb3cf00218e178.testing
import org.gradle.api.Project
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
        assertNotNull(project.testing.suites.findByName("it"))
    }
}
