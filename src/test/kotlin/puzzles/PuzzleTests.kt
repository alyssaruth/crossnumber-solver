package puzzles

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class PuzzleTests {
    @Test
    fun `Crossnumber 1`() {
        CROSSNUMBER_1.solve().sumAcrossClues() shouldBe 13895815379
    }
}