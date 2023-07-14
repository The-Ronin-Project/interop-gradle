plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

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
    implementation(libs.releases.hub.gradle.plugin)
}

gradlePlugin {
    plugins {
        create("interopGradleDependencyPlugin") {
            id = "com.projectronin.interop.gradle.dependency"
            implementationClass = "com.projectronin.interop.gradle.GradleDependencyPlugin"
        }
    }
}
