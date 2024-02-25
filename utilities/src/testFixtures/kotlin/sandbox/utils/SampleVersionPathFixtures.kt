@file:Suppress("MemberVisibilityCanBePrivate")

package sandbox.utils

object SampleVersionPathFixtures : List<SampleVersionPathFixtures.Fixture> by listOf(
    Fixture(
        givenPath = "libs/compileOnly/",
        expectedBaseName = null,
        exectedVersion = null,
    ) testing {
        "folder paths (ex: [$givenPath]): results in null baseName and version"
    } withTestClue { "$testClue (NOTE: paths ending in \"\")" },
    Fixture(
        givenPath = "libs/compileOnly/javaee-api-5.jar",
        expectedBaseName = "javaee-api",
        exectedVersion = Version(5, 0, 0)
    ),
    Fixture(
        givenPath = "libs/rt/jackson-dataformat-yaml-2.12.7.jar",
        expectedBaseName = "jackson-dataformat-yaml",
        exectedVersion = Version(2, 12, 7)
    ),
    Fixture(
        givenPath = "libs/rt/geronimo-jms_1.1_spec-1.1.1.jar",
        expectedBaseName = "geronimo-jms",
        exectedVersion = Version(1, 1, 0, qualifier = "spec-1.1.1")
    ),
) {
    data class Fixture(
        val givenPath: String,
        val expectedBaseName: String?,
        val exectedVersion: Version?,
    ) {
        var testing: String =
            "given path: $givenPath"
            private set

        var testClue: String = listOf(
            "givenPath" to givenPath,
            "expected base name" to (expectedBaseName ?: "null (not present in path)"),
            "expected version" to (exectedVersion ?: "null (not present in path)")
        ).joinToString(" ") { (note, value) -> "[$note: $value]" }
            private set

        operator fun component4(): String = testClue

        infix fun testing(s: String): Fixture = also { it.testing = s }
        inline infix fun testing(buildObjective: Fixture.() -> String): Fixture = testing(buildObjective())
        infix fun withTestClue(hint: String): Fixture = also { it.testClue = hint }
        inline infix fun withTestClue(hint: Fixture.() -> String): Fixture = withTestClue(hint())
    }
}





