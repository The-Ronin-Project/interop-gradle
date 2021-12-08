package com.projectronin.interop.gradle

import gradle.kotlin.dsl.accessors._3081ed7e6bb658519cc365c772992eb9.java
import gradle.kotlin.dsl.accessors._3081ed7e6bb658519cc365c772992eb9.sourceSets
import gradle.kotlin.dsl.accessors._583494fba9f2455342692d57689d5952.compileKotlin
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class BasePluginTest {
    private lateinit var project: Project

    @BeforeEach
    fun setup() {
        project = ProjectBuilder.builder().build()
        project.pluginManager.apply("com.projectronin.interop.gradle.base")
    }

    @Test
    fun `includes kotlin jvm plugin`() {
        assertNotNull(project.plugins.findPlugin("org.jetbrains.kotlin.jvm"))
    }

    @Test
    fun `includes spring dependency management plugin`() {
        assertNotNull(project.plugins.findPlugin("io.spring.dependency-management"))
    }

    @Test
    fun `includes ktlint plugin`() {
        assertNotNull(project.plugins.findPlugin("org.jlleitschuh.gradle.ktlint"))
    }

    @Test
    fun `sets java source compatability to 11`() {
        assertEquals(JavaVersion.VERSION_11, project.java.sourceCompatibility)
    }

    @Test
    fun `adds maven central repository`() {
        assertTrue(project.repositories.contains(project.repositories.mavenCentral()))
    }

    @Test
    fun `sets KotlinCompile options`() {
        val compile = project.tasks.compileKotlin.get()
        val options = compile.kotlinOptions
        assertEquals(listOf("-Xjsr305=strict"), options.freeCompilerArgs)
        assertEquals("11", options.jvmTarget)
    }

    @Test
    fun `includes Kotlin dependencies`() {
        val mainSourceSet = project.sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME)
        mainSourceSet.compileClasspath.assertHasJars("kotlin-reflect-1.5.31", "kotlin-stdlib-jdk8-1.5.31")
    }

    @Test
    fun `includes logging dependencies`() {
        val mainSourceSet = project.sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME)
        mainSourceSet.compileClasspath.assertHasJars("kotlin-logging-1.12.5", "slf4j-api-1.7.32")
        mainSourceSet.runtimeClasspath.assertHasJars("logback-classic-1.2.6")
    }
}
