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
    fun `includes spring dependency management plugin`() {
        assertNotNull(project.plugins.findPlugin("io.spring.dependency-management"))
    }

    @Test
    @SetEnvironmentVariables(
        SetEnvironmentVariable(key = "PACKAGE_USER", value = "test_user"),
        SetEnvironmentVariable(key = "PACKAGE_TOKEN", value = "token")
    )
    fun `sets maven repository to publishing`() {
        val repositories = project.getExtension<PublishingExtension>("publishing").repositories
        assertEquals(1, repositories.size)
        val github = repositories.getByName("GitHubPackages")
        if (github is MavenArtifactRepository) {
            assertEquals(URI("https://maven.pkg.github.com/projectronin/package-repo"), github.url)

            val credentials = github.credentials
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
            assertEquals(project.components.getByName("java"), library.component)
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
