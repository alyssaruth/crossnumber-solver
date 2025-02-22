package puzzles

import maths.appearsInPascalsTriangle
import maths.countStraightLinesThroughGrid
import maths.cubesUpTo
import maths.digitSum
import maths.digitToWord
import maths.digits
import maths.factorial
import maths.fibonacciUpTo
import maths.hasDigitSum
import maths.hasUniqueDigits
import maths.integerPartitions
import maths.isAbundant
import maths.isCoprimeWith
import maths.isKnownSierpinskiNumber
import maths.isMultipleOf
import maths.isPalindrome
import maths.isSumOfConsecutive
import maths.lcm
import maths.maximumRegionsByJoiningPointsOnACircle
import maths.middleNDigits
import maths.modPow
import maths.nonZeroDigits
import maths.product
import maths.reciprocalSum
import maths.reversed
import maths.sqrtFloor
import maths.sqrtWhole
import maths.squaresUpTo
import maths.violatesGoldbachConjecture
import solver.ClueConstructor
import solver.Crossnumber
import solver.clue.ContextualClue
import solver.clue.MultiReferenceClue
import solver.clue.calculationWithReference
import solver.clue.dualReference
import solver.clue.emptyClue
import solver.clue.equalsSomeOther
import solver.clue.isAnagramOf
import solver.clue.isEqualTo
import solver.clue.isFactorOf
import solver.clue.isHalfTheDifferenceBetween
import solver.clue.isLessThan
import solver.clue.isMultipleOf
import solver.clue.isNTimes
import solver.clue.isNotEqualTo
import solver.clue.isSumOf
import solver.clue.largest
import solver.clue.simpleClue
import solver.clue.singleReference
import solver.clue.smallest
import solver.clue.transformedEqualsRef
import solver.clueMap
import solver.factoryCrossnumber

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

private val clueMap: Map<String, ClueConstructor> = clueMap(
    "1A" to ::OneAcross,
    *"3A".isSumOf("15D", "9A", "6D"),
    *"5A".singleReference("5D") { it - 142 },
    "8A" to ::EightAcross,
    "9A" to simpleClue { factorial(it).digits().size.toLong() == it },
    "11A" to simpleClue(::isKnownSierpinskiNumber),
    "12A" to simpleClue { reciprocalSum(it.nonZeroDigits()) == 1.0 },
    *"13A".isNTimes(2, "7D") ,
    "16A" to simpleClue(::violatesGoldbachConjecture),
    "17A" to simpleClue(::violatesGoldbachConjecture),
    *"17A".isNotEqualTo("16A"),
    "19A" to simpleClue(isMultipleOf(717)),
    "23A".equalsSomeOther(),
    "25A" to simpleClue(::isPalindrome),
    *"26A".isLessThan("15D"),
    *"27A".singleReference("12A") { it.reversed() },
    "30A" to simpleClue(hasDigitSum(5)),
    "32A" to simpleClue { it.digits().map(::digitToWord).windowed(2).all { (x, y) -> x.last() == y.first() } },
    "33A" to simpleClue { it == it.digitSum() * 3L },
    "34A" to simpleClue { value -> fibonacciUpTo((value + 1) * (value + 1)).map(::sqrtFloor).contains(value) },
    *"35A".isSumOf("9A", "27A", "27D", "28D"),
    *"36A".singleReference("29D") { (it * 1.5).toLong() },
    *"37A".calculationWithReference("30A") { value, other -> isMultipleOf(other)(value + 1) },

    *"1D".isMultipleOf("1A"),
    *"2D".isFactorOf("1A"),
    "3D" to simpleClue { isSumOfFiftyConsecutiveSquares(it * it) },
    "4D" to simpleClue(hasUniqueDigits(1)),
    *"5D".isHalfTheDifferenceBetween("5A", "13A"),
    *"6D".singleReference("3A") { middleNDigits(2, it) },
    *"7D".singleReference("5A") { it + 808 },
    "10D" to largest(simpleClue {
        it.toInt().integerPartitions(ofLength = 2).none { partition -> partition.all(abundantNumbers::contains) }
    }),
    "14D" to transformedEqualsRef("12A") { it.modPow(91, 18_793_739) },
    *"15D".isSumOf("1D", "26A"),
    *"18D".isAnagramOf("31D"),
    "19D" to transformedEqualsRef("6D") { it.digitSum() + 1L },
    "20D" to dualReference("7D", "5A") { a, b -> lcm(a, b) },
    "21D".equalsSomeOther(),
    "22D" to smallest(simpleClue(appearsInPascalsTriangle(times = 8))),
    "24D" to isEqualTo(maximumRegionsByJoiningPointsOnACircle(27)),
    "27D" to isEqualTo(countStraightLinesThroughGrid(10)),
    "28D" to simpleClue(isSumOfConsecutive(4, digits = 4, ::cubesUpTo)),
    "29D" to simpleClue { isPalindrome(it + 5) },
    *"30D".isSumOf("18D", "31D"),
    "31D" to emptyClue(), // Covered by 18D
    "34D" to simpleClue { it.digitSum().toLong() == sqrtWhole(it) }
)

val CROSSNUMBER_4 = factoryCrossnumber(grid, clueMap)

/**
 * This number is a multiple of one of the two-digit answers in the crossnumber and shares no factors with the other two-digit answers
 */
class OneAcross(crossnumber: Crossnumber) : ContextualClue(crossnumber) {
    private val twoDigitAnswers = getTwoDigitAnswers()

    override fun check(value: Long) = if (twoDigitAnswers == null) true else {
        val divisors = twoDigitAnswers.filter { divisor -> isMultipleOf(divisor)(value) }
        val others = twoDigitAnswers - divisors

        divisors.size == 1 && others.all(isCoprimeWith(value))
    }

    private fun getTwoDigitAnswers(): List<Long>? {
        val answers = crossnumber.solutionsOfLength(2).keys.map(::lookupAnswer)
        if (answers.any { it == null }) {
            return null
        }

        return answers.filterNotNull()
    }
}

/**
 * The product of the three largest two-digit answers in this crossnumber
 */
class EightAcross(crossnumber: Crossnumber) : MultiReferenceClue(
    crossnumber,
    crossnumber.solutionsOfLength(2).keys.toList(),
    { it.sorted().takeLast(3).map(Long::toInt).product() }
)