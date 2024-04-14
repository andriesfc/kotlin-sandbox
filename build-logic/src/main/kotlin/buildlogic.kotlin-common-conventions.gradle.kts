@file:Suppress("UnstableApiUsage")

import buildlogic.catalog
import buildlogic.lib
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


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
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.add("-Xjsr305=strict")
        freeCompilerArgs.add("-Xjdk-release=21")
    }
}

dependencies {
    testImplementation(catalog.lib("kotest-assertions-core"))
    testImplementation(catalog.lib("kotest-runner-junit5"))
}