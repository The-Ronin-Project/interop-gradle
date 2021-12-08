package com.projectronin.interop.gradle

import gradle.kotlin.dsl.accessors._583494fba9f2455342692d57689d5952.dependencyManagement
import gradle.kotlin.dsl.accessors._583494fba9f2455342692d57689d5952.implementation
import gradle.kotlin.dsl.accessors._583494fba9f2455342692d57689d5952.testImplementation
import gradle.kotlin.dsl.accessors._583494fba9f2455342692d57689d5952.testRuntimeOnly
import org.gradle.kotlin.dsl.dependencies

plugins {
    id("com.projectronin.interop.gradle.junit")
}

val ktormVersion = "3.4.1"

dependencyManagement {
    imports {
        mavenBom("org.testcontainers:testcontainers-bom:1.16.0")
    }
    dependencies {
        dependency("org.liquibase:liquibase-core:4.4.3")
        dependency("com.github.database-rider:rider-junit5:1.29.0")
    }
}

dependencies {
    // Ktorm
    implementation("org.ktorm:ktorm-core:$ktormVersion")
    implementation("org.ktorm:ktorm-support-mysql:$ktormVersion")

    // Test Containers
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:mysql")
    testRuntimeOnly("mysql:mysql-connector-java:8.0.26")

    // Database Rider
    testImplementation("com.github.database-rider:rider-junit5")

    // Liquibase
    testImplementation("org.liquibase:liquibase-core")
    testRuntimeOnly("com.mattbertolini:liquibase-slf4j:4.0.0")
    testRuntimeOnly("org.yaml:snakeyaml:1.29")
}
