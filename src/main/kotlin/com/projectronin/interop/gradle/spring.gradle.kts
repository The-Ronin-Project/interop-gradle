package com.projectronin.interop.gradle

import gradle.kotlin.dsl.accessors._d02240e388341d2373196f1bcae80b57.dependencyManagement
import org.gradle.kotlin.dsl.kotlin

plugins {
    id("com.projectronin.interop.gradle.base")

    // Ensures that Spring annotated classes are open in Kotlin. See https://kotlinlang.org/docs/all-open-plugin.html#spring-support
    kotlin("plugin.spring")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework:spring-framework-bom:5.3.10")
    }
}
