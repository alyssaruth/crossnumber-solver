package puzzles

import maths.geometricMean
import maths.hasDigitRelationship
import maths.isMultipleOf
import maths.isSumOfTwoNthPowers
import maths.longDigits
import maths.mean
import maths.meanOf
import solver.clue.calculationWithReference
import solver.clue.emptyClue
import solver.clue.isMeanOf
import solver.clue.simpleClue
import solver.clue.singleReference
import solver.clueMap
import solver.factoryCrossnumber

/**
 * https://chalkdustmagazine.com/regulars/crossnumber/prize-crossnumber-issue-18/
 */
fun main() {
    CROSSNUMBER_18.solve()
}

private val grid = """
    ...#..........
    .#...#..#.###.
    .#.###..#.....
    .#.#......##.#
    .....##.#..#..
    .##.#....#.##.
    ......##.#....
    ....#.##......
    .##.#....#.##.
    ..#..#.##.....
    #.##......#.#.
    .....#..###.#.
    .###.#..#...#.
    ..........#...
""".trimIndent()

private val clueMap = clueMap(
    *"1A".singleReference("9A") { meanOf(it, it * it) },
    "3A" to digitsAreMeanOfEitherSide(),
    *"8A".singleReference("12D") { meanOf(it, 21) },
    *"9A".isMeanOf("10A", "42A"),
    *"10A".isMeanOf("9A", "41A"),
    *"11A".singleReference("1A") { meanOf(it * 8, it * 900, it) },
    "13A" to simpleClue { it.longDigits().geometricMean() == 6L },
    "14A" to emptyClue(), // TODO - Each digit* of this number is the mean of the first and last digits of this number.
    *"16A".singleReference("34A") { meanOf(it, 34) },
    "18A" to simpleClue { it.longDigits().mean() == 9L },
    "20A" to digitsAreMeanOfEitherSide(),
    "22A" to simpleClue { it.longDigits().mean() == 7L },
    "25A" to digitsAreMeanOfEitherSide(),
    "28A" to simpleClue { it.longDigits().mean() == 4L },
    "29A" to simpleClue { it.longDigits().mean() == 2L },
    "30A" to digitsAreMeanOfEitherSide(),
    *"32A".calculationWithReference("15D") { a32, d15 ->
        d15.longDigits().take(3).geometricMean()?.let { mean -> isMultipleOf(mean)(a32) } ?: false
    },
    *"34A".singleReference("16A") { meanOf(it, 16) },
    *"36A".singleReference("6D") { meanOf(it, 3 * it) },
    "38A" to emptyClue(), // TODO - Each digit* of this number is the mean of the first and last digits of this number
    "40A" to digitsAreMeanOfEitherSide(),
    *"41A".isMeanOf("10A", "9A"),
    *"42A".isMeanOf("10A", "41A"),
    "43A" to simpleClue { isSumOfTwoNthPowers(3)(it * 2) },
    "44A" to digitsAreMeanOfEitherSide(),
    *"45A".isMeanOf("28A", "32A"),

    "1D" to digitsAreMeanOfEitherSide(),
    "2D" to digitsAreMeanOfEitherSide(),
    *"3D".singleReference("40A") { it.longDigits().mean()?.let { mean -> mean + 3 } },
    "4D" to emptyClue(), // TODO - Each digit* of this number is the mean of the first and last digits of this number
)

private fun digitsAreMeanOfEitherSide() = hasDigitRelationship(3) { (a, b, c) -> 2 * b == a + c }

val CROSSNUMBER_18 = factoryCrossnumber(grid, clueMap)
