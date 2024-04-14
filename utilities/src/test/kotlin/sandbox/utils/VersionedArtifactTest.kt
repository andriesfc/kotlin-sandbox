package sandbox.utils

import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe

class VersionedArtifactTest : FunSpec({
    context("testing with samples") {
        withData(
            nameFn = SampleVersionedArtifacts.fixture::testing,
            SampleVersionedArtifacts
        ) { (givenPath, expectedBaseName, exectedVersion, expectIsReleaseBuild, summary) ->
            println(summary)
            val actual = shouldNotThrowAny { VersionedArtifact(givenPath) }
            assertSoftly(actual) {
                withClue({ "expecting path [$givenPath]" }) { path shouldBe givenPath }
                withClue({ "expecting artifactId [$expectedBaseName]" }) { artifactId shouldBe expectedBaseName }
                withClue({ "expecting version [$exectedVersion]" }) { version shouldBe exectedVersion }
                withClue({ "expecting ${if (isRelease) "release(true)" else "pre-release(false)"}" }) { isRelease shouldBeEqual expectIsReleaseBuild }
            }
        }
    }
})


