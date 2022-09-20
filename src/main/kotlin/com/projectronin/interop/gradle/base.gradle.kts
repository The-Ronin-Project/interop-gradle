package com.projectronin.interop.gradle

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")

    id("org.jlleitschuh.gradle.ktlint")
    id("com.dipien.releaseshub.gradle.plugin")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11

    // Generate sources and javadoc JARs. These will automatically be published in publishing is active.
    withSourcesJar()
    withJavadocJar()
}

repositories {
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

gradle.taskGraph.whenReady {
    // If we have a KtlintFormat task, we will disable our ktlint checks.
    // We do this because they use a cached view of the code, so anything that has been formatted will also fail.
    // But we still want ktlintCheck to be able to run generally, if needed/desired.
    if (gradle.taskGraph.allTasks.any { it.name.contains("KtlintFormat") }) {
        // We need to cleanup a few directories first in case any Ktlint data is currently there. If it is, we may end up flagging a failure that has already been fixed.
        File("${project.buildDir}/reports/ktlint").deleteRecursively()
        File("${project.buildDir}/intermediates/ktLint").deleteRecursively()

        gradle.taskGraph.allTasks.forEach {
            if (it.name.contains("KtlintCheck")) {
                logger.info("Disabling $it due to presence of KtlintFormat")
                it.enabled = false
            }
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }

    dependsOn(tasks.ktlintFormat)
}

dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.10")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.10")

    // Logging
    implementation("io.github.microutils:kotlin-logging:3.0.0")
    implementation("org.slf4j:slf4j-api:2.0.1")
    runtimeOnly("ch.qos.logback:logback-classic:1.4.1")
}
