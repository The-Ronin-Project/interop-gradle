plugins {
    id("interop.base")

    // Ensures that Spring annotated classes are open in Kotlin. See https://kotlinlang.org/docs/all-open-plugin.html#spring-support
    kotlin("plugin.spring")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework:spring-framework-bom:5.3.10")
    }
}