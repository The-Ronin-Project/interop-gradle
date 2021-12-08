package com.projectronin.interop.gradle

import org.gradle.kotlin.dsl.dependencies

plugins {
    id("com.projectronin.interop.gradle.junit")
}

dependencies {
    // Mockk
    testImplementation("io.mockk:mockk:1.12.0")
}
