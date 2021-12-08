package com.projectronin.interop.gradle

import gradle.kotlin.dsl.accessors._3081ed7e6bb658519cc365c772992eb9.implementation
import gradle.kotlin.dsl.accessors._3081ed7e6bb658519cc365c772992eb9.java
import gradle.kotlin.dsl.accessors._3081ed7e6bb658519cc365c772992eb9.runtimeOnly
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.repositories
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")

    // Adds Maven-like Dependency Management capabilities. See https://docs.spring.io/dependency-management-plugin/docs/current/reference/html/
    id("io.spring.dependency-management")

    id("org.jlleitschuh.gradle.ktlint")
}

java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.31")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.5.31")

    // Logging
    implementation("io.github.microutils:kotlin-logging:1.12.5")
    implementation("org.slf4j:slf4j-api:1.7.32")
    runtimeOnly("ch.qos.logback:logback-classic:1.2.6")
}
