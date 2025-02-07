package solver.clue

import io.kotest.matchers.ints.shouldBeLessThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import maths.digits
import solver.ClueId
import solver.Orientation
import solver.PartialSolution
import solver.clueMap
import solver.factoryCrossnumber
import kotlin.test.Test

class CalculationWithReferenceClueTest {
    private val grid = """
        #########
        #.......#
        #.#####.#
        #.#####.#
        #########
        #.#####.#
        #.#####.#
        #.......#
        #########
    """.trimIndent()

    @Test
    fun `Should do no filtering if referenced clue is pending`() {
        val clueMap = clueMap(
            "1A" to emptyClue(),
            *"5A".calculationWithReference("1A") { value, other -> value == other },

            "3D" to isEqualTo(111),
            "4D" to isEqualTo(111)
        )

        val crossnumber = factoryCrossnumber(grid, clueMap)
        val result = crossnumber.solve().solutions.getValue(ClueId(5, Orientation.ACROSS))
        result.shouldBeInstanceOf<PartialSolution>()
        result.possibilities.size shouldBe 100000
    }

    @Test
    fun `Should check against possible values of referenced clue`() {
        val clueMap = clueMap(
            "1A" to simpleClue { it.digits().distinct().size == 2 },
            *"5A".lessThan("1A"),

            "1D" to isEqualTo(111),
            "2D" to isEqualTo(222),
            "3D" to isEqualTo(111),
            "4D" to isEqualTo(111)
        )

        val crossnumber = factoryCrossnumber(grid, clueMap)
        val result = crossnumber.solve().solutions.getValue(ClueId(5, Orientation.ACROSS))
        result.shouldBeInstanceOf<PartialSolution>()
        result.possibilities.size shouldBeLessThan 100000
    }
}