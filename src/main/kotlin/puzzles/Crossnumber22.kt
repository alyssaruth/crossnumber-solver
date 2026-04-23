package puzzles

import maths.hasDigitRelationship
import maths.hasDigitSum
import maths.isEven
import maths.isMultipleOf
import maths.isPrime
import maths.isSquare
import solver.ClueConstructor
import solver.Crossnumber
import solver.clue.ComputedPossibilitiesClue
import solver.clue.isEqualTo
import solver.clue.simpleClue
import solver.clue.singleReference
import solver.clueMap
import solver.factoryCrossnumber

/**
 * https://chalkdustmagazine.com/regulars/crossnumber/prize-crossnumber-issue-22/
 */
fun main() {
    CROSSNUMBER_22.solve()
}

private val grid = """
    ......#..#...
    .#..#...##.#.
    ###.##...####
    ....#..###...
    .#......#..#.
    ##.#..#...##.
    .####.##...##
    ##....#..###.
    #..#......#..
    ..##.#..#...#
    ...####.##...
    .###....#..##
    ..#..#......#
""".trimIndent()

private val totalCellCount = grid.count { it == '.' }

private val clueMap = clueMap(
    "1A" to simpleClue(isMultipleOf(3L)),
    "5A" to simpleClue(::isPrime),
    "6A" to simpleClue(::isSquare),
    "8A" to timesDigitAppears(2),
    "9A" to simpleClue(isMultipleOf(9)),
    "11A" to simpleClue(isMultipleOf(111)),
    "12A" to simpleClue(isMultipleOf(3)),
    "14A" to timesDigitAppears(5),
    "15A" to simpleClue(::isSquare),
    "17A" to simpleClue(isMultipleOf(111111)),
    "20A" to timesDigitAppears(6),
    "21A" to simpleClue(isMultipleOf(11)),
    "22A" to simpleClue(isEven),
    "25A" to simpleClue(isMultipleOf(111)),
    "26A" to simpleClue(isMultipleOf(9)),
    "28A" to simpleClue(isMultipleOf(4)),
    "30A" to simpleClue(isMultipleOf(5)),
    "31A" to simpleClue(isMultipleOf(111111)),
    "34A" to timesDigitAppears(4),
    "35A" to timesDigitAppears(3),
    "36A" to simpleClue(::isPrime),
    "37A" to simpleClue(isMultipleOf(111)),
    "39A" to simpleClue(isMultipleOf(100)),
    "40A" to simpleClue(isMultipleOf(111)),
    "41A" to simpleClue(isMultipleOf(1111)),
    "43A" to simpleClue { it < 50 },
    *"44A".singleReference("45A") { it - 20 },
    "46A" to simpleClue(isMultipleOf(111111)),

    "1D" to timesDigitAppears(0),
    "2D" to timesDigitAppears(1),
    "3D" to simpleClue { ((it - 2) % 9) == 0L },
    "4D" to simpleClue(isMultipleOf(11)),
    "5D" to simpleClue(hasDigitSum(21)),
    "6D" to timesDigitAppears(8),
    *"7D".isEqualTo("6D"),
    "10D" to simpleClue { ((it - 2) % 9) == 0L },
    "12D" to simpleClue(isMultipleOf(3)),
    "13D" to simpleClue(isMultipleOf(111)),
    "14D" to simpleClue { ((it - 1) % 9) == 0L },
    "15D" to timesDigitAppears(7),
    "16D" to simpleClue(::isSquare),
    "18D" to simpleClue(isMultipleOf(11)),
    "19D" to simpleClue(::isPrime),
    "20D" to hasDigitRelationship { (a, b) -> b == a || b == 2*a },
    "23D" to hasDigitRelationship { (a, b) -> b == a || b == 2*a },
    "26D" to simpleClue(::isSquare),
    "27D" to simpleClue(isMultipleOf(111)),
    "28D" to simpleClue { ((it + 2) % 9) == 0L },
    "29D" to timesDigitAppears(9),
    "30D" to simpleClue(::isSquare),
    "32D" to simpleClue(isMultipleOf(11)),
    "33D" to simpleClue(isMultipleOf(11)),
    "34D" to simpleClue(isMultipleOf(5)),
    "35D" to simpleClue(isMultipleOf(222)),
    "38D" to simpleClue(isMultipleOf(16)),
    "41D" to simpleClue(::isSquare),
    "42D" to simpleClue(isMultipleOf(11)),
    "43D" to simpleClue { it > 40 }
)

val CROSSNUMBER_22 = factoryCrossnumber(grid, clueMap, skipSymmetryCheck = true, guessThreshold = 100)

private class NumberOfTimesDigitAppears(private val digit: Int, crossnumber: Crossnumber): ComputedPossibilitiesClue(crossnumber) {
    override val possibilities = computePossibilities()

    private fun computePossibilities(): Set<Long> {
        val otherDigits = (0..9).filterNot { it == digit }
        val minimumOtherDigits = otherDigits.sumOf(::minimumCountForDigit)
        val hardMax = totalCellCount - minimumOtherDigits

        val min = minimumCountForDigit(digit).toLong()
        val max = minOf(hardMax, crossnumber.digitMap.values.count { it.contains(digit) })
        return (min..max).toSet()
    }

    private fun minimumCountForDigit(digit: Int) = maxOf(10, crossnumber.digitMap.values.count { it.size == 1 && it.contains(digit) })
}

private fun timesDigitAppears(digit: Int): ClueConstructor = { NumberOfTimesDigitAppears(digit, it) }