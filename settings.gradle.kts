rootProject.name = "interop-gradle"

pluginManagement {
    plugins {
        kotlin("jvm") version "1.8.0"
        id("org.jlleitschuh.gradle.ktlint") version "11.1.0"
        id("pl.allegro.tech.build.axion-release") version "1.14.4"
        id("com.dipien.releaseshub.gradle.plugin") version "4.0.0"
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
