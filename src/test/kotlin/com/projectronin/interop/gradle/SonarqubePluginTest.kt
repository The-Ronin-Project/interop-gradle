package com.projectronin.interop.gradle

import org.gradle.api.Project
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.sonarqube.gradle.ActionBroadcast
import org.sonarqube.gradle.SonarExtension
import org.sonarqube.gradle.SonarProperties

class SonarqubePluginTest {
    private lateinit var project: Project

    @BeforeEach
    fun setup() {
        project = getProject("sonarqube-test")
        project.pluginManager.apply("com.projectronin.interop.gradle.sonarqube")
    }

    @Test
    fun `includes interop base plugin`() {
        assertNotNull(project.plugins.findPlugin("com.projectronin.interop.gradle.base"))
    }

    @Test
    fun `includes sonarqube plugin`() {
        assertNotNull(project.plugins.findPlugin("org.sonarqube"))
    }

    @Test
    fun `sets up sonarqube config`() {
        val sonarqube = project.getExtension<SonarExtension>("sonar")
        val propertiesActionsField = SonarExtension::class.java.getDeclaredField("propertiesActions")
        propertiesActionsField.isAccessible = true
        val actionBroadcast = propertiesActionsField.get(sonarqube) as ActionBroadcast<SonarProperties>

        val properties = SonarProperties(mutableMapOf())
        actionBroadcast.execute(properties)

        assertEquals("sonarqube-test", properties.properties["sonar.projectKey"])

        // We specifically do not want this set.
        assertNull(properties.properties["sonar.newCode.referenceBranch"])
    }
}
