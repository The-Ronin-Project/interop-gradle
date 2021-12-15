package com.projectronin.interop.gradle

plugins {
    id("com.projectronin.interop.gradle.base")
    `maven-publish`
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/projectronin/package-repo")
            credentials {
                username = System.getenv("PACKAGE_USER")
                password = System.getenv("PACKAGE_TOKEN")
            }
        }
    }
    publications {
        create<MavenPublication>("library") {
            from(components["java"])
        }
    }
}

// Create a shorthand task named "install" that triggers to maven local publishing
tasks.register("install") {
    dependsOn(tasks.publishToMavenLocal)
}
