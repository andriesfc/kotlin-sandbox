package sandbox.utils

import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

class VersionedPathTest : FunSpec({
    context("testing with samples") {
        withData(
            nameFn = SampleVersionPathFixtures.Fixture::testing,
            SampleVersionPathFixtures
        ) { (givenPath, expectedBaseName, exectedVersion, clue) ->
            withClue(clue) {
                assertSoftly(shouldNotThrowAny { VersionedPath(givenPath) }) {
                    path shouldBe givenPath
                    artifactId shouldBe expectedBaseName
                    version shouldBe exectedVersion
                }
            }
        }
    }
})


