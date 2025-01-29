package puzzles

import maths.digitSum
import maths.digits
import maths.distinctDivisors
import maths.hasWholeNthRoot
import maths.isFibonacci
import maths.isMultipleOf
import maths.isOdd
import maths.isPalindrome
import maths.isPowerOf
import maths.isPrime
import maths.isSquare
import maths.nthRoot
import solver.ClueConstructor
import solver.clue.dualReference
import solver.clue.emptyClue
import solver.clue.isEqualTo
import solver.clue.isMultipleOfRef
import solver.clue.simpleClue
import solver.clue.singleReference
import solver.factoryCrossnumber
import kotlin.math.abs

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
    "21A" to simpleClue { !isPrime(it) }, // TODO - A non-prime number whose highest common factor with 756 is 1.
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
    "45A" to emptyClue(), // TODO - The sum of 6D, 8D, 31D, 37D and 43D

    "1D" to simpleClue(isPowerOf(2)),
    "2D" to simpleClue(::isPalindrome),
    "3D" to simpleClue { hasWholeNthRoot(it, 3) && nthRoot(it, 3) == distinctDivisors(it).size.toLong() },
    "4D" to simpleClue(isOdd),
    "5D" to simpleClue(::isSquare)
)

val CROSSNUMBER_3 = factoryCrossnumber(grid, clueMap)