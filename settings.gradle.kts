rootProject.name = "interop-gradle"

pluginManagement {
    plugins {
        kotlin("jvm") version "1.6.10"
        id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
        id("pl.allegro.tech.build.axion-release") version "1.14.2"
        id("com.dipien.releaseshub.gradle.plugin") version "3.2.0"
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
