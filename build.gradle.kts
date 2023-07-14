import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.gradle.kotlin.dsl.support.serviceOf
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    `kotlin-dsl`
    `maven-publish`
    jacoco
    alias(libs.plugins.ktlint)
    alias(libs.plugins.axion)
    alias(libs.plugins.benmanes.versions)
    alias(libs.plugins.vcu)
    id("com.projectronin.interop.gradle.dependency")
}

// Force our precompiled plugin updates to run when he update version catalogs to ensure they're updated before PR creation.
tasks.getByName("versionCatalogUpdate").dependsOn("precompiledPluginUpdate")

// Java/Kotlin versioning
java {
    sourceCompatibility = JavaVersion.VERSION_11
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
    }

    dependsOn(tasks.ktlintFormat)
}

// Versioning/release
scmVersion {
    tag {
        initialVersion { _, _ -> "1.0.0" }
        prefix.set("")
    }
    versionCreator { versionFromTag, position ->
        if (position.branch != "main" && position.branch != "HEAD") {
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
    implementation(libs.spring.boot.gradle.plugin)
    implementation(libs.benmanes.versions.gradle.plugin)
    implementation(libs.vcu.gradle.plugin)

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

    testLogging {
        events("passed", "skipped", "failed", "standardOut", "standardError")
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

jacoco {
    toolVersion = "0.8.8"
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
}

tasks.register("install") {
    dependsOn(tasks.publishToMavenLocal)
}

tasks.named<DependencyUpdatesTask>("dependencyUpdates").configure {
    revision = "release"
    gradleReleaseChannel = "current"

    fun isNonStable(version: String): Boolean {
        val stableKeyword = listOf("RELEASE", "FINAL", "GA", "JRE").any { version.uppercase().contains(it) }
        val regex = "^[0-9,.v-]+(-r)?$".toRegex()
        val isStable = stableKeyword || regex.matches(version)
        return isStable.not()
    }

    rejectVersionIf {
        isNonStable(candidate.version)
    }
}
