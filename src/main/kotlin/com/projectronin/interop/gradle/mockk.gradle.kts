package com.projectronin.interop.gradle

import gradle.kotlin.dsl.accessors._583494fba9f2455342692d57689d5952.testImplementation
import org.gradle.kotlin.dsl.dependencies

plugins {
    id("com.projectronin.interop.gradle.junit")
}

dependencies {
    // Mockk
    testImplementation("io.mockk:mockk:1.12.0")
}
