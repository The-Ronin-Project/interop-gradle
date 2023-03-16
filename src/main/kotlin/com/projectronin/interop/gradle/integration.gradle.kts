package com.projectronin.interop.gradle

plugins {
    id("com.projectronin.interop.gradle.junit")
    `jvm-test-suite`
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
        }

        val it by registering(JvmTestSuite::class) {
            dependencies {
                implementation(project())
            }
            sources {
                java {
                    setSrcDirs(listOf("src/it/kotlin"))
                }
                resources {
                    setSrcDirs(listOf("src/it/resources"))
                }
            }
        }
    }
}
