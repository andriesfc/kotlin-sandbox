
plugins {
    id("buildlogic.kotlin-application-conventions")
}

dependencies {
    implementation(project(":utilities"))
}

application {
    mainClass = "sandbox.app.AppKt"
}
