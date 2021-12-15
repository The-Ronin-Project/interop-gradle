package com.projectronin.interop.gradle

import gradle.kotlin.dsl.accessors._e72a247e0305534ca9bb6c48de480e51.publishToMavenLocal
import gradle.kotlin.dsl.accessors._e72a247e0305534ca9bb6c48de480e51.publishing
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.publish.maven.internal.publication.DefaultMavenPublication
import org.gradle.testfixtures.ProjectBuilder
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
        project = ProjectBuilder.builder().build()
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
        SetEnvironmentVariable(key = "GITHUB_ACTOR", value = "test_user"),
        SetEnvironmentVariable(key = "GITHUB_PUBLISH_TOKEN", value = "token")
    )
    fun `sets maven repository to publishing`() {
        val repositories = project.publishing.repositories
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
        val publications = project.publishing.publications
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
        assertEquals(project.tasks.publishToMavenLocal, dependencies.first())
    }
}
