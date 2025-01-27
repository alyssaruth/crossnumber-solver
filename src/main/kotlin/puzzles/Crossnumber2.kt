package puzzles

import maths.digitCounts
import maths.digitSum
import maths.digits
import maths.distinctDivisors
import maths.hasDigitSum
import maths.isAbundant
import maths.isEqualTo
import maths.isMultipleOf
import maths.isPalindrome
import maths.isPowerOf
import maths.isPrime
import maths.isSquare
import maths.isTriangleNumber
import maths.nextPrime
import maths.primeFactors
import maths.properFactors
import maths.reversed
import maths.toRomanNumerals
import solver.ClueConstructor
import solver.dualReference
import solver.emptyClue
import solver.factoryCrossnumber
import solver.simpleClue
import solver.simpleReference
import kotlin.math.pow

/**
 * https://chalkdustmagazine.com/regulars/crossnumber/100-prize-crossnumber-issue-02/
 */
fun main() {
    factoryCrossnumber(grid, clueMap).solve()
}

private val grid = """
    ......#.##.....
    .#.##.#.##.###.
    .####.......#..
    .#.#....######.
    ...#.#......##.
    ##.#..#.#.#....
    ##.#.#...#...##
    ...............
    ##...#...#.#.##
    ....#.#.#..#.##
    .##......#.#...
    .######....#.#.
    ..#.......####.
    .###.##.#.##.#.
    .....##.#......
""".trimIndent()

private val a19Prime = nextPrime(370262)

private val clueMap: Map<String, ClueConstructor> = mapOf(
    "1A" to simpleReference("24A") { value, other -> isMultipleOf(other)(value) },
    "5A" to emptyClue(), // TODO - It is possible to construct a regular polygon with this number of sides using only a ruler and compass.
    "7A" to simpleClue { distinctDivisors(it).size.toDouble().pow(4.0).toLong() == it },
    "9A" to simpleClue { properFactors(it).size == 9 },
    "11A" to simpleReference("4D") { value, other -> other.toString().substring(0, 4) == value.toString() },
    "12A" to simpleClue(::isPrime),
    "13A" to dualReference("30D", "12A", Long::times),
    "16A" to emptyClue(), // TODO - The least number of pence which cannot be made using less than 5 coins
    "17A" to simpleClue { isTriangleNumber(it - 2) },
    "19A" to simpleClue(isEqualTo(a19Prime - 370262)),
    "21A" to simpleClue(::isPrime),
    "22A" to emptyClue(), // TODO - The smallest number with a (multiplicative) persistence of 11
    "24A" to emptyClue(), // TODO - The lowest number k such that when 3^k is divided by k the remainder is 24
    "25A" to simpleClue { toRomanNumerals(it).toCharArray().sorted().joinToString("") == "CDL" },
    "26A" to emptyClue(), // TODO - A year which began or will begin on a Wednesday
    "28A" to simpleClue(isMultipleOf(9)),
    "29A" to simpleClue { it.digitCounts().size == 1 },
    "31A" to simpleClue(::isSquare),
    "33A" to simpleReference("4D") { value, other ->
        other.toString().reversed().substring(0, 4).reversed() == value.toString()
    },
    "35A" to emptyClue(), // TODO - The minimum number of knights needed so that each square on a chessboard is either occupied or attacked by a knight.
    "36A" to emptyClue(), // TODO - The number of primes less than 100,000,000
    "39A" to simpleClue(::isSquare), // TODO - Also tetrahedral
    "40A" to emptyClue(), // TODO - The smallest even number, n, such that 2^n − 2 is properly divisible by n

    "1D" to simpleReference("32D") { value, other -> value == properFactors(other).sum() },
    "2D" to simpleClue(hasDigitSum(8)),
    "3D" to dualReference("34D", "12A", Long::plus),
    "4D" to simpleClue(::fourDown),
    "5D" to simpleReference("2D") { value, other -> value.digitSum().toLong() == other },
    "6D" to emptyClue(), // TODO - The sum of 32D, 35A and 1A
    "8D" to simpleClue(::isPrime),
    "10D" to emptyClue(), // TODO - The number of sequences of 16 (strictly) positive numbers such that each number is one more, one less or the same as the previous number and the first and last numbers are either 1 or 2
    "11D" to simpleClue(::isPalindrome),
    "14D" to simpleClue { value -> (2 * value).reversed() == value + 2 },
    "15D" to dualReference("28A", "5D") { a28, d5 -> a28 * d5.reversed() },
    "18D" to simpleClue(isPowerOf(3)),
    "19D" to simpleClue(::isAbundant),
    "20D" to emptyClue(), // TODO - The number of degrees Fahrenheit between the boiling and freezing points of water
    "21D" to simpleClue { value -> value.digitCounts().let { it.size == 2 && it.values.contains(1) } },
    "23D" to emptyClue(), // TODO - 15D plus 17A subtract 34D
    "26D" to simpleClue(hasDigitSum(3)),
    "27D" to simpleReference("25A") { value, other -> isMultipleOf(value)(other) },
    "30D" to simpleClue { !isPalindrome(it) },
    "32D" to simpleReference("1D") { value, other -> value == properFactors(other).sum() },
    "34D" to simpleClue(::isSquare),
    "37D" to dualReference("27D", "38D", Long::times),
    "38D" to simpleClue(isMultipleOf(10))
)

/**
 * The 2nd, 4th, 6th, 8th, 10th, 12th and 14th digits of this number are each larger than the digits either side of them. (15)
 * Correction: The 13th digit is actually larger than the 14th.
 */
private fun fourDown(value: Long): Boolean {
    val digits = value.digits()
    val digitIndices = (1..11 step 2)

    return digitIndices.all { digits[it] > digits[it - 1] && digits[it] > digits[it + 1] } && digits[13] > digits[14] && digits[13] < digits[12]
}