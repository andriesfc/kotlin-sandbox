package sandbox.utils

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

class VersionedPathTest : FunSpec({
    context("testing with samples") {
        withData(
            nameFn = SampleVersionPathFixtures.fixture::testing,
            SampleVersionPathFixtures
        ) { (givenPath, expectedBaseName, exectedVersion, clue) ->
            withClue(clue) {
                val p = shouldNotThrowAny { VersionedPath(givenPath) }
                with(p) {
                    path shouldBe givenPath
                    artifactId shouldBe expectedBaseName
                    version shouldBe exectedVersion
                }
                println("${p.artifactId}:${p.version}")
            }
        }
    }
})


