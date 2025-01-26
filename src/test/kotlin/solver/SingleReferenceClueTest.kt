package solver

import io.kotest.matchers.ints.shouldBeLessThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import maths.digits
import maths.isEqualTo
import kotlin.test.Test

class SingleReferenceClueTest {
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
        val clueMap = mapOf(
            "1A" to emptyClue(),
            "5A" to simpleReference("1A") { value, other -> value == other },

            "3D" to simpleClue(isEqualTo(111)),
            "4D" to simpleClue(isEqualTo(111))
        )

        val crossnumber = factoryCrossnumber(grid, clueMap)
        val result = crossnumber.solve().solutions.getValue(ClueId(5, Orientation.ACROSS))
        result.shouldBeInstanceOf<PartialSolution>()
        result.possibilities.size shouldBe 100000
    }

    @Test
    fun `Should check against possible values of referenced clue`() {
        val clueMap = mapOf(
            "1A" to simpleClue { it.digits().distinct().size == 2 },
            "5A" to simpleReference("1A") { value, other -> value < other },

            "1D" to simpleClue(isEqualTo(111)),
            "2D" to simpleClue(isEqualTo(222)),
            "3D" to simpleClue(isEqualTo(111)),
            "4D" to simpleClue(isEqualTo(111))
        )

        val crossnumber = factoryCrossnumber(grid, clueMap)
        val result = crossnumber.solve().solutions.getValue(ClueId(5, Orientation.ACROSS))
        result.shouldBeInstanceOf<PartialSolution>()
        result.possibilities.size shouldBeLessThan 100000
    }
}