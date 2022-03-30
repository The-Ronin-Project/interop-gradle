rootProject.name = "interop-gradle"

pluginManagement {
    plugins {
        kotlin("jvm") version "1.6.10"
        id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
        id("pl.allegro.tech.build.axion-release") version "1.13.6"
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
