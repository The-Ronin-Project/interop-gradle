package com.projectronin.interop.gradle

import gradle.kotlin.dsl.accessors._583494fba9f2455342692d57689d5952.jacoco
import gradle.kotlin.dsl.accessors._583494fba9f2455342692d57689d5952.jacocoTestReport
import gradle.kotlin.dsl.accessors._583494fba9f2455342692d57689d5952.test
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
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
        project = ProjectBuilder.builder().build()
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
        val jacoco = project.jacoco
        assertEquals("0.8.7", jacoco.toolVersion)
        assertEquals("codecov", jacoco.reportsDirectory.asFile.get().name)
    }

    @Test
    fun `sets up jacoco reports`() {
        val reports = project.tasks.jacocoTestReport.get().reports
        assertTrue(reports.xml.required.get())
        assertFalse(reports.csv.required.get())
        assertTrue(reports.html.required.get())
    }

    @Test
    fun `enables test logging`() {
        val testLogging = project.tasks.test.get().testLogging
        assertTrue(testLogging.showStandardStreams)
        assertTrue(testLogging.showExceptions)
    }

    @Test
    fun `runs jacoco after tests`() {
        val test = project.tasks.test.get()
        assertTrue(
            test.finalizedBy.getDependencies(project.tasks.jacocoTestReport.get()).size == 1
        )
    }
}
