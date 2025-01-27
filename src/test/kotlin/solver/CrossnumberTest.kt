package solver

import VALID_GRID
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import maths.isPrime
import org.junit.jupiter.api.Test
import solver.clue.simpleClue

class CrossnumberTest {
    @Test
    fun `Should validate clue IDs`() {
        val clues = listOf("2A", "5A").associateWith {
            simpleClue(::isPrime)
        }

        val ex = shouldThrow<IllegalArgumentException> { factoryCrossnumber(VALID_GRID, clues) }
        ex.message shouldBe "Invalid clue ID(s): [2A]"
    }
}