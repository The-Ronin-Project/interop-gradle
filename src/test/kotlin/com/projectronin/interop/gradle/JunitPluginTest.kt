package com.projectronin.interop.gradle

import gradle.kotlin.dsl.accessors._3081ed7e6bb658519cc365c772992eb9.sourceSets
import gradle.kotlin.dsl.accessors._3081ed7e6bb658519cc365c772992eb9.test
import org.gradle.api.Project
import org.gradle.api.internal.tasks.testing.junitplatform.JUnitPlatformTestFramework
import org.gradle.api.tasks.SourceSet
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class JunitPluginTest {
    private lateinit var project: Project

    @BeforeEach
    fun setup() {
        project = ProjectBuilder.builder().build()
        project.pluginManager.apply("com.projectronin.interop.gradle.junit")
    }

    @Test
    fun `includes interop jacoco plugin`() {
        assertNotNull(project.plugins.findPlugin("com.projectronin.interop.gradle.jacoco"))
    }

    @Test
    fun `includes JUnit Jupiter dependencies`() {
        val testSourceSet = project.sourceSets.getByName(SourceSet.TEST_SOURCE_SET_NAME)
        testSourceSet.compileClasspath.assertHasJars("junit-jupiter-5.7.0")
    }

    @Test
    fun `sets JUnit Platform`() {
        assertTrue(project.tasks.test.get().testFramework is JUnitPlatformTestFramework)
    }
}
