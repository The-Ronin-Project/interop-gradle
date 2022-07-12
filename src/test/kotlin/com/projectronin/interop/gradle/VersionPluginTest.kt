package com.projectronin.interop.gradle

import io.mockk.every
import io.mockk.mockk
import org.gradle.api.Project
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import pl.allegro.tech.build.axion.release.domain.VersionConfig
import pl.allegro.tech.build.axion.release.domain.properties.TagProperties
import pl.allegro.tech.build.axion.release.domain.scm.ScmPosition

class VersionPluginTest {
    private lateinit var project: Project

    @BeforeEach
    fun setup() {
        project = getProject()
        project.pluginManager.apply("com.projectronin.interop.gradle.version")
    }

    @Test
    fun `includes interop base plugin`() {
        assertNotNull(project.plugins.findPlugin("com.projectronin.interop.gradle.base"))
    }

    @Test
    fun `includes axion release plugin`() {
        assertNotNull(project.plugins.findPlugin("pl.allegro.tech.build.axion-release"))
    }

    @Test
    fun `axion sets initial version to 1_0_0`() {
        assertEquals(
            "1.0.0",
            project.getExtension<VersionConfig>("scmVersion").tag.initialVersion.apply(
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
        val version = project.getExtension<VersionConfig>("scmVersion").versionCreator.apply(versionFromTag, position)
        assertEquals("1.0.0", version)
    }

    @Test
    fun `axion does not change version for main`() {
        val versionFromTag = "1.0.0"
        val position = mockk<ScmPosition> {
            every { branch } returns "main"
        }
        val version = project.getExtension<VersionConfig>("scmVersion").versionCreator.apply(versionFromTag, position)
        assertEquals("1.0.0", version)
    }

    @Test
    fun `axion does not change version for HEAD`() {
        val versionFromTag = "1.0.0"
        val position = mockk<ScmPosition> {
            every { branch } returns "HEAD"
        }
        val version = project.getExtension<VersionConfig>("scmVersion").versionCreator.apply(versionFromTag, position)
        assertEquals("1.0.0", version)
    }

    @Test
    fun `axion uses full branch when not following JIRA pattern`() {
        val versionFromTag = "1.0.0"
        val position = mockk<ScmPosition> {
            every { branch } returns "my-branch"
        }
        val version = project.getExtension<VersionConfig>("scmVersion").versionCreator.apply(versionFromTag, position)
        assertEquals("1.0.0-my-branch", version)
    }

    @Test
    fun `axion uses shortened branch when following JIRA pattern`() {
        val versionFromTag = "1.0.0"
        val position = mockk<ScmPosition> {
            every { branch } returns "JIRA-7354-my-branch"
        }
        val version = project.getExtension<VersionConfig>("scmVersion").versionCreator.apply(versionFromTag, position)
        assertEquals("1.0.0-JIRA7354", version)
    }
}
