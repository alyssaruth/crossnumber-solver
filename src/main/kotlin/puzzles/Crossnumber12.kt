package puzzles

import maths.containsDigits
import maths.digitProduct
import maths.digitsAllTheSame
import maths.hasDigitProduct
import maths.hasDigitSum
import maths.hcf
import maths.isEven
import maths.isMultipleOf
import maths.isOdd
import maths.isPalindrome
import maths.isPrime
import maths.isSquare
import maths.longDigits
import maths.reversed
import solver.Clue
import solver.clue.calculationWithReference
import solver.clue.emptyClue
import solver.clue.plus
import solver.clue.simpleClue
import solver.clue.singleReference
import solver.clueMap
import solver.factoryCrossnumber

/**
 * https://chalkdustmagazine.com/regulars/crossnumber/prize-crossnumber-issue-12/
 */
fun main() {
    CROSSNUMBER_12.solve()
}

private val grid = """
    ...#...#...#...
    .#...#...#...#.
    ...#...#...#...
    #.###.###.###.#
    ...#...#...#...
    .#...#...#...#.
    ...#...#...#...
    #.###.###.###.#
    ...#...#...#...
    .#...#...#...#.
    ...#...#...#...
    #.###.###.###.#
    ...#...#...#...
    .#...#...#...#.
    ...#...#...#...
""".trimIndent()

private val clueMap = clueMap(
    *"1A".threeTimes("2D"),
    "3A" to threeTimesA(::isPalindrome),
    *"5A".threeMoreThan("33A"),
    *"7A".threeTimesTheReverseOf("11A"),
    *"9A".threeLessThan("2D"),
    *"10A".singleReference("3A") { it.reversed() },
    *"11A".threeTimesTheReverseOf("18A"),
    "12A" to simpleClue { isPrime(it - 3) },
    "14A" to simpleClue(isOdd),
    "16A" to simpleClue { isMultipleOf(10)(it - 3) },
    *"18A".threeTimesTheReverseOf("8D"),
    *"20A".threeMoreThan("31A"),
    *"22A".threeMoreThan("15D"),
    "24A" to simpleClue { isMultipleOf(110)(it - 3) },
    *"26A".threeTimes("67A"),
    "28A" to digitsAllTheSame(3),
    *"29A".threeLessThan("57D"),
    *"30A".threeTimes("16A"),
    *"31A".threeLessThan("20D"),
    *"33A".threeTimes("57D"),
    "35A" to simpleClue { isMultipleOf(100)(it - 33) },
    *"37A".threeTimes("17D"),
    "39A" to emptyClue(),
    "41A" to threeTimesA(::isPrime),
    *"43A".calculationWithReference("43D") { x, y -> hcf(x, y) == 33L },
    *"45A".threeLessThan("46D"),
    *"47A".calculationWithReference("63D") { x, y -> hcf(x, y) == 33L },
    *"48A".singleReference("3D") { it.reversed() },
    "49A" to digitsAllTheSame(3),
    *"50A".singleReference("39A") { it + 300 },
    "52A" to simpleClue(isMultipleOf(31)) + simpleClue { isPrime(it/31) },
    *"54A".calculationWithReference("44D") { x, y -> hcf(x, y) == 33L },
    "56A" to simpleClue { isSquare(it - 3) },
    "58A" to threeTimesA(::isPrime),
    "60A" to digitsAllTheSame(3),
    "62A" to simpleClue(isOdd),
    "64A" to threeTimesA(::isSquare),
    "66A" to simpleClue(hasDigitProduct(3)),
    "67A" to simpleClue(isMultipleOf(15)),
    "68A" to simpleClue(containsDigits(3, 6, 9)),
    *"69A".singleReference("58D") { 3 * digitProduct(it) },
    "70A" to digitsAllTheSame(3),
    *"71A".threeTimes("63D"),
    *"72A".threeLessThan("64A"),

    *"1D".threeTimes("12A"),
    *"2D".threeMoreThan("9A"),
    "3D" to simpleClue(isOdd),
    *"4D".singleReference("3A") { it.reversed() },
    "5D" to simpleClue(isOdd),
    "6D" to simpleClue { isMultipleOf(10)(it - 3) },
    *"7D".threeLessThan("38D"),
    "8D" to simpleClue(isEven),
    *"13D".singleReference("62D") { it + 31 },
    "15D" to simpleClue(isEven),
    "17D" to simpleClue(hasDigitProduct(3)),
    *"19D".singleReference("7A") { it + 300 },
    *"20D".threeMoreThan("21D"),
    *"21D".threeLessThan("20A"),
    *"22D".threeMoreThan("28A"),
    "23D" to simpleClue(hasDigitSum(17)),
    "24D" to simpleClue { isMultipleOf(112)(it + 3) },
    "25D" to simpleClue { isMultipleOf(122)(it + 33) },
    *"26D".threeTimes("34D"),
    *"27D".threeTimes("6D"),
    *"32D".threeTimes("31A"),
    "34D" to simpleClue { isMultipleOf(4)(it - 3) },
    "36D" to simpleClue(containsDigits(3, 6, 9)),
    "38D" to simpleClue { isPalindrome(it + 3) },
    *"39D".threeMoreThan("39A"),
    *"40D".singleReference("39A") { it + 30 },
    "41D" to simpleClue(isMultipleOf(33)) + simpleClue { isPrime(it/33) },
    "42D" to simpleClue(isMultipleOf(37)) + simpleClue { isPrime(it/37) },
    *"43D".calculationWithReference("54A") { x, y -> hcf(x, y) == 33L },
    *"44D".calculationWithReference("43A") { x, y -> hcf(x, y) == 33L },
    *"45D".threeLessThan("56A"),
    "46D" to simpleClue { isPrime(it - 3) },
    *"51D".threeTimes("39D"),
    "53D" to simpleClue(containsDigits(3, 6, 9)),
    "55D" to digitsAllTheSame(3),
    *"57D".threeMoreThan("29A"),
    "58D" to simpleClue { it.longDigits().all(isOdd) },
    *"59D".singleReference("69A") { 3 * digitProduct(it) },
    "60D" to digitsAllTheSame(3),
    "61D" to digitsAllTheSame(3),
    *"62D".threeMoreThan("62A"),
    "63D" to simpleClue(isMultipleOf(33)),
    "64D" to simpleClue(isMultipleOf(333)) + simpleClue { isSquare(it/333) },
    "65D" to threeTimesA(::isSquare)
)

val CROSSNUMBER_12 = factoryCrossnumber(grid, clueMap)

private fun threeTimesA(checker: Clue) = simpleClue(isMultipleOf(3)) + simpleClue { checker(it/3) }
private fun String.threeTimes(other: String) = singleReference(other) { it * 3 }
private fun String.threeMoreThan(other: String) = singleReference(other) { it + 3 }
private fun String.threeLessThan(other: String) = singleReference(other) { it - 3 }
private fun String.threeTimesTheReverseOf(otherClue: String) =
    singleReference(otherClue) { 3 * it.reversed() }


