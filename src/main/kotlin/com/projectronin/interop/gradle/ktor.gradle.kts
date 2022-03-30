package com.projectronin.interop.gradle

plugins {
    id("com.projectronin.interop.gradle.base")
}

dependencies {
    // Ktor
    implementation("io.ktor:ktor-client-cio:1.6.8")
    implementation("io.ktor:ktor-client-core:1.6.8")
    implementation("io.ktor:ktor-client-jackson:1.6.8") {
        exclude(group = "junit")
    }
    implementation("io.ktor:ktor-auth-jwt:1.6.8") {
        exclude(group = "junit")
    }

    testImplementation("io.ktor:ktor-client-mock:1.6.8")
}
