@file:Suppress("MemberVisibilityCanBePrivate", "ClassName")

package sandbox.utils

object SampleVersionPathFixtures : List<SampleVersionPathFixtures.fixture> by listOf(
    fixture(
        givenPath = "libs/compileOnly/",
        expectedBaseName = null,
        exectedVersion = null,
    ) testingWhen {
        "folder paths (ex: [$givenPath]): results in null baseName and version"
    } withTestClue {
        "$testClue (NOTE: paths ending in \"\")"
    },
    fixture(
        givenPath = "libs/compileOnly/javaee-api-5.jar",
        expectedBaseName = "javaee-api",
        exectedVersion = Version(5, 0, 0)
    ),
    fixture(
        givenPath = "libs/rt/jackson-dataformat-yaml-2.12.7.jar",
        expectedBaseName = "jackson-dataformat-yaml",
        exectedVersion = Version(2, 12, 7)
    ),
    fixture(
        givenPath = "libs/rt/geronimo-jms_1.1_spec-1.1.1.jar",
        expectedBaseName = "geronimo-jms",
        exectedVersion = Version(
            1, 1, 0,
            qualifier = Version.Qualifer("spec-1.1.1", Version.Qualifer.Prefix.UNDERSCORE)
        )
    ),
    fixture(
        givenPath = "jackson-core-2.12.7.jar",
        expectedBaseName = "jackson-core",
        exectedVersion = Version(2, 12, 7)
    ),
    fixture(
        givenPath = "jackson-core-asl-1.9.12.jar",
        expectedBaseName = "jackson-core-asl",
        exectedVersion = Version(1, 9, 12)
    ),
    fixture(
        givenPath = "/rt/kotest-framework-core",
        expectedBaseName = "kotest-framework-core",
        exectedVersion = null
    ) testingWhen { "sample path *without* version: [$givenPath]" },
    fixture(
        givenPath = "/",
        expectedBaseName = null,
        exectedVersion = null
    ) testingWhen { "sample path is root: [$givenPath]" },
    fixture(
        givenPath = "3.1.0",
        expectedBaseName = null,
        exectedVersion = Version(3, 1, 0)
    ) testingWhen { "sample path is just a version: [$givenPath]" }
) {
    data class fixture(
        val givenPath: String,
        val expectedBaseName: String?,
        val exectedVersion: Version?,
    ) {
        var testing: String =
            "sample path: [$givenPath]"
            private set

        var testClue: String = listOf(
            "givenPath" to givenPath,
            "expected base name" to (expectedBaseName ?: "null (not present in path)"),
            "expected version" to (exectedVersion ?: "null (not present in path)")
        ).joinToString(" ") { (note, value) -> "[$note: $value]" }
            private set

        operator fun component4(): String = testClue
        infix fun testingWhen(s: String): fixture = also { it.testing = s }
        inline infix fun testingWhen(buildObjective: fixture.() -> String): fixture = testingWhen(buildObjective())
        infix fun withTestClue(hint: String): fixture = also { it.testClue = hint }
        inline infix fun withTestClue(hint: fixture.() -> String): fixture = withTestClue(hint())
    }
}





