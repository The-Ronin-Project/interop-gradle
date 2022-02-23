import pl.allegro.tech.build.axion.release.domain.TagNameSerializationConfig
import pl.allegro.tech.build.axion.release.domain.properties.TagProperties
import pl.allegro.tech.build.axion.release.domain.scm.ScmPosition

plugins {
    `kotlin-dsl`
    `maven-publish`
    jacoco
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
    id("pl.allegro.tech.build.axion-release") version "1.13.6"
}

// Versioning/release
scmVersion {
    tag(
        closureOf<TagNameSerializationConfig> {
            initialVersion = KotlinClosure2<TagProperties, ScmPosition, String>({ _, _ -> "1.0.0" })
            prefix = ""
        }
    )
    versionCreator = KotlinClosure2<String, ScmPosition, String>({ versionFromTag, position ->
        if (position.branch != "master" && position.branch != "HEAD") {
            val jiraBranchRegex = Regex("(\\w+)-(\\d+)-(.+)")
            val match = jiraBranchRegex.matchEntire(position.branch)

            val branchExtension = match?.let {
                val (project, number, _) = it.destructured
                "$project$number"
            } ?: position.branch

            "$versionFromTag-$branchExtension"
        } else {
            versionFromTag
        }
    })
}

project.version = scmVersion.version

repositories {
    maven {
        url = uri("https://repo.devops.projectronin.io/repository/maven-public/")
        mavenContent {
            releasesOnly()
        }
    }
    maven(url = "https://plugins.gradle.org/m2/")
}

dependencies {
    implementation("io.spring.gradle:dependency-management-plugin:1.0.11.RELEASE")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31")
    implementation("org.jetbrains.kotlin:kotlin-allopen:1.5.31")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:10.2.0")
    implementation("pl.allegro.tech.build:axion-release-plugin:1.13.6")
    implementation("org.unbroken-dome.gradle-plugins:gradle-testsets-plugin:4.0.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.7.0")
    // Allows us to change environment variables
    testImplementation("org.junit-pioneer:junit-pioneer:1.5.0")
    // For axion-release
    testImplementation("org.apache.sshd:sshd-core:2.8.0")
    testImplementation("io.mockk:mockk:1.12.0")
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

// ktlint includes the generated-sources, which includes the classes created by Gradle for these plugins
ktlint {
    enableExperimentalRules.set(true)
    filter {
        // We should be able to just do a wildcard exclude, but it's not working.
        // This solution comes from https://github.com/JLLeitschuh/ktlint-gradle/issues/222#issuecomment-480758375
        exclude { projectDir.toURI().relativize(it.file.toURI()).path.contains("/generated-sources/") }
    }
}

// Publishing
publishing {
    repositories {
        maven {
            name = "nexus"
            credentials {
                username = System.getenv("NEXUS_USER")
                password = System.getenv("NEXUS_TOKEN")
            }
            url = if (project.version.toString().endsWith("SNAPSHOT")) {
                uri("https://repo.devops.projectronin.io/repository/maven-snapshots/")
            } else {
                uri("https://repo.devops.projectronin.io/repository/maven-releases/")
            }
        }
    }
    publications {
        create<MavenPublication>("library") {
            from(components["java"])
        }
    }
}

tasks.register("install") {
    dependsOn(tasks.publishToMavenLocal)
}
