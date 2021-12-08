package com.projectronin.interop.gradle

import org.gradle.kotlin.dsl.dependencies

plugins {
    id("com.projectronin.interop.gradle.jacoco")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
