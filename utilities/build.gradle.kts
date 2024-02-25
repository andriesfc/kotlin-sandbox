@file:Suppress("SpellCheckingInspection")

plugins {
    id("buildlogic.kotlin-library-conventions")
    `java-test-fixtures`
}

dependencies {
    testImplementation(libs.kotest.framework.datatest)
}
