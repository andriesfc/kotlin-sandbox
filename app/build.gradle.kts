
plugins {
    id("buildlogic.kotlin-application-conventions")
}

dependencies {
    implementation(project(":utilities"))
    implementation(libs.poi)
    implementation(libs.poi.ooxml)
    implementation(libs.poi.ooxml.full)
    implementation(libs.picocli)
    implementation(libs.slf4j.api)
    implementation(libs.slf4j.jdk.platform.logging)
    testImplementation(libs.slf4j.simple)
}

application {
    applicationName = "sandbox-app"
    mainClass = "sandbox.app.AppKt"
}
