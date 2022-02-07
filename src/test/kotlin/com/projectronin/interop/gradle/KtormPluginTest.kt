package com.projectronin.interop.gradle

import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class KtormPluginTest {
    private lateinit var project: Project

    @BeforeEach
    fun setup() {
        project = getProject()
        project.pluginManager.apply("com.projectronin.interop.gradle.ktorm")
    }

    @Test
    fun `includes interop junit plugin`() {
        assertNotNull(project.plugins.findPlugin("com.projectronin.interop.gradle.junit"))
    }

    @Test
    fun `includes Ktorm dependencies`() {
        val mainSourceSet = project.sourceSets().getByName(SourceSet.MAIN_SOURCE_SET_NAME)
        mainSourceSet.compileClasspath.assertHasJars("ktorm-core-3.4.1", "ktorm-support-mysql-3.4.1")
    }

    @Test
    fun `includes TestContainers dependencies`() {
        val testSourceSet = project.sourceSets().getByName(SourceSet.TEST_SOURCE_SET_NAME)
        testSourceSet.compileClasspath.assertHasJars("junit-jupiter-1.16.0", "mysql-1.16.0")
        testSourceSet.runtimeClasspath.assertHasJars("mysql-connector-java-8.0.26")
    }

    @Test
    fun `includes Database Rider dependencies`() {
        val testSourceSet = project.sourceSets().getByName(SourceSet.TEST_SOURCE_SET_NAME)
        testSourceSet.compileClasspath.assertHasJars("rider-junit5-1.29.0")
    }

    @Test
    fun `includes Liquibase dependencies`() {
        val testSourceSet = project.sourceSets().getByName(SourceSet.TEST_SOURCE_SET_NAME)
        testSourceSet.compileClasspath.assertHasJars("liquibase-core-4.4.3")
        testSourceSet.runtimeClasspath.assertHasJars("liquibase-slf4j-4.0.0", "snakeyaml-1.29")
    }
}
