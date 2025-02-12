package puzzles

import maths.containsDigit
import maths.cubesUpTo
import maths.digits
import maths.digitsAreMeanOfEitherSide
import maths.geometricMean
import maths.integerPartitions
import maths.isCube
import maths.isMultipleOf
import maths.isPrime
import maths.isSquare
import maths.longDigits
import maths.longProduct
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
    "14A" to digitsAreMeanOfFirstAndLast(),
    *"16A".singleReference("34A") { meanOf(it, 34) },
    "18A" to hasDigitMean(9),
    "20A" to digitsAreMeanOfEitherSide(),
    "22A" to hasDigitMean(7),
    "25A" to digitsAreMeanOfEitherSide(),
    "28A" to hasDigitMean(4),
    "29A" to hasDigitMean(2),
    "30A" to digitsAreMeanOfEitherSide(),
    *"32A".calculationWithReference("15D") { a32, d15 ->
        d15.longDigits().take(3).geometricMean()?.let { mean -> isMultipleOf(mean)(a32) } ?: false
    },
    *"34A".singleReference("16A") { meanOf(it, 16) },
    *"36A".singleReference("6D") { meanOf(it, 3 * it) },
    "38A" to digitsAreMeanOfFirstAndLast(),
    "40A" to digitsAreMeanOfEitherSide(),
    *"41A".isMeanOf("10A", "9A"),
    *"42A".isMeanOf("10A", "41A"),
    "43A" to simpleClue {
        (it * 2).toInt().integerPartitions(2)
            .any { partition -> partition.all { x -> isCube(x.toLong()) && x.digits().size == 3 } }
    },
    "44A" to digitsAreMeanOfEitherSide(),
    *"45A".isMeanOf("28A", "32A"),

    "1D" to digitsAreMeanOfEitherSide(),
    "2D" to digitsAreMeanOfEitherSide(),
    *"3D".singleReference("40A") { it.longDigits().mean()?.let { mean -> mean + 3 } },
    "4D" to digitsAreMeanOfFirstAndLast(),
    "5D" to simpleClue { it.longDigits().geometricMean() == 4L },
    "6D" to emptyClue(), // The mean of 6D and 6D :troll:
    "7D" to simpleClue { it.longDigits().geometricMean() == 3L },
    "12D" to emptyClue(), // The mean of two times 12D and zero :troll:
    "13D" to simpleClue(::isSquare), // The mean of two times a square and zero
    "15D" to simpleClue { it.longDigits().geometricMean() == 2L },
    "17D" to simpleClue { it.longDigits().average() == 6.5 },
    "19D" to digitsAreMeanOfEitherSide(),
    "20D" to digitsAreMeanOfEitherSide(),
    "21D" to digitsAreMeanOfEitherSide(),
    *"23D".singleReference("12D") { meanOf(it, 19) },
    "24D" to hasDigitMean(6),
    "26D" to hasDigitMean(5),
    "27D" to hasDigitMean(3),
    "31D" to simpleClue { it.longDigits().mean()?.let(::isSquare) ?: false },
    *"33D".calculationWithReference("15D") { d33, d15 ->
        d33.longDigits().geometricMean() == d15.longDigits().takeLast(3).longProduct()
    },
    "35D" to simpleClue(containsDigit(0)),
    "36D" to simpleClue { isMultipleOf(4)(it) && isPrime(it / 4) },
    *"37D".singleReference("23D") { meanOf(301 * it, it) },
    "39D" to digitsAreMeanOfEitherSide(),
    "40D" to hasDigitMean(8),
    "43D" to meanOfPrimeAndCube(),
)

private fun hasDigitMean(mean: Long) = simpleClue { it.longDigits().mean() == mean }

private fun meanOfPrimeAndCube() = simpleClue { value ->
    val cubes = cubesUpTo(2 * value).filter { it.digits().size == 2 }
    val diffs = cubes.map { (2 * value) - it }
    diffs.any { diff -> isPrime(diff) && diff.digits().size == 2 }
}

private fun digitsAreMeanOfFirstAndLast() = simpleClue {
    val digits = it.longDigits()
    val mean = meanOf(digits.first(), digits.last())
    digits.drop(1).dropLast(1).all { digit -> digit == mean }
}

val CROSSNUMBER_18 = factoryCrossnumber(grid, clueMap)
