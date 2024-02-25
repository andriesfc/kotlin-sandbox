@file:Suppress("UnstableApiUsage")

import buildlogic.catalog
import buildlogic.lib


plugins {
    id("org.jetbrains.kotlin.jvm")
}

repositories {
    mavenCentral()
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter("5.10.0")
        }
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(8)
    }
}

dependencies {
    testImplementation(catalog.lib("kotest-assertions-core"))
    testImplementation(catalog.lib("kotest-runner-junit5"))
}