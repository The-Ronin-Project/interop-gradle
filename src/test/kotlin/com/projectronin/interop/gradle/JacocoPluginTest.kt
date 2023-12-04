package com.projectronin.interop.gradle

import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class JacocoPluginTest {
    private lateinit var project: Project

    @BeforeEach
    fun setup() {
        project = getProject()
        project.pluginManager.apply("com.projectronin.interop.gradle.jacoco")
    }

    @Test
    fun `includes interop base plugin`() {
        assertNotNull(project.plugins.findPlugin("com.projectronin.interop.gradle.base"))
    }

    @Test
    fun `includes jacoco plugin`() {
        assertNotNull(project.plugins.findPlugin("jacoco"))
    }

    @Test
    fun `sets up jacoco`() {
        val jacoco = project.getExtension<JacocoPluginExtension>("jacoco")
        assertEquals("0.8.8", jacoco.toolVersion)
        assertEquals("codecov", jacoco.reportsDirectory.asFile.get().name)
    }

    @Test
    fun `sets up jacoco reports`() {
        val reports = project.getTask<JacocoReport>("jacocoTestReport").reports
        assertTrue(reports.xml.required.get())
        assertFalse(reports.csv.required.get())
        assertTrue(reports.html.required.get())
    }

    @Test
    fun `enables test logging`() {
        val testLogging = project.test().testLogging
        assertTrue(testLogging.showStandardStreams)
        assertTrue(testLogging.showExceptions)
    }

    @Test
    fun `excludes HL7 formats`() {
        val extension = project.test().extensions.getByType<JacocoTaskExtension>()
        assertEquals(listOf("org/hl7/fhir/r5/formats/**", "org/hl7/fhir/r4/formats/**"), extension.excludes)
    }

    @Test
    fun `runs jacoco after tests`() {
        val test = project.test()
        assertTrue(
            test.finalizedBy.getDependencies(project.getTask<JacocoReport>("jacocoTestReport")).size == 1,
        )
    }
}
