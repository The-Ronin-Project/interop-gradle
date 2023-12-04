package com.projectronin.interop.gradle

import org.gradle.api.Project
import org.gradle.api.internal.tasks.testing.junitplatform.JUnitPlatformTestFramework
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class JunitPluginTest {
    private lateinit var project: Project

    @BeforeEach
    fun setup() {
        project = getProject()
        project.pluginManager.apply("com.projectronin.interop.gradle.junit")
    }

    @Test
    fun `includes interop jacoco plugin`() {
        assertNotNull(project.plugins.findPlugin("com.projectronin.interop.gradle.jacoco"))
    }

    @Test
    fun `includes JUnit Jupiter dependencies`() {
        val testSourceSet = project.sourceSets().getByName(SourceSet.TEST_SOURCE_SET_NAME)
        testSourceSet.compileClasspath.assertHasJars("junit-jupiter")
    }

    @Test
    fun `sets JUnit Platform`() {
        assertTrue(
            project.test().testFramework is JUnitPlatformTestFramework,
        )
    }

    @Test
    fun `sets test logging events`() {
        val events = project.test().testLogging.events
        assertEquals(
            setOf(
                TestLogEvent.PASSED,
                TestLogEvent.SKIPPED,
                TestLogEvent.FAILED,
                TestLogEvent.STANDARD_OUT,
                TestLogEvent.STANDARD_ERROR,
            ),
            events,
        )
    }

    @Test
    fun `sets exception format`() {
        assertEquals(TestExceptionFormat.FULL, project.test().testLogging.exceptionFormat)
    }
}
