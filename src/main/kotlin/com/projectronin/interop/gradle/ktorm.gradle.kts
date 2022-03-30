package com.projectronin.interop.gradle

plugins {
    id("com.projectronin.interop.gradle.junit")
}

dependencies {
    implementation(platform("org.testcontainers:testcontainers-bom:1.16.3"))

    // Ktorm
    implementation("org.ktorm:ktorm-core:3.4.1")
    implementation("org.ktorm:ktorm-support-mysql:3.4.1")

    // Test Containers
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:mysql")
    testRuntimeOnly("mysql:mysql-connector-java:8.0.28")

    // Database Rider
    testImplementation("com.github.database-rider:rider-junit5:1.32.3")

    // Liquibase
    testImplementation("org.liquibase:liquibase-core:4.9.1")
    testRuntimeOnly("com.mattbertolini:liquibase-slf4j:4.0.0")
    testRuntimeOnly("org.yaml:snakeyaml:1.30")
}
