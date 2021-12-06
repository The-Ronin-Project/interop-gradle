[![Lint](https://github.com/projectronin/ichi/actions/workflows/interop_lint.yml/badge.svg)](https://github.com/projectronin/ichi/actions/workflows/interop_lint.yml)

# interop-gradle

This project contains all the plugins created by the Interops team for our Gradle build. Below you will find a
description of each of the currently available plugins.

### interop.base

This [plugin](src/main/kotlin/interop.base.gradle.kts) defines the base configuration for Interops projects and is the
basis for all other plugins.

### interop.jackson

This [plugin](src/main/kotlin/interop.jackson.gradle.kts) defines the Jackson configuration that should be utilized for
handling JSON parsing leveraging [Jackson](https://github.com/FasterXML/jackson)

### interop.jacoco

This [plugin](src/main/kotlin/interop.jacoco.gradle.kts) defines the [Jacoco](https://www.jacoco.org/jacoco/)
configuration and should be utilized by any projects requiring testing and code coverage.

### interop.junit

This [plugin](src/main/kotlin/interop.junit.gradle.kts) builds on **interop.jacoco** and provides the basic
configuration for utilizing [JUnit Jupiter](https://junit.org/junit5/), including the interop.jacoco plugin.

### interop.ktor

This [plugin](src/main/kotlin/interop.ktor.gradle.kts) defines the ktor configuration for Interops projects and should
be utilized by any project using [Ktor](https://ktor.io/).

### interop.ktorm

This [plugin](src/main/kotlin/interop.ktorm.gradle.kts) defines the ktorm configuration for Interops projects and should
be utilized by any project using [Ktorm](https://www.ktorm.org/).

### interop.mockk

This [plugin](src/main/kotlin/interop.mockk.gradle.kts) defines the configuration for the mocking
framework [MockK](https://mockk.io/) and should be used by any project utilizing mock testing.

### interop.spring

This [plugin](src/main/kotlin/interop.spring.gradle.kts) defines the Spring configuration and should be utilized by any
project that uses [Spring](https://spring.io/).
