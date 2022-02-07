package com.projectronin.interop.gradle

import io.mockk.every
import io.mockk.mockk
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.SourceSet
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junitpioneer.jupiter.SetEnvironmentVariable
import pl.allegro.tech.build.axion.release.domain.VersionConfig
import pl.allegro.tech.build.axion.release.domain.properties.TagProperties
import pl.allegro.tech.build.axion.release.domain.scm.ScmPosition
import java.net.URI

class BasePluginTest {
    private lateinit var project: Project

    @BeforeEach
    fun setup() {
        project = getProject()
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
    fun `includes axion release plugin`() {
        assertNotNull(project.plugins.findPlugin("pl.allegro.tech.build.axion-release"))
    }

    @Test
    fun `sets java source compatability to 11`() {
        assertEquals(JavaVersion.VERSION_11, project.getExtension<JavaPluginExtension>("java").sourceCompatibility)
    }

    @Test
    fun `adds maven central repository`() {
        assertTrue(project.repositories.contains(project.repositories.mavenCentral()))
    }

    @Test
    @SetEnvironmentVariable.SetEnvironmentVariables(
        SetEnvironmentVariable(key = "PACKAGE_USER", value = "test_user"),
        SetEnvironmentVariable(key = "PACKAGE_TOKEN", value = "token")
    )
    fun `adds GitHub Packages repository`() {
        val github =
            project.repositories.find { it is MavenArtifactRepository && it.url == URI.create("https://maven.pkg.github.com/projectronin/package-repo") } as MavenArtifactRepository

        val credentials = github.credentials
        assertEquals("test_user", credentials.username)
        assertEquals("token", credentials.password)
    }

    @Test
    fun `adds maven local repository`() {
        assertTrue(project.repositories.contains(project.repositories.mavenLocal()))
    }

    @Test
    fun `sets KotlinCompile options`() {
        val compile = project.getTask<KotlinCompile>("compileKotlin")
        val options = compile.kotlinOptions
        assertEquals(listOf("-Xjsr305=strict"), options.freeCompilerArgs)
        assertEquals("11", options.jvmTarget)
    }

    @Test
    fun `includes Kotlin dependencies`() {
        val mainSourceSet =
            project.sourceSets().getByName(SourceSet.MAIN_SOURCE_SET_NAME)
        mainSourceSet.compileClasspath.assertHasJars("kotlin-reflect-1.5.31", "kotlin-stdlib-jdk8-1.5.31")
    }

    @Test
    fun `includes logging dependencies`() {
        val mainSourceSet =
            project.sourceSets().getByName(SourceSet.MAIN_SOURCE_SET_NAME)
        mainSourceSet.compileClasspath.assertHasJars("kotlin-logging-1.12.5", "slf4j-api-1.7.32")
        mainSourceSet.runtimeClasspath.assertHasJars("logback-classic-1.2.6")
    }

    @Test
    fun `axion sets initial version to 1_0_0`() {
        assertEquals(
            "1.0.0",
            project.getExtension<VersionConfig>("scmVersion").tag.initialVersion.call(
                mockk<TagProperties>(),
                mockk<ScmPosition>()
            )
        )
    }

    @Test
    fun `axion sets prefix to empty`() {
        assertEquals("", project.getExtension<VersionConfig>("scmVersion").tag.prefix)
    }

    @Test
    fun `axion does not change version for master`() {
        val versionFromTag = "1.0.0"
        val position = mockk<ScmPosition> {
            every { branch } returns "master"
        }
        val version = project.getExtension<VersionConfig>("scmVersion").versionCreator.call(versionFromTag, position)
        assertEquals("1.0.0", version)
    }

    @Test
    fun `axion does not change version for HEAD`() {
        val versionFromTag = "1.0.0"
        val position = mockk<ScmPosition> {
            every { branch } returns "HEAD"
        }
        val version = project.getExtension<VersionConfig>("scmVersion").versionCreator.call(versionFromTag, position)
        assertEquals("1.0.0", version)
    }

    @Test
    fun `axion uses full branch when not following JIRA pattern`() {
        val versionFromTag = "1.0.0"
        val position = mockk<ScmPosition> {
            every { branch } returns "my-branch"
        }
        val version = project.getExtension<VersionConfig>("scmVersion").versionCreator.call(versionFromTag, position)
        assertEquals("1.0.0-my-branch", version)
    }

    @Test
    fun `axion uses shortened branch when following JIRA pattern`() {
        val versionFromTag = "1.0.0"
        val position = mockk<ScmPosition> {
            every { branch } returns "JIRA-7354-my-branch"
        }
        val version = project.getExtension<VersionConfig>("scmVersion").versionCreator.call(versionFromTag, position)
        assertEquals("1.0.0-JIRA7354", version)
    }
}
