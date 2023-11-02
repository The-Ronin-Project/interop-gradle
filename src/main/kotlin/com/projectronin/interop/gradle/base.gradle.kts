package com.projectronin.interop.gradle

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")

    id("org.jlleitschuh.gradle.ktlint")
}

configure<JavaPluginExtension> {
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

tasks.loadKtlintReporters.get().dependsOn(tasks.named("cleanLoadKtlintReporters"))

gradle.taskGraph.whenReady {
    // If we have a KtlintFormat task, we will disable our ktlint checks.
    // We do this because they use a cached view of the code, so anything that has been formatted will also fail.
    // But we still want ktlintCheck to be able to run generally, if needed/desired.
    if (gradle.taskGraph.allTasks.any { it.name.contains("KtlintFormat") }) {
        gradle.taskGraph.allTasks.forEach {
            if (it.name.contains("KtlintCheck")) {
                logger.info("Disabling $it due to presence of KtlintFormat")
                it.enabled = false
            }
        }
    }
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
    }

    dependsOn(tasks.ktlintFormat)
}

dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.20")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.20")

    // Logging
    implementation("io.github.microutils:kotlin-logging:3.0.5")
    implementation("org.slf4j:slf4j-api:2.0.9")
    runtimeOnly("ch.qos.logback:logback-classic:1.4.11")
}
