package com.projectronin.interop.gradle

plugins {
    id("com.projectronin.interop.gradle.jacoco")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
