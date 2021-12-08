package com.projectronin.interop.gradle

import gradle.kotlin.dsl.accessors._3081ed7e6bb658519cc365c772992eb9.implementation
import gradle.kotlin.dsl.accessors._3081ed7e6bb658519cc365c772992eb9.testImplementation
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.exclude

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
