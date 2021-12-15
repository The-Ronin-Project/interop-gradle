package com.projectronin.interop.gradle

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
        configure<JacocoTaskExtension> {
            // Exclude FHIR parsers as they are too large for instrumentation
            excludes = listOf("org/hl7/fhir/r5/formats/**", "org/hl7/fhir/r4/formats/**")
        }
    }
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}
