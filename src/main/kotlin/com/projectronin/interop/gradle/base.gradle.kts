package com.projectronin.interop.gradle

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import pl.allegro.tech.build.axion.release.domain.TagNameSerializationConfig
import pl.allegro.tech.build.axion.release.domain.properties.TagProperties
import pl.allegro.tech.build.axion.release.domain.scm.ScmPosition

plugins {
    kotlin("jvm")

    id("org.jlleitschuh.gradle.ktlint")
    id("pl.allegro.tech.build.axion-release")
}

java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    maven {
        url = uri("https://maven.pkg.github.com/projectronin/package-repo")
        credentials {
            username = System.getenv("PACKAGE_USER")
            password = System.getenv("PACKAGE_TOKEN")
        }
    }
    maven {
        url = uri("https://repo.devops.projectronin.io/repository/maven-releases/")
        mavenContent {
            releasesOnly()
        }
    }

    // Only include the snapshot repo if this is not a release task
    if (System.getProperty("ronin.release", "false") == "false") {
        maven {
            url = uri("https://repo.devops.projectronin.io/repository/maven-snapshots/")
            mavenContent {
                snapshotsOnly()
            }
        }
    }

    maven {
        url = uri("https://repo.devops.projectronin.io/repository/maven-public/")
        mavenContent {
            releasesOnly()
        }
    }
    mavenLocal()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

// Versioning/release
scmVersion {
    tag(
        closureOf<TagNameSerializationConfig> {
            initialVersion = KotlinClosure2<TagProperties, ScmPosition, String>({ _, _ -> "1.0.0" })
            prefix = ""
        }
    )
    versionCreator = KotlinClosure2<String, ScmPosition, String>({ versionFromTag, position ->
        if (position.branch != "master" && position.branch != "HEAD") {
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

project.version = scmVersion.version

dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.31")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.5.31")

    // Logging
    implementation("io.github.microutils:kotlin-logging:1.12.5")
    implementation("org.slf4j:slf4j-api:1.7.32")
    runtimeOnly("ch.qos.logback:logback-classic:1.2.6")
}
