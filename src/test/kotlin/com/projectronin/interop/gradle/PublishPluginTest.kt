package com.projectronin.interop.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.internal.publication.DefaultMavenPublication
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.junitpioneer.jupiter.SetEnvironmentVariable
import org.junitpioneer.jupiter.SetEnvironmentVariable.SetEnvironmentVariables
import java.net.URI

class PublishPluginTest {
    private lateinit var project: Project

    @BeforeEach
    fun setup() {
        project = getProject()
        project.pluginManager.apply("com.projectronin.interop.gradle.publish")
    }

    @Test
    fun `includes maven publish plugin`() {
        assertNotNull(project.plugins.findPlugin("maven-publish"))
    }

    @Test
    fun `includes interop version plugin`() {
        assertNotNull(project.plugins.findPlugin("com.projectronin.interop.gradle.version"))
    }

    @Test
    @SetEnvironmentVariables(
        SetEnvironmentVariable(key = "NEXUS_USER", value = "test_user"),
        SetEnvironmentVariable(key = "NEXUS_TOKEN", value = "token"),
    )
    fun `sets maven repository to publishing for snapshot`() {
        val snapshotProject = getProject()
        // We apply this first so that it sets the version so that we can override it below
        snapshotProject.pluginManager.apply("com.projectronin.interop.gradle.version")
        snapshotProject.version = "1.0.0-SNAPSHOT"
        snapshotProject.pluginManager.apply("com.projectronin.interop.gradle.publish")

        val repositories = snapshotProject.getExtension<PublishingExtension>("publishing").repositories
        assertEquals(1, repositories.size)
        val nexus = repositories.getByName("nexus")
        if (nexus is MavenArtifactRepository) {
            assertEquals(URI("https://repo.devops.projectronin.io/repository/maven-snapshots/"), nexus.url)

            val credentials = nexus.credentials
            assertEquals("test_user", credentials.username)
            assertEquals("token", credentials.password)
        } else {
            fail { "Non Maven artifact repository" }
        }
    }

    @Test
    @SetEnvironmentVariables(
        SetEnvironmentVariable(key = "NEXUS_USER", value = "test_user"),
        SetEnvironmentVariable(key = "NEXUS_TOKEN", value = "token"),
    )
    fun `sets maven repository to publishing for non-snapshot`() {
        val nonSnapshotProject = getProject()
        // We apply this first so that it sets the version so that we can override it below
        nonSnapshotProject.pluginManager.apply("com.projectronin.interop.gradle.version")
        nonSnapshotProject.version = "1.0.0"
        nonSnapshotProject.pluginManager.apply("com.projectronin.interop.gradle.publish")

        val repositories = nonSnapshotProject.getExtension<PublishingExtension>("publishing").repositories
        assertEquals(1, repositories.size)
        val nexus = repositories.getByName("nexus")
        if (nexus is MavenArtifactRepository) {
            assertEquals(URI("https://repo.devops.projectronin.io/repository/maven-releases/"), nexus.url)

            val credentials = nexus.credentials
            assertEquals("test_user", credentials.username)
            assertEquals("token", credentials.password)
        } else {
            fail { "Non Maven artifact repository" }
        }
    }

    @Test
    fun `creates publication`() {
        val publications = project.getExtension<PublishingExtension>("publishing").publications
        assertEquals(1, publications.size)

        val library = publications.getByName("library")
        if (library is DefaultMavenPublication) {
            assertEquals(project.components.getByName("java"), library.component.get())
        } else {
            fail { "Non Maven publication" }
        }
    }

    @Test
    fun `creates install alias task`() {
        val install = project.tasks.getByName("install")
        val dependencies = install.dependsOn
        assertEquals(1, dependencies.size)
        assertEquals(project.getTaskProvider<DefaultTask>("publishToMavenLocal"), dependencies.first())
    }
}
