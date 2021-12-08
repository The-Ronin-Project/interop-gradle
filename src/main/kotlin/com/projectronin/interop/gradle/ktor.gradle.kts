package com.projectronin.interop.gradle

plugins {
    id("com.projectronin.interop.gradle.base")
}

val ktorVersion = "1.6.3"

dependencies {
    // Ktor
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-jackson:$ktorVersion") {
        exclude(group = "junit")
    }
    implementation("io.ktor:ktor-auth-jwt:$ktorVersion") {
        exclude(group = "junit")
    }

    testImplementation("io.ktor:ktor-client-mock:$ktorVersion")
}
