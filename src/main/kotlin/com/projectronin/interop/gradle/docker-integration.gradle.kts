package com.projectronin.interop.gradle

plugins {
    id("com.projectronin.interop.gradle.integration")
    id("com.projectronin.interop.gradle.spring-boot")
}

var itSetup = tasks.create("itSetup") {
    dependsOn(tasks.clean)
    dependsOn(tasks.bootJar)

    tasks.bootJar.get().mustRunAfter(tasks.clean)

    doLast {
        exec {
            commandLine("docker compose build --no-cache".split(" "))
        }
    }
}

tasks.named("it").get().dependsOn(itSetup)
