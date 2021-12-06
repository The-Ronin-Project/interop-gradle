plugins {
    id("interop.junit")
}

val ktormVersion = "3.4.1"

dependencyManagement {
    imports {
        mavenBom("org.testcontainers:testcontainers-bom:1.16.0")
    }
    dependencies {
        dependency("org.liquibase:liquibase-core:4.4.3")
        dependency("com.github.database-rider:rider-junit5:1.29.0")
    }
}

dependencies {
    // Ktorm
    implementation("org.ktorm:ktorm-core:$ktormVersion")
    implementation("org.ktorm:ktorm-support-mysql:$ktormVersion")

    // Test Containers
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:mysql")
    testRuntimeOnly("mysql:mysql-connector-java:8.0.26")

    // Database Rider
    testImplementation("com.github.database-rider:rider-junit5")

    // Liquibase
    testImplementation("org.liquibase:liquibase-core")
    testRuntimeOnly("com.mattbertolini:liquibase-slf4j:4.0.0")
    testRuntimeOnly("org.yaml:snakeyaml:1.29")
}
