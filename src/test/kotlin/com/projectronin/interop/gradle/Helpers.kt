package com.projectronin.interop.gradle

import org.gradle.api.file.FileCollection
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue

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
