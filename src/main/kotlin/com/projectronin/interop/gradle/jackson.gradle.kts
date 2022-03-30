package com.projectronin.interop.gradle

plugins {
    id("com.projectronin.interop.gradle.base")
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-core:2.13.2")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.13.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.2.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.13.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.2")
}
