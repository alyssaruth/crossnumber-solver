package solver

import CLUE_ID
import VALID_GRID
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import maths.isMultipleOf
import maths.isPrime
import kotlin.test.Test

class PendingSolutionTest {
    @Test
    fun `Should correctly compute starting and next values based on digit restrictions`() {
        val list = listOf(listOf(1, 3, 7), listOf(2), listOf(0, 3, 9))

        val startingValue = PendingSolution.startingValue(list)
        startingValue shouldBe "120"

        val allValues = mutableListOf<String>()
        var currentValue: String? = startingValue
        while (currentValue != null) {
            allValues.add(currentValue)
            currentValue = PendingSolution.nextValue(list, currentValue)
        }

        allValues.map(String::toInt).shouldContainExactly(120, 123, 129, 320, 323, 329, 720, 723, 729)
    }

    @Test
    fun `A pending solution with too large to even begin looping should remain pending`() {
        val clueId = ClueId(1, Orientation.ACROSS)
        val pts = (0..10).map { Point(it, 0) }
        val digitMap = pts.associateWith { (0..9).toList() } + (Point(0, 0) to (1..9).toList())

        val solution = PendingSolution(pts, emptyClue(), digitMap)
        val crossnumber = solution.iterate(clueId, dummyCrossnumber(digitMap))

        crossnumber.solutions.getValue(clueId) shouldBe solution
        crossnumber.digitMap shouldBe digitMap
    }

    @Test
    fun `A pending solution that would generate too many possibilities for the heap should remain pending`() {
        val clueId = ClueId(1, Orientation.ACROSS)
        val pts = (0..7).map { Point(it, 0) }
        val digitMap = pts.associateWith { (0..9).toList() } + (Point(0, 0) to (1..9).toList())

        val solution = PendingSolution(pts, emptyClue(), digitMap)
        val crossnumber = solution.iterate(clueId, dummyCrossnumber(digitMap))

        crossnumber.solutions.getValue(clueId) shouldBe solution
        crossnumber.digitMap shouldBe digitMap
    }

    @Test
    fun `A pending solution with a large loop size but restrictive enough clue should compute possibilities`() {
        val clueId = ClueId(1, Orientation.ACROSS)
        val pts = (0..7).map { Point(it, 0) }
        val digitMap = pts.associateWith { (0..9).toList() } + (Point(0, 0) to (5..9).toList())

        val solution = PendingSolution(pts, simpleClue(isMultipleOf(365)), digitMap)
        val crossnumber = solution.iterate(clueId, dummyCrossnumber(digitMap))
        val updatedSolution = crossnumber.solutions.getValue(clueId)
        updatedSolution.shouldBeInstanceOf<PartialSolution>()
        updatedSolution.possibilities.size shouldBe 136986
    }

    @Test
    fun `Iterating a pending solution to a partial one, and updating the digit map`() {
        val squares = listOf(Point(0, 0), Point(0, 1))
        val digitMap = mapOf(
            Point(0, 0) to (1..9).toList(),
            Point(0, 1) to listOf(7)
        )

        val solution = PendingSolution(squares, simpleClue(::isPrime), digitMap)

        val crossnumber = solution.iterate(CLUE_ID, dummyCrossnumber(digitMap))
        val newSolution = crossnumber.solutions.getValue(CLUE_ID)
        newSolution.shouldBeInstanceOf<PartialSolution>()
        newSolution.possibilities.shouldContainExactlyInAnyOrder(17, 37, 47, 67, 97)

        crossnumber.digitMap shouldBe mapOf(
            Point(0, 0) to listOf(1, 3, 4, 6, 9),
            Point(0, 1) to listOf(7)
        )
    }

    @Test
    fun `A pending solution with a small enough search space should perform okay`() {
        val pts = (0..5).map { Point(it, 0) }
        val digitMap = pts.associateWith { (0..9).toList() } + (Point(0, 0) to (1..9).toList())

        val solution = PendingSolution(pts, emptyClue(), digitMap)
        val crossnumber = solution.iterate(CLUE_ID, dummyCrossnumber(digitMap))
        val newSolution = crossnumber.solutions.getValue(CLUE_ID)

        newSolution.shouldBeInstanceOf<PartialSolution>()
        newSolution.possibilities.size shouldBe 900000
        crossnumber.digitMap shouldBe digitMap
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
        val crossnumber = solution.iterate(CLUE_ID, dummyCrossnumber(digitMap))
        val newSolution = crossnumber.solutions.getValue(CLUE_ID)
        newSolution.shouldBeInstanceOf<PartialSolution>()
        newSolution.possibilities.size shouldBe 864000
    }

    private fun dummyCrossnumber(digitMap: Map<Point, List<Int>>): Crossnumber =
        Crossnumber(parseGrid(VALID_GRID), digitMap, emptyMap())
}