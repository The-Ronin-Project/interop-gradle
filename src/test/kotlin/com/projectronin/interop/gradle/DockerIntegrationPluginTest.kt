package com.projectronin.interop.gradle

import gradle.kotlin.dsl.accessors._239d66625f7e5fbf3a7cfab494ff1e03.clean
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.provider.Provider
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DockerIntegrationPluginTest {
    private lateinit var project: Project

    @BeforeEach
    fun setup() {
        project = getProject()
        project.pluginManager.apply("com.projectronin.interop.gradle.docker-integration")
    }

    @Test
    fun `includes interop integration plugin`() {
        assertNotNull(project.plugins.findPlugin("com.projectronin.interop.gradle.integration"))
    }

    @Test
    fun `includes interop spring boot plugin`() {
        assertNotNull(project.plugins.findPlugin("com.projectronin.interop.gradle.spring-boot"))
    }

    @Test
    fun `setup depends on clean and bootJar`() {
        val task = project.tasks.named("itSetup").get()
        val dependencies = task.dependsOn.filterIsInstance<Provider<Task>>().map { it.get() }

        assertTrue(dependencies.contains(project.tasks.clean.get()))
        assertTrue(dependencies.contains(project.tasks.named("bootJar").get()))
    }

    @Test
    fun `bootJar must run after clean`() {
        val bootJar = project.tasks.named("bootJar").get()
        assertTrue(bootJar.mustRunAfter.getDependencies(bootJar).contains(project.tasks.clean.get()))
    }

    // We should have a docker compose build test here, ideally. But we can't test the way it sets up Actions to verify.

    @Test
    fun `it depends on itSetup`() {
        val task = project.tasks.named("it").get()
        assertTrue(task.dependsOn.contains(project.tasks.named("itSetup").get()))
    }
}
