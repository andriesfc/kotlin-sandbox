@file:Suppress("MemberVisibilityCanBePrivate", "ClassName")

package sandbox.utils

object SampleVersionedArtifacts : List<SampleVersionedArtifacts.fixture> by listOf(
    fixture(
        path = "libs/compileOnly/",
        artifactName = null,
        exectedVersion = null,
    ) testingCase {
        "folder paths (ex: [$path]): results in null baseName and version"
    } withDetails {
        "$testingDetails (NOTE: paths ending in \"\")"
    },
    fixture(
        path = "libs/compileOnly/javaee-api-5.jar",
        artifactName = "javaee-api",
        exectedVersion = Version(5, 0, 0)
    ),
    fixture(
        path = "libs/rt/jackson-dataformat-yaml-2.12.7.jar",
        artifactName = "jackson-dataformat-yaml",
        exectedVersion = Version(2, 12, 7)
    ),
    fixture(
        path = "libs/rt/geronimo-jms_1.1_spec-1.1.1.jar",
        artifactName = "geronimo-jms",
        exectedVersion = Version(1,1,0, "_spec-1.1.1")
    ),
    fixture(
        path = "jackson-core-2.12.7.jar",
        artifactName = "jackson-core",
        exectedVersion = Version(2, 12, 7)
    ),
    fixture(
        path = "jackson-core-asl-1.9.12.jar",
        artifactName = "jackson-core-asl",
        exectedVersion = Version(1, 9, 12)
    ),
    fixture(
        path = "/rt/kotest-framework-core",
        artifactName = "kotest-framework-core",
        exectedVersion = null
    ) testingCase { "sample path *without* version: [$path]" },
    fixture(
        path = "/",
        artifactName = null,
        exectedVersion = null
    ) testingCase { "sample path is root: [$path]" },
    fixture(
        path = "3.1.0",
        artifactName = null,
        exectedVersion = Version(3, 1, 0),
    ) testingCase { "sample path is just a version: [$path]" },
    fixture(
        path = "rt/cisko-engr-3.8.12+R1.jar",
        artifactName = "cisko-engr",
        exectedVersion = Version(3, 8, 12, "+R1")
    )
) {
    data class fixture(
        val path: String,
        val artifactName: String?,
        val exectedVersion: Version?,
        val expectIsReleaseBuild: Boolean = true,
    ) {
        var testing: String =
            "sample path: [$path]"
            private set

        var testingDetails: String = listOf(
            "path" to path,
            "expected base name" to (artifactName ?: "null (not present in path)"),
            "expected version" to (exectedVersion ?: "null (not present in path)"),
            "denotesReleaseBuild" to (exectedVersion?.isRelease() == false).let { if (it) "yes" else "no" }
        ).joinToString(" ") { (note, value) -> "\n[$note: $value]" }
            private set

        operator fun component5(): String = testingDetails
        infix fun testingCase(s: String): fixture = also { it.testing = s }
        inline infix fun testingCase(buildObjective: fixture.() -> String): fixture = testingCase(buildObjective())
        infix fun withDetails(hint: String): fixture = also { it.testingDetails = hint }
        inline infix fun withDetails(hint: fixture.() -> String): fixture = withDetails(hint())
    }
}





