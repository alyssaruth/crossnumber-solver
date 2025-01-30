package puzzles

import maths.digitSum
import maths.digitToWord
import maths.digits
import maths.fromDigits
import maths.hasDigitSum
import maths.hasUniqueDigits
import maths.isAnagramOf
import maths.isMultipleOf
import maths.isPalindrome
import maths.lcm
import maths.modPow
import maths.nonZeroDigits
import maths.reciprocalSum
import maths.reversed
import maths.sqrtWhole
import solver.ClueConstructor
import solver.clue.calculationWithReference
import solver.clue.dualReference
import solver.clue.emptyClue
import solver.clue.equalsSomeOther
import solver.clue.isFactorOfRef
import solver.clue.isMultipleOfRef
import solver.clue.multiReference
import solver.clue.plus
import solver.clue.simpleClue
import solver.clue.singleReference
import solver.clue.tripleReference
import solver.factoryCrossnumber
import kotlin.math.abs

/**
 * https://chalkdustmagazine.com/regulars/crossnumber/prize-crossnumber-issue-04/
 */
fun main() {
    CROSSNUMBER_4.solve()
}

private val grid = """
    ....#....##....
    .##.#.##.....#.
    ..#......##.##.
    ....#.##.##....
    #.##.##....#.##
    #.##.###.##....
    .........###.#.
    .#.#.#.#.#.#.#.
    .#.###.........
    ....##.###.##.#
    ##.#....##.##.#
    ....##.##.#....
    .##.##......#..
    .#.....##.#.##.
    ....##....#....
""".trimIndent()

private val clueMap: Map<String, ClueConstructor> = mapOf(
    "1A" to isFactorOfRef("1D") + isMultipleOfRef("2D"), // TODO - This number is a multiple of one of the two-digit answers in the crossnumber and shares no factors with the other two-digit answers
    "3A" to tripleReference("15D", "9A", "6D") { a, b, c -> a + b + c },
    "5A" to singleReference("5D") { it - 142 } + singleReference("7D") { it - 808 },
    "8A" to emptyClue(), // TODO - The product of the three largest two-digit answers in this crossnumber
    "9A" to tripleReference(
        "3A",
        "15D",
        "6D"
    ) { a3, d15, d6 -> a3 - d15 - d6 }, // TODO - This number is equal to the number of digits in its factorial
    "11A" to emptyClue(), // TODO - A Sierpiński number
    "12A" to simpleClue { reciprocalSum(it.nonZeroDigits()) == 1.0 } + singleReference("27A") { it.reversed() },
    "13A" to singleReference("7D") { it * 2 },
    "16A" to emptyClue(), // TODO - A counterexample to the conjecture that every odd number can be written in the form p+2a2, where p is 1 or a prime and a is an integer
    "17A" to emptyClue(), // TODO - A second counterexample to the conjecture in 16A
    "19A" to simpleClue(isMultipleOf(717)),
    "23A" to equalsSomeOther("23A"),
    "25A" to simpleClue(::isPalindrome),
    "26A" to calculationWithReference("15D") { value, other -> value < other },
    "27A" to singleReference("12A") { it.reversed() },
    "30A" to simpleClue(hasDigitSum(5)),
    "32A" to simpleClue { it.digits().map(::digitToWord).windowed(2).all { (x, y) -> x.last() == y.first() } },
    "33A" to simpleClue { it == it.digitSum() * 3L },
    "34A" to emptyClue(), // TODO - Integer part of the square root of a Fibonacci number.
    "35A" to multiReference("9A", "27A", "27D", "28D") { it.sum() },
    "36A" to singleReference("29D") { (it * 1.5).toLong() },
    "37A" to calculationWithReference("30A") { value, other -> isMultipleOf(other)(value + 1) },

    "1D" to isMultipleOfRef("1A"),
    "2D" to isFactorOfRef("1A"),
    "3D" to emptyClue(), // TODO - A positive integer whose square is the sum of 50 consecutive squares
    "4D" to simpleClue(hasUniqueDigits(1)),
    "5D" to dualReference("5A", "13A") { x, y -> abs(x - y) / 2 } +
            singleReference("5A") { it + 142 },
    "6D" to singleReference("3A") { it.digits().drop(1).dropLast(1).fromDigits() } +
            tripleReference("3A", "15D", "9A") { a3, d15, a9 -> a3 - d15 - a9 },
    "7D" to singleReference("5A") { it + 808 },
    "10D" to emptyClue(), // TODO - The largest number that is not the sum of two abundant numbers
    "14D" to calculationWithReference("12A") { value, other -> value.modPow(91, 18_793_739) == other },
    "15D" to dualReference("1D", "26A", Long::plus) +
            tripleReference("3A", "9A", "6D") { a3, a9, d6 -> a3 - a9 - d6 },
    "18D" to calculationWithReference("31D") { value, other -> value.isAnagramOf(other) },
    "19D" to calculationWithReference("6D") { value, other -> value.digitSum() + 1L == other },
    "20D" to dualReference("7D", "5A") { a, b -> lcm(a, b) },
    "21D" to equalsSomeOther("21D"),
    "22D" to emptyClue(), // TODO - The smallest number that appears eight times in Pascal’s triangle
    "24D" to emptyClue(), // TODO - The maximum number of regions that can be formed by joining 27 points on a circle with straight lines
    "27D" to emptyClue(), // TODO - The number of straight lines that go through at least two points of a 10×10 grid of points
    "28D" to emptyClue(), // TODO - The sum of four consecutive positive cubes
    "29D" to simpleClue { isPalindrome(it + 5) },
    "30D" to dualReference("18D", "31D", Long::plus),
    "31D" to calculationWithReference("18D") { value, other -> value.isAnagramOf(other) },
    "34D" to simpleClue { it.digitSum().toLong() == sqrtWhole(it) }
)

val CROSSNUMBER_4 = factoryCrossnumber(grid, clueMap)