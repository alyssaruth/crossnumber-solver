package puzzles

import maths.digitCounts
import maths.digitSum
import maths.digits
import maths.digitsAreStrictlyIncreasing
import maths.distinctDivisors
import maths.hasWholeNthRoot
import maths.hcf
import maths.isFibonacci
import maths.isMultipleOf
import maths.isOdd
import maths.isPalindrome
import maths.isPowerOf
import maths.isPrime
import maths.isSquare
import maths.nthRoot
import maths.toBinary
import solver.ClueConstructor
import solver.clue.calculationWithReference
import solver.clue.dualReference
import solver.clue.emptyClue
import solver.clue.isEqualTo
import solver.clue.isMultipleOfRef
import solver.clue.multiReference
import solver.clue.smallest
import solver.clue.plus
import solver.clue.simpleClue
import solver.clue.singleReference
import solver.factoryCrossnumber
import kotlin.math.abs

/**
 * https://chalkdustmagazine.com/regulars/crossnumber/prize-crossnumber-issue-03/
 */
fun main() {
    CROSSNUMBER_3.solve()
}

private val grid = """
    .......#.......
    .#.#.#...#####.
    .#.#.##........
    .#....#...#.##.
    .#.#.#...#.....
    .#..###.###.##.
    .....#...#.##..
    #......#......#
    ..##.#...#.....
    .##.###.###..#.
    .....#...#.#.#.
    .##.#...#....#.
    ........##.#.#.
    .#####...#.#.#.
    .......#.......
""".trimIndent()

private val clueMap: Map<String, ClueConstructor> = mapOf(
    "1A" to simpleClue(isMultipleOf(999)),
    "5A" to dualReference("45A", "1A") { a, b -> abs(a - b) / 2 },
    "7A" to emptyClue(),
    "9A" to isMultipleOfRef("41A"),
    "12A" to dualReference("4D", "43D", Long::times),
    "13A" to dualReference("7A", "43D", Long::minus),
    "14A" to dualReference("21D", "37A", Long::minus),
    "15A" to emptyClue(), // TODO - A number whose name includes all five vowels exactly once.
    "16A" to singleReference("8D", ::digitSum),
    "18A" to simpleClue { it.digits().sorted() == listOf(0, 2, 4, 6, 8) },
    "21A" to simpleClue { hcf(it, 756) == 1L && !isPrime(it) },
    "24A" to singleReference("29D", ::digitSum),
    "25A" to emptyClue(), // TODO - The product of four consecutive Fibonacci numbers
    "26A" to dualReference("14A", "21D", Long::times),
    "29A" to emptyClue(), // TODO - The largest known n such that all the digits of 2^n are not zero
    "30A" to isEqualTo(418), // I am a teapot
    "32A" to simpleClue(::isPrime), // TODO - A prime number that is the sum of 25 consecutive prime numbers.
    "35A" to emptyClue(), // TODO - The number of different nets of a cube (with reflections and rotations being considered as the same net)
    "36A" to simpleClue(::isFibonacci),
    "37A" to emptyClue(), // TODO - When written in a base other than 10, this number is 256
    "39A" to isEqualTo(789), // Why is 6 afraid of 7?
    "40A" to emptyClue(), // TODO - The number of ways to play the first 3 moves (2 white moves, 1 black move) in a game of chess.
    "41A" to simpleClue(isMultipleOf(719)),
    "42A" to simpleClue { isPrime(it) && isPrime(it + 2) },
    "44A" to singleReference("29D") { it / 2 },
    "45A" to multiReference("6D", "8D", "31D", "37D", "43D") { it.sum() },

    "1D" to simpleClue(isPowerOf(2)),
    "2D" to simpleClue(::isPalindrome),
    "3D" to simpleClue { hasWholeNthRoot(3)(it) && nthRoot(it, 3) == distinctDivisors(it).size.toLong() },
    "4D" to simpleClue(isOdd),
    "5D" to simpleClue(::isSquare),
    "6D" to dualReference("5D", "27D") { d5, d27 -> d5 * (d27 - 1) } + singleReference("33D") { it + 1000006 },
    "8D" to simpleClue(isOdd) + calculationWithReference("16A") { value, other -> value.digitSum().toLong() == other },
    "10D" to simpleClue(isMultipleOf(7)),
    "11D" to emptyClue(), // TODO - The number of pairs of twin primes less than 1,000,000
    "17D" to singleReference("26A") { distinctDivisors(it).size.toLong() },
    "19D" to calculationWithReference("30A") { value, other -> value > other },
    "20D" to simpleClue { it.digitsAreStrictlyIncreasing() && it.digits().all { digit -> isPrime(digit.toLong()) } },
    "21D" to dualReference("14A", "37A", Long::plus),
    "22D" to simpleClue(isMultipleOf(27)),
    "23D" to emptyClue(), // TODO - A number n such that (n−1)!+1 is divisible by n^2
    "24D" to emptyClue(), // TODO - The smallest number that cannot be changed into a prime by changing one digit.
    "27D" to dualReference("5D", "6D") { d5, d6 -> (d6 / d5) + 1 },
    "28D" to isMultipleOfRef("34D"),
    "29D" to simpleClue(isMultipleOf(7)),
    "31D" to simpleClue(::isPrime) + simpleClue { it.digitCounts().values.toList() == listOf(2, 2, 2) },
    "33D" to singleReference("6D") { it - 1000006 },
    "34D" to smallest(simpleClue { !it.toBinary().isPalindrome() && (it * it).toBinary().isPalindrome() }),
    "37D" to simpleClue { (it * it).digitCounts().keys.sorted() == listOf(1, 2, 3, 4) },
    "38D" to simpleClue(hasWholeNthRoot(3)),
    "39D" to emptyClue(), // TODO - The largest number that cannot be written as the sum of positive, distinct integers, the sum of whose reciprocals is 1
    "43D" to smallest(simpleClue { it == 2L * it.digitSum() })
)

val CROSSNUMBER_3 = factoryCrossnumber(grid, clueMap)