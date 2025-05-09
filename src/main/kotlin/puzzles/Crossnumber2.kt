package puzzles

import maths.countPrimesUpTo
import maths.dayOfWeek
import maths.degreesToFahrenheit
import maths.digitSum
import maths.digits
import maths.digitsSameExceptOne
import maths.distinctDivisors
import maths.firstNDigits
import maths.hasDigitSum
import maths.hasMultiplicativePersistence
import maths.hasUniqueDigits
import maths.hasWholeNthRoot
import maths.inPence
import maths.isAbundant
import maths.isEven
import maths.isMultipleOf
import maths.isPalindrome
import maths.isPowerOf
import maths.isPrime
import maths.isSquare
import maths.isTetrahedralNumber
import maths.isTriangleNumber
import maths.lastNDigits
import maths.modPow
import maths.nGonIsConstructible
import maths.nextPrime
import maths.pow
import maths.properFactors
import maths.reversed
import maths.sorted
import maths.toRomanNumerals
import solver.ClueConstructor
import solver.clue.asyncEquals
import solver.clue.dualReference
import solver.clue.emptyClue
import solver.clue.isEqualTo
import solver.clue.isMultipleOf
import solver.clue.isProductOf
import solver.clue.isSumOf
import solver.clue.makeSingleReference
import solver.clue.multiReference
import solver.clue.plus
import solver.clue.simpleClue
import solver.clue.simplyNot
import solver.clue.singleReference
import solver.clue.smallest
import solver.clue.transformedEqualsRef
import solver.clueMap
import solver.digitReducer.DigitReducerConstructor
import solver.digitReducer.digitIndices
import solver.digitReducer.simpleReducer
import solver.factoryCrossnumber
import java.util.Calendar

/**
 * https://chalkdustmagazine.com/regulars/crossnumber/100-prize-crossnumber-issue-02/
 */
fun main() {
    CROSSNUMBER_2.solve()
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

private val a16 = (10..99).first { inPence(it).size == 5 }

private val digitReducers: List<DigitReducerConstructor> = listOf(
    "4D".simpleReducer(digitIndices(1, 3, 5, 7, 9, 11, 12, 13)) { it > 0 }
)

private val clueMap: Map<String, ClueConstructor> = clueMap(
    *"1A".isMultipleOf("24A"),
    "5A" to simpleClue(::nGonIsConstructible),
    "7A" to simpleClue { hasWholeNthRoot(4)(it) && distinctDivisors(it).size.pow(4) == it },
    "9A" to simpleClue { properFactors(it).size == 9 },
    *"11A".singleReference("4D") { it.firstNDigits(4) },
    "12A" to simpleClue(::isPrime),
    *"13A".isProductOf("30D", "12A"),
    "16A" to isEqualTo(a16.toLong()),
    "17A" to simpleClue { isTriangleNumber(it + 2) },
    "19A" to isEqualTo(a19Prime - 370262),
    "21A" to simpleClue(::isPrime),
    "22A" to smallest(simpleClue(hasMultiplicativePersistence(11))),
    "24A" to simpleClue { 3L.modPow(it, it) == 24L },
    *"25A".isMultipleOf("27D"),
    "25A" to simpleClue { toRomanNumerals(it).sorted() == "CDL" },
    "26A" to simpleClue { it >= 1582 && dayOfWeek("$it-01-01") == Calendar.WEDNESDAY },
    "28A" to simpleClue(isMultipleOf(9)) + dualReference("15D", "5D") { d15, d5 -> d15 / d5.reversed() },
    "29A" to simpleClue(hasUniqueDigits(1)),
    "31A" to simpleClue(::isSquare),
    *"33A".singleReference("4D") { it.lastNDigits(4) },
    "35A" to isEqualTo(12), // https://en.wikipedia.org/wiki/Mathematical_chess_problem#Domination_problems
    "36A" to asyncEquals { countPrimesUpTo(100_000_000).toLong() },
    "39A" to simpleClue(::isSquare) + simpleClue(::isTetrahedralNumber),
    "40A" to simpleClue(isEven) + simpleClue { 2L.modPow(it, it) == 2L },

    *"1D".singleReference("32D") { other -> properFactors(other).sum() },
    "2D" to simpleClue(hasDigitSum(8)) + makeSingleReference("5D") { it.digitSum().toLong() },
    *"3D".isSumOf("34D", "12A"),
    "4D" to simpleClue(::fourDown),
    "5D" to transformedEqualsRef("2D", ::digitSum),
    *"6D".isSumOf("32D", "35A", "1A"),
    "8D" to simpleClue(::isPrime),
    "10D" to asyncEquals { tenDown().size.toLong() },
    "11D" to simpleClue(::isPalindrome),
    "14D" to simpleClue { value -> (2 * value).reversed() == value + 2 },
    "15D" to dualReference("28A", "5D") { a28, d5 -> a28 * d5.reversed() },
    "18D" to simpleClue(isPowerOf(3)),
    "19D" to simpleClue(::isAbundant),
    "20D" to isEqualTo(degreesToFahrenheit(100) - degreesToFahrenheit(0)),
    "21D" to digitsSameExceptOne(6),
    "23D" to multiReference("15D", "17A", "34D") { (d15, a17, d34) -> d15 + a17 - d34 },
    "26D" to simpleClue(hasDigitSum(3)),
    "27D" to emptyClue(),
    "30D" to simplyNot(::isPalindrome),
    *"32D".singleReference("1D") { other -> properFactors(other).sum() },
    "34D" to simpleClue(::isSquare),
    *"37D".isProductOf("27D", "38D"),
    "38D" to simpleClue(isMultipleOf(10))
)

val CROSSNUMBER_2 = factoryCrossnumber(grid, clueMap, digitReducers)

/**
 * The 2nd, 4th, 6th, 8th, 10th, 12th and 14th digits of this number are each larger than the digits either side of them. (15)
 * Correction: The 13th digit is actually larger than the 14th.
 */
private fun fourDown(value: Long): Boolean {
    val digits = value.digits()
    val digitIndices = (1..11 step 2)

    return digitIndices.all { digits[it] > digits[it - 1] && digits[it] > digits[it + 1] } && digits[13] > digits[14] && digits[13] < digits[12]
}

/**
 * The number of sequences of 16 (strictly) positive numbers such that:
 *  - each number is one more, one less or the same as the previous number
 *  - the first and last numbers are either 1 or 2
 */
private tailrec fun tenDown(currentChains: List<List<Int>> = listOf(listOf(1), listOf(2))): List<List<Int>> {
    val currentSize = currentChains.first().size
    if (currentSize == 16) {
        return currentChains
    } else {
        val newChains = currentChains.flatMap { chain ->
            val currentEnd = chain.last()
            val nexts =
                listOf(currentEnd - 1, currentEnd, currentEnd + 1).filter { it > 0 && it < (16 - currentSize + 2) }
            nexts.map { chain + it }
        }
        return tenDown(newChains)
    }
}