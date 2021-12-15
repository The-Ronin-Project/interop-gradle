package com.projectronin.interop.gradle

plugins {
    id("com.projectronin.interop.gradle.base")
}

val jacksonVersion = "2.12.3"

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
}
