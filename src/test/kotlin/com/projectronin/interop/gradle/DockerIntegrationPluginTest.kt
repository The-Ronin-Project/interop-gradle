package com.projectronin.interop.gradle

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.provider.Provider
import org.junit.jupiter.api.Assertions.assertEquals
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

        assertTrue(dependencies.contains(project.tasks.named("clean").get()))
        assertTrue(dependencies.contains(project.tasks.named("bootJar").get()))
    }

    @Test
    fun `bootJar must run after clean`() {
        val bootJar = project.tasks.named("bootJar").get()
        assertTrue(bootJar.mustRunAfter.getDependencies(bootJar).contains(project.tasks.named("clean").get()))
    }

    // We should have a docker compose tests here, ideally. But we can't test the way it sets up Actions to verify.

    @Test
    fun `runDocker depends on itSetup`() {
        val task = project.tasks.named("runDocker").get()
        assertTrue(task.dependsOn.contains(project.tasks.named("itSetup").get()))
    }

    @Test
    fun `it depends on itSetup`() {
        val task = project.tasks.named("it").get()
        assertTrue(task.dependsOn.contains(project.tasks.named("runDocker").get()))
    }

    @Test
    fun `it finalized by stopDocker`() {
        val task = project.tasks.named("it").get()
        assertEquals("stopDocker", task.finalizedBy.getDependencies(project.getTask("it")).single().name)
    }
}
