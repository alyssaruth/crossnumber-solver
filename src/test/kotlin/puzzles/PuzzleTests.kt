package puzzles

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class PuzzleTests {
    @Test
    fun `Crossnumber 1`() {
        CROSSNUMBER_1.solve().sumAcrossClues() shouldBe 13_895_815_379
    }

    @Test
    fun `Crossnumber 2`() {
        CROSSNUMBER_2.solve().sumAcrossClues() shouldBe 277_777_800_764_962
    }

    @Test
    fun `Crossnumber 3`() {
        CROSSNUMBER_3.solve().sumAcrossClues() shouldBe 60_196_499
    }

    @Test
    fun `Crossnumber 4`() {
        CROSSNUMBER_4.solve().sumAcrossClues() shouldBe 1_435_302_478
    }

    @Test
    fun `Crossnumber 5`() {
        CROSSNUMBER_5.solve().sumAcrossClues() shouldBe 136_462_689_389
    }

    @Test
    fun `Crossnumber 6`() {
        CROSSNUMBER_6.solve().sumAcrossClues() shouldBe 6_323_098
    }

    @Test
    fun `Crossnumber 7`() {
        CROSSNUMBER_7.solve().sumAcrossClues() shouldBe 15_240_052_826_303_185
    }

    @Test
    fun `Crossnumber 8`() {
        CROSSNUMBER_8.solve().sumAcrossClues() shouldBe 43_788_970
    }

    @Test
    fun `Crossnumber 9`() {
        CROSSNUMBER_9.solve().sumAcrossClues() shouldBe 2_222_222_406_182_591
    }

    @Test
    fun `Crossnumber 10`() {
        CROSSNUMBER_10.solve().sumAcrossClues() shouldBe 891_873_821_817_653
    }

    @Test
    fun `Crossnumber 11`() {
        CROSSNUMBER_11.solve().sumAcrossClues() shouldBe 10_994_518_584
    }

    @Test
    fun `Crossnumber 12`() {
        CROSSNUMBER_12.solve().sumAcrossClues() shouldBe 19_404
    }

    @Test
    fun `Crossnumber 13`() {
        CROSSNUMBER_13.solve().sumAcrossClues() shouldBe 13_704
    }

    @Test
    fun `Crossnumber 14`() {
        solveCrossnumber14().sumAcrossClues() shouldBe 7419
    }

    @Test
    fun `Crossnumber 15`() {
        CROSSNUMBER_15.solve().digitsFromRow(7)?.sum() shouldBe 34
    }

    @Test
    fun `Crossnumber 16`() {
        CROSSNUMBER_16.solve().digitsFromRow(11)?.sum() shouldBe 54
    }

    @Test
    fun `Crossnumber 17`() {
        val result = CROSSNUMBER_17.solve()
        result.isSolved() shouldBe false // This one doesn't have a unique solution, it turns out.
        result.digitsFromRow(2)?.sum() shouldBe 29
    }

    @Test
    fun `Crossnumber 18`() {
        CROSSNUMBER_18.solve().digitsFromRow(6)?.average() shouldBe 6.0
    }

    @Test
    fun `Crossnumber 19`() {
        CROSSNUMBER_19.solve().digitsFromRow(1)?.sum() shouldBe 43
    }

    @Test
    fun `Crossnumber 20`() {
        CROSSNUMBER_20.solve().digitsFromRow(3)?.sum() shouldBe 34
    }

    @Test
    fun `Crossnumber 21`() {
        solveCrossnumber21().digitsFromRow(3)?.sum() shouldBe 52
    }

    @Test
    fun `Crossnumber 22`() {
        CROSSNUMBER_22.solve().digitsFromRow(6)?.sum() shouldBe 16
    }
}