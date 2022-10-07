package com.projectronin.interop.gradle

import gradle.kotlin.dsl.accessors._4162334096b86e34875e2238f5fb9aff.publishToMavenLocal

plugins {
    id("com.projectronin.interop.gradle.spring")
    id("org.springframework.boot")
    `maven-publish`
}

// Ensure that the jar task does not run. We should only get the bootJar output.
tasks.getByName<Jar>("jar") {
    enabled = false
}

publishing {
    repositories {
        maven {
            name = "nexus"
            credentials {
                username = System.getenv("NEXUS_USER")
                password = System.getenv("NEXUS_TOKEN")
            }
            url = if (project.version.toString().endsWith("SNAPSHOT")) {
                uri("https://repo.devops.projectronin.io/repository/maven-snapshots/")
            } else {
                uri("https://repo.devops.projectronin.io/repository/maven-releases/")
            }
        }
    }
    publications {
        create<MavenPublication>("bootJava") {
            artifact(tasks.getByName("bootJar"))
        }
    }
}

// Create a shorthand task named "install" that triggers to maven local publishing
tasks.register("install") {
    dependsOn(tasks.publishToMavenLocal)
}
