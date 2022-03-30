package com.projectronin.interop.gradle

import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class JacksonPluginTest {
    private lateinit var project: Project

    @BeforeEach
    fun setup() {
        project = getProject()
        project.pluginManager.apply("com.projectronin.interop.gradle.jackson")
    }

    @Test
    fun `includes interop base plugin`() {
        assertNotNull(project.plugins.findPlugin("com.projectronin.interop.gradle.base"))
    }

    @Test
    fun `includes Jackson dependencies`() {
        val mainSourceSet = project.sourceSets().getByName(SourceSet.MAIN_SOURCE_SET_NAME)
        mainSourceSet.compileClasspath.assertHasJars(
            "jackson-core",
            "jackson-annotations",
            "jackson-databind",
            "jackson-dataformat-yaml",
            "jackson-datatype-jsr310",
            "jackson-module-kotlin"
        )
    }
}
