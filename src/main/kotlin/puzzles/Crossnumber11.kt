package puzzles

import maths.digitCounts
import maths.digitProduct
import maths.digitSum
import maths.firstNDigits
import maths.hasDigitRelationship
import maths.isMultipleOf
import maths.isPrime
import maths.isSquare
import maths.lastNDigits
import maths.longDigits
import maths.properFactors
import maths.reversed
import maths.sumOfCubesOfDigits
import maths.sumOfNthPowerOfDigits
import solver.clue.calculationWithReference
import solver.clue.emptyClue
import solver.clue.isDifferenceBetween
import solver.clue.isEqualTo
import solver.clue.isProductOf
import solver.clue.simpleClue
import solver.clue.singleReference
import solver.clueMap
import solver.factoryCrossnumber

/**
 * https://chalkdustmagazine.com/regulars/crossnumber/prize-crossnumber-issue-11/
 */
fun main() {
    CROSSNUMBER_11.solve()
}

private val grid = """
    ######..........
    #......####.#.#.
    ###.#.#..#..#.#.
    #...#.#########.
    ###.#.#.#.#...#.
    #.#.......#####.
    #...##.####...##
    #.#..#..#.##.#.#
    #.#.##.#..#..#.#
    ##...####.##...#
    .#####.......#.#
    .#...#.#.#.#.###
    .#########.#...#
    .#.#..#..#.#.###
    .#.#.####......#
    ..........######
""".trimIndent()

private val clueMap = clueMap(
    "1A" to emptyClue(),
    "5A" to simpleClue { isPrime(digitProduct(it)) },
    "8A" to simpleClue(isMultipleOf(10)),
    *"8A".isDifferenceBetween("29D", "12D"),
    *"9A".isEqualTo("1D"),
    "10A" to simpleClue { it == sumOfCubesOfDigits(it) },
    *"13A".isDifferenceBetween("18A", "26A"),
    "15A" to simpleClue { it.longDigits().all { digit -> isMultipleOf(digit)(it) } },
    *"17A".calculationWithReference("20A") { a17, a20 -> properFactors(a17).map { it * a20 }.contains(a17) },
    "18A" to hasDigitRelationship { (a, b) -> b == a - 1 },
    *"20A".singleReference("6D", ::digitSum),
    *"21A".calculationWithReference("10A") { value, other -> value.lastNDigits(1) == other.lastNDigits(1) },
    *"21A".calculationWithReference("24A") { value, other -> value.firstNDigits(1) == other.firstNDigits(1) },
    *"24A".calculationWithReference("33A") { value, other -> value.lastNDigits(1) == other.lastNDigits(1) },
    *"25A".singleReference("19D", ::digitSum),
    "26A" to hasDigitRelationship { (a, b) -> b == a - 1 },
    *"27A".calculationWithReference("25A") { a27, a25 -> properFactors(a27).map { it * a25 }.contains(a27) },
    "29A" to simpleClue { it.longDigits().all { digit -> isMultipleOf(digit)(it) } },
    *"32A".isDifferenceBetween("18A", "26A"),
    "33A" to simpleClue { it == sumOfCubesOfDigits(it) },
    *"35A".isEqualTo("37D"),
    "36A" to simpleClue(isMultipleOf(10)),
    *"36A".isDifferenceBetween("29D", "12D"),
    "37A" to simpleClue { isPrime(digitProduct(it)) },
    *"38A".isGreaterThanAndMultipleOfAndReverseOf("1A"),

    "1D" to emptyClue(), // Covered by 9A
    "2D" to simpleClue(::isSquare),
    *"2D".isProductOf("1D", "9A"),
    *"3D".calculationWithReference("35D") { value, other -> isMultipleOf(other)(value - 1) },
    *"4D".isGreaterThanAndMultipleOfAndReverseOf("28D"),
    "6D" to simpleClue { it.digitCounts().all { (key, value) -> key == value } },
    "7D" to simpleClue { it == sumOfNthPowerOfDigits(5)(it) },
    "11D" to simpleClue { !isSquare(it) && isMultipleOf(6)(it) },
    *"11D".singleReference("15A") { it.digitSum() + 3L },
    "12D" to simpleClue(::isSquare),
    *"12D".calculationWithReference("15A") { x, y -> x.firstNDigits(1) == y.firstNDigits(1) },
    *"14D".isGreaterThanAndMultipleOfAndReverseOf("16D"),
    "16D" to emptyClue(),
    "19D" to simpleClue { it.digitCounts().all { (key, value) -> key == value } },
    "22D" to emptyClue(),
    *"23D".isGreaterThanAndMultipleOfAndReverseOf("22D"),
    "28D" to emptyClue(),
    "29D" to simpleClue(::isSquare),
    *"29D".calculationWithReference("29A") { x, y -> x.firstNDigits(1) == y.firstNDigits(1) },
    "30D" to simpleClue { !isSquare(it) && isMultipleOf(6)(it) },
    *"30D".singleReference("29A") { it.digitSum() + 3L },
    "31D" to simpleClue { it == sumOfNthPowerOfDigits(5)(it) },
    *"34D".calculationWithReference("2D") { value, other -> isMultipleOf(other)(value - 1) },
    "35D" to simpleClue(::isSquare),
    *"35D".isProductOf("37D", "35A"),
    *"37D".isEqualTo("35A")
)

val CROSSNUMBER_11 = factoryCrossnumber(grid, clueMap)

private fun String.isGreaterThanAndMultipleOfAndReverseOf(otherClue: String) =
    this.calculationWithReference(otherClue) { value, other ->
        value > other && isMultipleOf(other)(value) && value == other.reversed()
    } + arrayOf(this to simpleClue { it > it.reversed() && isMultipleOf(it.reversed())(it) },
        otherClue to simpleClue { it < it.reversed() && isMultipleOf(it)(it.reversed()) })
