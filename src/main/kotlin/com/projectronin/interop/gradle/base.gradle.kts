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

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.10")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.10")

    // Logging
    implementation("io.github.microutils:kotlin-logging:2.1.23")
    implementation("org.slf4j:slf4j-api:2.0.0")
    runtimeOnly("ch.qos.logback:logback-classic:1.4.0")
}
