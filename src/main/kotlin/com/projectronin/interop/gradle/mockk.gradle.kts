package com.projectronin.interop.gradle

plugins {
    id("com.projectronin.interop.gradle.junit")
}

dependencies {
    // Mockk
    testImplementation("io.mockk:mockk:1.12.0")
}
