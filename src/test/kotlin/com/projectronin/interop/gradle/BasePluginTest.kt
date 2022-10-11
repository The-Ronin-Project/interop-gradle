package com.projectronin.interop.gradle

import gradle.kotlin.dsl.accessors._697f70aae3fabb853c9932ae6a77b8d1.clean
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.SourceSet
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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
    fun `includes ktlint plugin`() {
        assertNotNull(project.plugins.findPlugin("org.jlleitschuh.gradle.ktlint"))
    }

    @Test
    fun `includes releases hub plugin`() {
        assertNotNull(project.plugins.findPlugin("com.dipien.releaseshub.gradle.plugin"))
    }

    @Test
    fun `sets java source compatability to 17`() {
        assertEquals(JavaVersion.VERSION_11, project.getExtension<JavaPluginExtension>("java").sourceCompatibility)
    }

    private fun getMavenRepository(project: Project, url: String): MavenArtifactRepository? =
        project.repositories.find { it is MavenArtifactRepository && it.url == URI.create(url) } as? MavenArtifactRepository

    @Test
    fun `adds maven central mirror repository`() {
        val maven = getMavenRepository(project, "https://repo.devops.projectronin.io/repository/maven-public/")

        val credentials = maven!!.credentials
        assertNull(credentials.username)
        assertNull(credentials.password)
    }

    @Test
    fun `adds Ronin release repository`() {
        val maven = getMavenRepository(project, "https://repo.devops.projectronin.io/repository/maven-releases/")

        val credentials = maven!!.credentials
        assertNull(credentials.username)
        assertNull(credentials.password)
    }

    @Test
    fun `adds Ronin snapshot repository when no release is set`() {
        System.getProperties().remove("ronin.release")
        val noReleaseProject = getProject()
        noReleaseProject.pluginManager.apply("com.projectronin.interop.gradle.base")

        val maven =
            getMavenRepository(noReleaseProject, "https://repo.devops.projectronin.io/repository/maven-snapshots/")

        val credentials = maven!!.credentials
        assertNull(credentials.username)
        assertNull(credentials.password)
    }

    @Test
    fun `adds Ronin snapshot repository when release is set to false`() {
        System.setProperty("ronin.release", "false")
        val falseReleaseProject = getProject()
        falseReleaseProject.pluginManager.apply("com.projectronin.interop.gradle.base")

        val maven =
            getMavenRepository(falseReleaseProject, "https://repo.devops.projectronin.io/repository/maven-snapshots/")

        val credentials = maven!!.credentials
        assertNull(credentials.username)
        assertNull(credentials.password)
    }

    @Test
    fun `does not add Ronin snapshot repository when release is set to true`() {
        System.setProperty("ronin.release", "true")
        val trueReleaseProject = getProject()
        trueReleaseProject.pluginManager.apply("com.projectronin.interop.gradle.base")

        val maven =
            getMavenRepository(trueReleaseProject, "https://repo.devops.projectronin.io/repository/maven-snapshots/")

        assertNull(maven)
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
        mainSourceSet.compileClasspath.assertHasJars(
            "kotlin-reflect",
            "kotlin-stdlib-jdk8"
        )
    }

    @Test
    fun `includes logging dependencies`() {
        val mainSourceSet =
            project.sourceSets().getByName(SourceSet.MAIN_SOURCE_SET_NAME)
        mainSourceSet.compileClasspath.assertHasJars("kotlin-logging-jvm", "slf4j-api")
        mainSourceSet.runtimeClasspath.assertHasJars("logback-classic")
    }

    @Test
    fun `loadKtlintReporters depends on clean`() {
        val task = project.tasks.named("loadKtlintReporters").get()
        val dependencies = task.dependsOn.filterIsInstance<Provider<Task>>().map { it.get() }

        assertTrue(dependencies.contains(project.tasks.clean.get()))
    }
}
