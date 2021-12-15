[![codecov](https://codecov.io/gh/projectronin/interop-gradle/branch/master/graph/badge.svg?token=5KEAfntolA)](https://app.codecov.io/gh/projectronin/interop-gradle/branch/master)
[![Tests](https://github.com/projectronin/interop-gradle/actions/workflows/test.yml/badge.svg)](https://github.com/projectronin/interop-gradle/actions/workflows/test.yml)
[![Lint](https://github.com/projectronin/interop-gradle/actions/workflows/lint.yml/badge.svg)](https://github.com/projectronin/interop-gradle/actions/workflows/lint.yml)

# interop-gradle

This project contains all the plugins created by the Interops team for our Gradle build. Below you will find a
description of each of the currently available plugins.

### Precompiled Script Plugins

* [com.projectronin.interop.gradle.base](src/main/kotlin/com/projectronin/interop/gradle/base.gradle.kts) - defines the
  base configuration for Interops projects and is the basis for all other plugins.
* [com.projectronin.interop.gradle.jackson](src/main/kotlin/com/projectronin/interop/gradle/jackson.gradle.kts) -
  defines the Jackson configuration that should be utilized for handling JSON parsing
  leveraging [Jackson](https://github.com/FasterXML/jackson)
* [com.projectronin.interop.gradle.jacoco](src/main/kotlin/com/projectronin/interop/gradle/jacoco.gradle.kts) - defines
  the [Jacoco](https://www.jacoco.org/jacoco/)
  configuration and should be utilized by any projects requiring testing and code coverage.
* [com.projectronin.interop.gradle.junit](src/main/kotlin/com/projectronin/interop/gradle/junit.gradle.kts) - builds
  on **interop.jacoco** and provides the basic configuration for utilizing [JUnit Jupiter](https://junit.org/junit5/),
  including the interop.jacoco plugin.
* [com.projectronin.interop.gradle.ktor](src/main/kotlin/com/projectronin/interop/gradle/ktor.gradle.kts) - defines the
  ktor configuration for Interops projects and should be utilized by any project using [Ktor](https://ktor.io/).
* [com.projectronin.interop.gradle.ktorm](src/main/kotlin/com/projectronin/interop/gradle/ktorm.gradle.kts) - defines
  the ktorm configuration for Interops projects and should be utilized by any project
  using [Ktorm](https://www.ktorm.org/).
* [com.projectronin.interop.gradle.mockk](src/main/kotlin/com/projectronin/interop/gradle/mockk.gradle.kts) - defines
  the configuration for the mocking framework [MockK](https://mockk.io/) and should be used by any project utilizing
  mock testing.
* [com.projectronin.interop.gradle.publish](src/main/kotlin/com/projectronin/interop/gradle/publish.gradle.kts) -
  defines the configuration enabling publishing of Maven artifacts
* [com.projectronin.interop.gradle.spring](src/main/kotlin/com/projectronin/interop/gradle/spring.gradle.kts) - defines
  the Spring configuration and should be utilized by any project that uses [Spring](https://spring.io/).
