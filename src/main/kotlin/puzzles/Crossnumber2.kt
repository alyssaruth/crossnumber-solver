package puzzles

import maths.countPrimesUpTo
import maths.degreesToFahrenheit
import maths.digitCounts
import maths.digitSum
import maths.digits
import maths.distinctDivisors
import maths.hasDigitSum
import maths.hasMultiplicativePersistence
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
import maths.nGonIsConstructible
import maths.nextPrime
import maths.properFactors
import maths.reversed
import maths.toRomanNumerals
import solver.ClueConstructor
import solver.LOOP_THRESHOLD
import solver.clue.dualReference
import solver.clue.emptyClue
import solver.clue.isEqualTo
import solver.clue.minimumOf
import solver.factoryCrossnumber
import solver.clue.plus
import solver.clue.simpleClue
import solver.clue.simpleReference
import solver.clue.singleReferenceEquals
import solver.clue.tripleReference
import kotlin.math.pow

/**
 * https://chalkdustmagazine.com/regulars/crossnumber/100-prize-crossnumber-issue-02/
 */
fun main() {
    println("12345".substring(0, 4))
    val primeThread = Thread { primesUpToOneHundredMillion = countPrimesUpTo(100_000_000).toLong() }
    primeThread.start()
    val attemptOne = factoryCrossnumber(grid, clueMap).solve()
    if (primesUpToOneHundredMillion == -1L) {
        println("Waiting for primes calculation...")
        primeThread.join()
        attemptOne.copy(loopThreshold = LOOP_THRESHOLD).solve()
    }
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

private var primesUpToOneHundredMillion = -1L

private val a16 = (10..99).first { inPence(it).size == 5 }
private val d10 = tenDown().size.toLong()

private val clueMap: Map<String, ClueConstructor> = mapOf(
    "1A" to simpleReference("24A") { value, other -> isMultipleOf(other)(value) },
    "5A" to simpleClue(::nGonIsConstructible),
    "7A" to simpleClue { distinctDivisors(it).size.toDouble().pow(4.0).toLong() == it },
    "9A" to simpleClue { properFactors(it).size == 9 },
    "11A" to simpleReference("4D") { value, other -> other.toString().substring(0, 4) == value.toString() },
    "12A" to simpleClue(::isPrime) + dualReference("3D", "34D", Long::minus),
    "13A" to dualReference("30D", "12A", Long::times),
    "16A" to isEqualTo(a16.toLong()),
    "17A" to simpleClue { isTriangleNumber(it + 2) },
    "19A" to isEqualTo(a19Prime - 370262),
    "21A" to simpleClue(::isPrime),
    "22A" to minimumOf(simpleClue(hasMultiplicativePersistence(11))),
    "24A" to emptyClue(), // TODO - The lowest number k such that when 3^k is divided by k the remainder is 24
    "25A" to simpleClue { toRomanNumerals(it).toCharArray().sorted().joinToString("") == "CDL" },
    "26A" to emptyClue(), // TODO - A year which began or will begin on a Wednesday
    "28A" to simpleClue(isMultipleOf(9)),
    "29A" to simpleClue { it.digitCounts().size == 1 },
    "31A" to simpleClue(::isSquare),
    "33A" to simpleReference("4D") { value, other ->
        other.toString().reversed().substring(0, 4).reversed() == value.toString()
    },
    "35A" to isEqualTo(12), // https://en.wikipedia.org/wiki/Mathematical_chess_problem#Domination_problems
    "36A" to simpleClue { if (primesUpToOneHundredMillion == -1L) true else it == primesUpToOneHundredMillion },
    "39A" to simpleClue(::isSquare) + simpleClue(::isTetrahedralNumber),
    "40A" to simpleClue(isEven), // TODO - The smallest even number, n, such that 2^n − 2 is properly divisible by n

    "1D" to singleReferenceEquals("32D") { other -> properFactors(other).sum() },
    "2D" to simpleClue(hasDigitSum(8)),
    "3D" to dualReference("34D", "12A", Long::plus),
    "4D" to simpleClue(::fourDown),
    "5D" to simpleReference("2D") { value, other -> value.digitSum().toLong() == other },
    "6D" to tripleReference("32D", "35A", "1A") { d32, a35, a1 -> d32 + a35 + a1 },
    "8D" to simpleClue(::isPrime),
    "10D" to isEqualTo(d10),
    "11D" to simpleClue(::isPalindrome),
    "14D" to simpleClue { value -> (2 * value).reversed() == value + 2 },
    "15D" to dualReference("28A", "5D") { a28, d5 -> a28 * d5.reversed() },
    "18D" to simpleClue(isPowerOf(3)),
    "19D" to simpleClue(::isAbundant),
    "20D" to isEqualTo(degreesToFahrenheit(100) - degreesToFahrenheit(0)),
    "21D" to simpleClue { value -> value.digitCounts().let { it.size == 2 && it.values.contains(1) } },
    "23D" to tripleReference("15D", "17A", "34D") { d15, a17, d34 -> d15 + a17 - d34 },
    "26D" to simpleClue(hasDigitSum(3)),
    "27D" to simpleReference("25A") { value, other -> isMultipleOf(value)(other) },
    "30D" to simpleClue { !isPalindrome(it) },
    "32D" to singleReferenceEquals("1D") { other -> properFactors(other).sum() },
    "34D" to simpleClue(::isSquare) + dualReference("3D", "12A", Long::minus),
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

/**
 * The number of sequences of 16 (strictly) positive numbers such that:
 *  - each number is one more, one less or the same as the previous number
 *  - the first and last numbers are either 1 or 2
 */
private tailrec fun tenDown(currentChains: List<List<Int>> = listOf(listOf(1), listOf(2))): List<List<Int>> {
    if (currentChains.first().size == 16) {
        return currentChains.filter { listOf(1, 2).contains(it.last()) }
    } else {
        val newChains = currentChains.flatMap { chain ->
            val currentEnd = chain.last()
            val nexts = listOf(currentEnd - 1, currentEnd, currentEnd + 1).filter { it > 0 }
            nexts.map { chain + it }
        }
        return tenDown(newChains)
    }
}