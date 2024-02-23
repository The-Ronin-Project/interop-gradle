package com.projectronin.interop.gradle

plugins {
    id("com.projectronin.interop.gradle.integration")
    id("com.projectronin.interop.gradle.spring-boot")
}

val itSetup =
    tasks.create("itSetup") {
        val clean = tasks.named("clean")
        val bootJar = tasks.named("bootJar")

        dependsOn(clean)
        dependsOn(bootJar)

        bootJar.get().mustRunAfter(clean)
    }

val runDocker =
    tasks.create("runDocker") {
        dependsOn(itSetup)

        doLast {
            exec {
                workingDir = file("./src/it/resources")
                commandLine("docker compose -f docker-compose-it.yaml up -d --wait --wait-timeout 600".split(" "))
            }
        }
    }

val stopDocker =
    tasks.create("stopDocker") {
        doLast {
            exec {
                workingDir = file("./src/it/resources")
                commandLine("docker compose -f docker-compose-it.yaml down".split(" "))
            }
        }
    }

tasks.named("it").get().apply {
    dependsOn(runDocker)

    finalizedBy(stopDocker)
}
