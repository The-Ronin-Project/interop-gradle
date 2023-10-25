package com.projectronin.interop.gradle

plugins {
    id("com.projectronin.interop.gradle.base")
    id("org.sonarqube")
}

sonar {
    properties {
        property("sonar.projectKey", project.rootProject.name)
        property("sonar.newCode.referenceBranch", "main")
    }
}

subprojects {
    sonar {
        properties {
            property("sonar.sources", "src/main")
            property("sonar.tests", "src/test")
        }
    }
}
