package com.projectronin.interop.gradle

import gradle.kotlin.dsl.accessors._583494fba9f2455342692d57689d5952.testImplementation
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
