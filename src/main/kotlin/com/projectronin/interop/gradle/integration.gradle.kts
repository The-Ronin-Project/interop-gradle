package com.projectronin.interop.gradle

plugins {
    id("com.projectronin.interop.gradle.junit")
    id("org.unbroken-dome.test-sets")
}

testSets {
    "it"()
}
