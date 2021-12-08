[![Lint](https://github.com/projectronin/interop-gradle/actions/workflows/lint.yml/badge.svg)](https://github.com/projectronin/interop-gradle/actions/workflows/lint.yml)

# interop-gradle

This project contains all the plugins created by the Interops team for our Gradle build. Below you will find a
description of each of the currently available plugins.

### Precompiled Script Plugins

* [interop.base](src/main/kotlin/interop.base.gradle.kts) - defines the base configuration for Interops projects and is
  the basis for all other plugins.
* [interop.jackson](src/main/kotlin/interop.jackson.gradle.kts) - defines the Jackson configuration that should be
  utilized for handling JSON parsing leveraging [Jackson](https://github.com/FasterXML/jackson)
* [interop.jacoco](src/main/kotlin/interop.jacoco.gradle.kts) - defines the [Jacoco](https://www.jacoco.org/jacoco/)
  configuration and should be utilized by any projects requiring testing and code coverage.
* [interop.junit](src/main/kotlin/interop.junit.gradle.kts) - builds on **interop.jacoco** and provides the basic
  configuration for utilizing [JUnit Jupiter](https://junit.org/junit5/), including the interop.jacoco plugin.
* [interop.ktor](src/main/kotlin/interop.ktor.gradle.kts) - defines the ktor configuration for Interops projects and
  should be utilized by any project using [Ktor](https://ktor.io/).
* [interop.ktorm](src/main/kotlin/interop.ktorm.gradle.kts) - defines the ktorm configuration for Interops projects and
  should be utilized by any project using [Ktorm](https://www.ktorm.org/).
* [interop.mockk](src/main/kotlin/interop.mockk.gradle.kts) - defines the configuration for the mocking
  framework [MockK](https://mockk.io/) and should be used by any project utilizing mock testing.
* [interop.spring](src/main/kotlin/interop.spring.gradle.kts) - defines the Spring configuration and should be utilized
  by any project that uses [Spring](https://spring.io/).
