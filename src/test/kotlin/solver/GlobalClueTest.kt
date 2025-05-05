package solver

import io.kotest.matchers.shouldBe
import maths.digits
import maths.isSquare
import org.junit.jupiter.api.Test
import solver.clue.isGreaterThan
import solver.clue.simpleClue

class GlobalClueTest {
    private val grid = """
        #.#
        ...
        #.#
    """.trimIndent()

    private val rules = clueMap(
        "1D" to simpleClue(::isSquare),
        "2A" to simpleClue(::isSquare),
        *"2A".isGreaterThan("1D")
    )

    private val bannedDigits = listOf(0, 1, 5, 6, 8)

    private val globalClue: GlobalClue = { crossnumber ->
        val solvedClues = crossnumber.partialSolutions().values.filter(ISolution::isSolved).flatMap { it.possibilities }
        solvedClues.none { it.digits().intersect(bannedDigits).isNotEmpty() }
    }

    @Test
    fun `Should be able to apply global rules to eliminate options via contradiction`() {
        val crossnumber = factoryCrossnumber(grid, rules, globalClues = listOf(globalClue))
        val result = crossnumber.solve()
        result.isSolved() shouldBe true
    }
}