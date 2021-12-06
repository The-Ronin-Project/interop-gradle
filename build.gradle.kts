plugins {
    `kotlin-dsl`

    jacoco
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
}

repositories {
    mavenCentral()
    maven(url = "https://plugins.gradle.org/m2/")
}

dependencies {
    implementation("io.spring.gradle:dependency-management-plugin:1.0.11.RELEASE")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31")
    implementation("org.jetbrains.kotlin:kotlin-allopen:1.5.31")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:10.2.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.7.0")
}

// Setup Jacoco for the tests
tasks.withType<Test> {
    useJUnitPlatform()
}

jacoco {
    toolVersion = "0.8.7"
    // Custom reports directory can be specfied like this:
    reportsDirectory.set(file("./codecov"))
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        csv.required.set(false)
        html.required.set(true)
    }
}

tasks {
    test {
        testLogging.showStandardStreams = true
        testLogging.showExceptions = true
    }
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}
