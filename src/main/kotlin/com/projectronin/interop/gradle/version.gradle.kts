package com.projectronin.interop.gradle

import pl.allegro.tech.build.axion.release.domain.TagNameSerializationConfig
import pl.allegro.tech.build.axion.release.domain.properties.TagProperties
import pl.allegro.tech.build.axion.release.domain.scm.ScmPosition

plugins {
    id("com.projectronin.interop.gradle.base")
    id("pl.allegro.tech.build.axion-release")
}

rootProject.apply {
    scmVersion {
        tag(
            closureOf<TagNameSerializationConfig> {
                initialVersion = KotlinClosure2<TagProperties, ScmPosition, String>({ _, _ -> "1.0.0" })
                prefix = ""
            }
        )
        versionCreator = KotlinClosure2<String, ScmPosition, String>({ versionFromTag, position ->
            val supportedHeads = setOf("HEAD", "master", "main")
            if (!supportedHeads.contains(position.branch)) {
                val jiraBranchRegex = Regex("(\\w+)-(\\d+)-(.+)")
                val match = jiraBranchRegex.matchEntire(position.branch)
                val branchExtension = match?.let {
                    val (project, number, _) = it.destructured
                    "$project$number"
                } ?: position.branch

                "$versionFromTag-$branchExtension"
            } else {
                versionFromTag
            }
        })
    }
}

allprojects {
    project.version = project.rootProject.scmVersion.version
}
