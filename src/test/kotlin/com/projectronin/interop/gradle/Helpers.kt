package com.projectronin.interop.gradle

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.named
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import java.io.File

fun FileCollection.assertHasJars(vararg jarNames: String) {
    val allFiles = this.files.map { it.name }
    for (jarName in jarNames) {
        assertTrue(allFiles.contains("$jarName.jar")) { "No JAR found for $jarName" }
    }
}

fun FileCollection.assertNoJarsStartingWith(vararg jarPrefixes: String) {
    val allFiles = this.files.map { it.name }
    for (jarPrefix in jarPrefixes) {
        assertNull(allFiles.find { it.startsWith(jarPrefix) }) { """JAR found for prefix "$jarPrefix"""" }
    }
}

fun getProject() = ProjectBuilder.builder().withProjectDir(File(System.getProperty("user.dir"))).build()

inline fun <reified T> Project.getExtension(name: String): T = this.extensions.getByName(name) as T

inline fun <reified T : Task> Project.getTaskProvider(name: String): TaskProvider<T> = this.tasks.named<T>(name)
inline fun <reified T : Task> Project.getTask(name: String): T = getTaskProvider<T>(name).get()

// Convenience calls that are usually available within Gradle code, but are part of some weird accessors that change over time
fun Project.sourceSets() = getExtension<SourceSetContainer>("sourceSets")
fun Project.test() = getTask<Test>("test")
