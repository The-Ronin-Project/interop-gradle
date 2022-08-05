import org.gradle.kotlin.dsl.support.serviceOf
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import pl.allegro.tech.build.axion.release.domain.properties.TagProperties

plugins {
    kotlin("jvm")
    `kotlin-dsl`
    `maven-publish`
    jacoco
    id("org.jlleitschuh.gradle.ktlint")
    id("pl.allegro.tech.build.axion-release")
    id("com.dipien.releaseshub.gradle.plugin")
}

// Java/Kotlin versioning
java {
    sourceCompatibility = JavaVersion.VERSION_11
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

// Versioning/release
scmVersion {
    tag {
        initialVersion(TagProperties.InitialVersionSupplier { _, _ -> "1.0.0" })
        prefix.set("")
    }
    versionCreator { versionFromTag, position ->
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
    }
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
    implementation(libs.bundles.kotlin)
    implementation(libs.ktlint.gradle)
    implementation(libs.axion.release.plugin)
    implementation(libs.gradle.testsets.plugin)
    implementation(libs.releases.hub.gradle.plugin)

    testImplementation(libs.junit.jupiter)
    // Allows us to change environment variables
    testImplementation(libs.junit.pioneer)
    // For axion-release
    testImplementation(libs.sshd.core)
    testImplementation(libs.mockk)

    // See https://github.com/gradle/gradle/issues/16774#issuecomment-853407822
    testRuntimeOnly(
        files(serviceOf<org.gradle.api.internal.classpath.ModuleRegistry>().getModule("gradle-tooling-api-builders").classpath.asFiles.first())
    )
}

// Setup Jacoco for the tests
tasks.withType<Test> {
    useJUnitPlatform()

    jvmArgs("--add-opens=java.base/java.util=ALL-UNNAMED")
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

// Release Hubs
releasesHub {
    // We want to inspect all the .kts pre-compiled scripts we're using as well.
    // Note that this will still include our base files.
    dependenciesPaths = File("src/main/kotlin").walk().filter { it.name.endsWith(".kts") }.map { it.path }.toList()
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
}

tasks.register("install") {
    dependsOn(tasks.publishToMavenLocal)
}
