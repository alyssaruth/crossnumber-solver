package solver

import VALID_GRID
import maths.isPrime
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlin.test.Test

class SolutionTest {
    @Test
    fun `Iterating a pending solution to a partial one, and updating the digit map`() {
        val squares = listOf(Point(0, 0), Point(0, 1))
        val digitMap = mapOf(
            Point(0, 0) to (1..9).toList(),
            Point(0, 1) to listOf(7)
        )

        val solution = PendingSolution(squares, simpleClue(::isPrime), digitMap)

        val (newSolution, newDigitMap) = solution.iterate(ClueId(1, Orientation.ACROSS), dummyCrossnumber(digitMap))
        newSolution.shouldBeInstanceOf<PartialSolution>()
        newSolution.possibilities.shouldContainExactlyInAnyOrder(17, 37, 47, 67, 97)

        newDigitMap shouldBe mapOf(
            Point(0, 0) to listOf(1, 3, 4, 6, 9),
            Point(0, 1) to listOf(7)
        )
    }

    @Test
    fun `A pending solution with too large a search space should remain pending`() {
        val pts = (0..8).map { Point(it, 0) }
        val digitMap = pts.associateWith { (0..9).toList() } + (Point(0, 0) to (1..9).toList())

        val solution = PendingSolution(pts, emptyClue(), digitMap)
        val (newSolution, newMap) = solution.iterate(ClueId(1, Orientation.ACROSS), dummyCrossnumber(digitMap))

        newSolution shouldBe solution
        newMap shouldBe digitMap
    }

    @Test
    fun `A pending solution with a small enough search space should perform okay`() {
        val pts = (0..5).map { Point(it, 0) }
        val digitMap = pts.associateWith { (0..9).toList() } + (Point(0, 0) to (1..9).toList())

        val solution = PendingSolution(pts, emptyClue(), digitMap)
        val (newSolution, newMap) = solution.iterate(ClueId(1, Orientation.ACROSS), dummyCrossnumber(digitMap))

        newSolution.shouldBeInstanceOf<PartialSolution>()
        newSolution.possibilities.size shouldBe 900000
        newMap shouldBe digitMap
    }

    @Test
    fun `Should cope with larger numbers fine provided some digits are restricted`() {
        val pts = (0..10).map { Point(it, 0) }

        val digitMap = mapOf(
            Point(0, 0) to (1..9).toList(), // 9
            Point(1, 0) to listOf(0, 1),          // 18
            Point(2, 0) to listOf(3, 7, 8),       // 54
            Point(3, 0) to listOf(5),             // 54
            Point(4, 0) to (0..9).toList(), // 540
            Point(5, 0) to (0..9).toList(), // 5400
            Point(6, 0) to listOf(8, 9),         // 10,800
            Point(7, 0) to listOf(1, 2, 3, 4, 5), // 54,000
            Point(8, 0) to listOf(3, 7),          // 108,000
            Point(9, 0) to listOf(0, 1, 2, 3),   // 432,000
            Point(10, 0) to listOf(0, 1),        // 864,000
        )

        val solution = PendingSolution(pts, emptyClue(), digitMap)
        val (newSolution) = solution.iterate(ClueId(1, Orientation.ACROSS), dummyCrossnumber(digitMap))
        newSolution.shouldBeInstanceOf<PartialSolution>()
        newSolution.possibilities.size shouldBe 864000
    }

    private fun dummyCrossnumber(digitMap: Map<Point, List<Int>>): Crossnumber =
        Crossnumber(parseGrid(VALID_GRID), digitMap, emptyMap())
}