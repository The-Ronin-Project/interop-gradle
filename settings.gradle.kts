rootProject.name = "interop-gradle"

pluginManagement {
    plugins {
        kotlin("jvm") version "1.6.10"
        id("org.jlleitschuh.gradle.ktlint") version "10.3.0"
        id("pl.allegro.tech.build.axion-release") version "1.14.0"
        id("com.dipien.releaseshub.gradle.plugin") version "3.1.0"
    }

    repositories {
        maven {
            url = uri("https://repo.devops.projectronin.io/repository/maven-public/")
            mavenContent {
                releasesOnly()
            }
        }
        gradlePluginPortal()
    }
}
