package com.projectronin.interop.gradle

import gradle.kotlin.dsl.accessors._3081ed7e6bb658519cc365c772992eb9.implementation
import org.gradle.kotlin.dsl.dependencies

plugins {
    id("com.projectronin.interop.gradle.base")
}

val jacksonVersion = "2.12.3"

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
}
