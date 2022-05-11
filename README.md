[![codecov](https://codecov.io/gh/projectronin/interop-gradle/branch/master/graph/badge.svg?token=5KEAfntolA)](https://app.codecov.io/gh/projectronin/interop-gradle/branch/master)
[![Tests](https://github.com/projectronin/interop-gradle/actions/workflows/test.yml/badge.svg)](https://github.com/projectronin/interop-gradle/actions/workflows/test.yml)
[![Lint](https://github.com/projectronin/interop-gradle/actions/workflows/lint.yml/badge.svg)](https://github.com/projectronin/interop-gradle/actions/workflows/lint.yml)

# interop-gradle

This project contains all the plugins created by the Interops team for our Gradle build. Below you will find a
description of each of the currently available plugins.

### Precompiled Script Plugins

* [com.projectronin.interop.gradle.base](src/main/kotlin/com/projectronin/interop/gradle/base.gradle.kts) - defines the
  base configuration for Interops projects and is the basis for all other plugins.
* [com.projectronin.interop.gradle.integration](src/main/kotlin/com/projectronin/interop/gradle/integration.gradle.kts)
    - defines the base setup utilized by Integration tests.
* [com.projectronin.interop.gradle.jacoco](src/main/kotlin/com/projectronin/interop/gradle/jacoco.gradle.kts) - defines
  the [Jacoco](https://www.jacoco.org/jacoco/)
  configuration and should be utilized by any projects requiring testing and code coverage.
* [com.projectronin.interop.gradle.junit](src/main/kotlin/com/projectronin/interop/gradle/junit.gradle.kts) - builds
  on **interop.jacoco** and provides the basic configuration for utilizing [JUnit Jupiter](https://junit.org/junit5/),
  including the interop.jacoco plugin.
* [com.projectronin.interop.gradle.publish](src/main/kotlin/com/projectronin/interop/gradle/publish.gradle.kts) -
  defines the configuration enabling publishing of Maven artifacts
* [com.projectronin.interop.gradle.spring](src/main/kotlin/com/projectronin/interop/gradle/spring.gradle.kts) - defines
  the Spring configuration and should be utilized by any project that uses [Spring](https://spring.io/).
* [com.projectronin.interop.gradle.version](src/main/kotlin/com/projectronin/interop/gradle/version.gradle.kts) -
  defines
  the setup for automated version generation through
  the [Axion Release Plugin](https://axion-release-plugin.readthedocs.io/en/latest/)
