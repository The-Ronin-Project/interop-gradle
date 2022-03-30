package com.projectronin.interop.gradle

import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SpringPluginTest {
    private lateinit var project: Project

    @BeforeEach
    fun setup() {
        project = getProject()
        project.pluginManager.apply("com.projectronin.interop.gradle.spring")
    }

    @Test
    fun `includes interop base plugin`() {
        assertNotNull(project.plugins.findPlugin("com.projectronin.interop.gradle.base"))
    }

    @Test
    fun `includes kotlin spring plugin`() {
        assertNotNull(project.plugins.findPlugin("org.jetbrains.kotlin.plugin.spring"))
    }

    @Test
    fun `includes spring dependencies`() {
        val mainSourceSet = project.sourceSets().getByName(SourceSet.MAIN_SOURCE_SET_NAME)
        mainSourceSet.compileClasspath.assertHasJars("spring-context")
    }
}
