package puzzles

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class PuzzleTests {
    @Test
    fun `Crossnumber 1`() {
        CROSSNUMBER_1.solve().sumAcrossClues() shouldBe 13895815379
    }

    @Test
    fun `Crossnumber 2`() {
        CROSSNUMBER_2.solve().sumAcrossClues() shouldBe 277777800764962
    }
}