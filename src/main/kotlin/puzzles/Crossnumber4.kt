package puzzles

import maths.appearsInPascalsTriangle
import maths.countStraightLinesThroughGrid
import maths.cubesUpTo
import maths.digitSum
import maths.digitToWord
import maths.digits
import maths.factorial
import maths.fromDigits
import maths.hasDigitSum
import maths.hasUniqueDigits
import maths.hasWholeNthRoot
import maths.integerPartitions
import maths.isAbundant
import maths.isAnagramOf
import maths.isKnownSierpinskiNumber
import maths.isMultipleOf
import maths.isPalindrome
import maths.isSumOfConsecutive
import maths.lcm
import maths.modPow
import maths.nonZeroDigits
import maths.product
import maths.reciprocalSum
import maths.reversed
import maths.sqrtWhole
import maths.squaresUpTo
import maths.violatesGoldbachConjecture
import solver.ClueConstructor
import solver.clue.calculationWithReference
import solver.clue.doesNotEqualRef
import solver.clue.dualReference
import solver.clue.emptyClue
import solver.clue.equalsSomeOther
import solver.clue.isEqualTo
import solver.clue.isFactorOfRef
import solver.clue.isMultipleOfRef
import solver.clue.largest
import solver.clue.multiReference
import solver.clue.plus
import solver.clue.simpleClue
import solver.clue.singleReference
import solver.clue.smallest
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

private val abundantNumbers = (1..99999).filter { isAbundant(it.toLong()) }.toSet()

private val isSumOfFiftyConsecutiveSquares = isSumOfConsecutive(50, 8, ::squaresUpTo)

private val clueMap: Map<String, ClueConstructor> = mapOf(
    "1A" to isFactorOfRef("1D") + isMultipleOfRef("2D"), // TODO - This number is a multiple of one of the two-digit answers in the crossnumber and shares no factors with the other two-digit answers
    "3A" to tripleReference("15D", "9A", "6D") { a, b, c -> a + b + c },
    "5A" to singleReference("5D") { it - 142 } + singleReference("7D") { it - 808 },
    "8A" to multiReference("9A", "33A", "6D", "34D") { it.sorted().drop(1).map(Long::toInt).product() },
    "9A" to simpleClue { factorial(it).digits().size.toLong() == it },
    "11A" to simpleClue(::isKnownSierpinskiNumber),
    "12A" to simpleClue { reciprocalSum(it.nonZeroDigits()) == 1.0 } + singleReference("27A") { it.reversed() },
    "13A" to singleReference("7D") { it * 2 },
    "16A" to simpleClue(::violatesGoldbachConjecture) + doesNotEqualRef("17A"),
    "17A" to simpleClue(::violatesGoldbachConjecture) + doesNotEqualRef("16A"),
    "19A" to simpleClue(isMultipleOf(717)),
    "23A" to equalsSomeOther("23A"),
    "25A" to simpleClue(::isPalindrome),
    "26A" to calculationWithReference("15D") { value, other -> value < other } +
            dualReference("15D", "1D", Long::minus),
    "27A" to singleReference("12A") { it.reversed() },
    "30A" to simpleClue(hasDigitSum(5)),
    "32A" to simpleClue { it.digits().map(::digitToWord).windowed(2).all { (x, y) -> x.last() == y.first() } },
    "33A" to simpleClue { it == it.digitSum() * 3L },
    "34A" to emptyClue(), // TODO - Integer part of the square root of a Fibonacci number.
    "35A" to multiReference("9A", "27A", "27D", "28D") { it.sum() },
    "36A" to singleReference("29D") { (it * 1.5).toLong() },
    "37A" to calculationWithReference("30A") { value, other -> isMultipleOf(other)(value + 1) },

    "1D" to isMultipleOfRef("1A") + dualReference("15D", "26A", Long::minus),
    "2D" to isFactorOfRef("1A"),
    "3D" to simpleClue { isSumOfFiftyConsecutiveSquares(it * it) },
    "4D" to simpleClue(hasUniqueDigits(1)),
    "5D" to dualReference("5A", "13A") { x, y -> abs(x - y) / 2 } +
            singleReference("5A") { it + 142 },
    "6D" to singleReference("3A") { it.digits().drop(1).dropLast(1).fromDigits() } +
            tripleReference("3A", "15D", "9A") { a3, d15, a9 -> a3 - d15 - a9 },
    "7D" to singleReference("5A") { it + 808 },
    "10D" to largest(simpleClue {
        it.toInt().integerPartitions(ofLength = 2).none { partition -> partition.all(abundantNumbers::contains) }
    }),
    "14D" to calculationWithReference("12A") { value, other -> value.modPow(91, 18_793_739) == other },
    "15D" to dualReference("1D", "26A", Long::plus) +
            tripleReference("3A", "9A", "6D") { a3, a9, d6 -> a3 - a9 - d6 },
    "18D" to calculationWithReference("31D") { value, other -> value.isAnagramOf(other) },
    "19D" to calculationWithReference("6D") { value, other -> value.digitSum() + 1L == other },
    "20D" to dualReference("7D", "5A") { a, b -> lcm(a, b) },
    "21D" to equalsSomeOther("21D"),
    "22D" to smallest(simpleClue(appearsInPascalsTriangle(times = 8))),
    "24D" to emptyClue(), // TODO - The maximum number of regions that can be formed by joining 27 points on a circle with straight lines
    "27D" to isEqualTo(countStraightLinesThroughGrid(10)),
    "28D" to simpleClue(isSumOfConsecutive(4, digits = 4, ::cubesUpTo)),
    "29D" to simpleClue { isPalindrome(it + 5) },
    "30D" to dualReference("18D", "31D", Long::plus),
    "31D" to calculationWithReference("18D") { value, other -> value.isAnagramOf(other) },
    "34D" to simpleClue { hasWholeNthRoot(2)(it) && it.digitSum().toLong() == sqrtWhole(it) }
)

val CROSSNUMBER_4 = factoryCrossnumber(grid, clueMap)