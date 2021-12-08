package com.projectronin.interop.gradle

import org.gradle.kotlin.dsl.jacoco

plugins {
    id("com.projectronin.interop.gradle.base")
    jacoco
}

jacoco {
    toolVersion = "0.8.7"
    // Custom reports directory can be specfied like this:
    reportsDirectory.set(file("./codecov"))
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        csv.required.set(false)
        html.required.set(true)
    }
}

tasks {
    test {
        testLogging.showStandardStreams = true
        testLogging.showExceptions = true
    }
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}
