package com.projectronin.interop.gradle

plugins {
    id("com.projectronin.interop.gradle.base")

    // Ensures that Spring annotated classes are open in Kotlin. See https://kotlinlang.org/docs/all-open-plugin.html#spring-support
    kotlin("plugin.spring")
}

dependencies {
    implementation(platform("org.springframework:spring-framework-bom:5.3.22"))
    implementation("org.springframework:spring-context")
}
