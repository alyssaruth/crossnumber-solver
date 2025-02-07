package puzzles

import maths.allCombinations
import maths.bigPow
import maths.canBeWrittenInSomeBaseAs
import maths.digitProduct
import maths.digitSum
import maths.digits
import maths.englishWordsUpTo
import maths.fibonacciUpTo
import maths.hasDigitSum
import maths.hasUniqueDigits
import maths.hasWholeNthRoot
import maths.isEven
import maths.isFibonacci
import maths.isMultipleOf
import maths.isOdd
import maths.isPalindrome
import maths.isPrime
import maths.isSquare
import maths.isTriangleNumber
import maths.longDigits
import maths.nthCakeNumber
import maths.product
import maths.sqrtWhole
import solver.ClueConstructor
import solver.Crossnumber
import solver.Orientation
import solver.RAM_THRESHOLD
import solver.clue.ContextualClue
import solver.clue.calculationWithReference
import solver.clue.emptyClue
import solver.clue.isDifferenceBetween
import solver.clue.isEqualTo
import solver.clue.isFactorOf
import solver.clue.isMeanOf
import solver.clue.isMultipleOf
import solver.clue.isProductOf
import solver.clue.largest
import solver.clue.isNotEqualTo
import solver.clue.plus
import solver.clue.simpleClue
import solver.clue.singleReference
import solver.clueMap
import solver.factoryCrossnumber
import java.math.BigInteger
import kotlin.math.abs

/**
 * https://chalkdustmagazine.com/regulars/crossnumber/prize-crossnumber-issue-06/
 */
fun main() {
    CROSSNUMBER_6.solve()
}

private val grid = """
    #######..#######
    #....#...#.....#
    ##.###.####.#.##
    .#######.#..#..#
    ....#....#....##
    ###..##.....#.#.
    #....#..#.###.#.
    #.#.##.######.#.
    #.#......#..#.#.
    #.#...#.##.####.
    #.#.#####..##...
    ..####.####.####
    .##.##.#.......#
    ..#.#....#...#..
    .#####.###.####.
    .......##.......
""".trimIndent()

private val englishWords = englishWordsUpTo(4)

private val clues = clueMap(
    "1A" to divisibleBySumAndProductOfDigits(),
    "3A" to simpleClue(hasUniqueDigits(4)) + simpleClue(::isSquare),
    *"5A".isProductOf("1A", "5D"),
    *"6A".isDifferenceBetween("22D", "23D"),
    "11A" to simpleClue(::isPrime),
    "12A" to simpleClue(::isSquare),
    "13A" to isEqualTo(nthCakeNumber(36)),
    *"15A".isFactorOf("7D"),
    "17A" to simpleClue { englishWords.contains(it.toString(16)) },
    *"18A".triangleNumberPair("19D"),
    "20A" to simpleClue(::isPalindrome),
    "23A" to simpleClue(hasDigitSum(7)) + simpleClue(isMultipleOf(7)),
    *"24A".isFactorOf("33D"),
    "25A" to largest(simpleClue { (it * it).digits().size == 12 }),
    *"29A".calculationWithReference("22D") { value, other -> isMultipleOf(value)(other + 12) },
    *"30A".isMultipleOf("26D"),
    *"31A".singleReference("29A", ::digitSum),
    *"32A".isProductOf("36D", "12A"),
    "33A" to simpleClue(::isFibonacci),
    "37A" to simpleClue(::isPalindrome) + simpleClue(isMultipleOf(1111)),
    "41A" to simpleClue(isOdd),
    "42A" to simpleClue { it.digits().windowed(2).all { (a, b) -> b == a + 1 } },
    *"43A".triangleNumberPair("29D"),
    *"44A".isMeanOf("45D", "41A"),
    *"46A".triangleNumberPair("47A"),
    "47A" to emptyClue(),

    "1D" to simpleClue { fibonacciUpTo(it, it.longDigits()).contains(it) },
    "2D" to divisibleBySumAndProductOfDigits(),
    *"4D".singleReference("3A", ::sqrtWhole),
    *"5D".singleReference("35D") { it.digits().take(2).product() },
    "7D" to simpleClue { !isPrime(it) },
    "8D" to simpleClue(isMultipleOf(10)) + ::EightDown,
    *"9D".isFactorOf("45D"),
    *"10D".isProductOf("11A", "12A"),
    "11D" to simpleClue { isMultipleOf(4)(it - 3) },
    *"14D".isMultipleOf("45D"),
    *"16D".isProductOf("11A", "36D"),
    "19D" to emptyClue(),
    *"21D".isFactorOf("20A"),
    *"22D".isMultipleOf("36D"),
    *"23D".isMultipleOf("44A"),
    *"23D".isMultipleOf("28D"),
    "24D" to simpleClue { hasWholeNthRoot(4)(((it * it) + 1) / 2) },
    *"26D".isNotEqualTo("44A"),
    "27D" to isEqualTo(((100 * 0.9) * 1.1).toLong()),
    "28D" to isEqualTo(((100 * 1.1) * 0.9).toLong()),
    "29D" to emptyClue(),
    *"33D".isMultipleOf("17A"),
    *"34D".calculationWithReference("15A") { value, other -> isMultipleOf(other + 1)(value) },
    "35D" to simpleClue(isEven),
    *"36D".isMeanOf("11A", "12A", "36D"),
    "37D" to simpleClue {
        it.bigPow(3).digitSum() == it.bigPow(6).digitSum() &&
                it.bigPow(3).digitSum() == it.bigPow(7).digitSum()
    },
    "38D" to canBeWrittenInSomeBaseAs(110001, 4),
    *"39D".singleReference("34D", ::digitProduct),
    "40D" to simpleClue { it == it.digitSum() + digitProduct(it) },
    "45D" to simpleClue(isMultipleOf(5))
)

private fun divisibleBySumAndProductOfDigits() =
    simpleClue { isMultipleOf(it.digitSum().toLong())(it) && isMultipleOf(digitProduct(it))(it) }

/**
 * This number and <other> are a pair of triangle numbers whose sum and difference are also triangle numbers
 */
private fun String.triangleNumberPair(other: String): Array<Pair<String, ClueConstructor>> {
    val calcLambda: (Long, Long) -> Boolean = { x, y -> isTriangleNumber(x + y) && isTriangleNumber(abs(x - y)) }
    return arrayOf(
        this to simpleClue(::isTriangleNumber),
        other to simpleClue(::isTriangleNumber)
    ) + this.calculationWithReference(other, calcLambda)
}

/**
 * Ten times the sum of the across clues in this crossnumber
 */
private class EightDown(crossnumber: Crossnumber) : ContextualClue(crossnumber) {
    private val possibilities = computePossibilities()
    private val range = computeRange()

    override fun check(value: Long) = possibilities?.contains(value) ?: range?.contains(value) ?: true

    private fun computeRange(): LongRange? {
        val possibilities = getAcrossPossibilities() ?: return null

        val min = 10 * possibilities.sumOf { it.min() }
        val max = 10 * possibilities.sumOf { it.max() }
        return min..max
    }

    private fun computePossibilities(): Set<Long>? {
        val possibilities = getAcrossPossibilities() ?: return null
        val size = possibilities.fold(BigInteger.ONE) { size, possibles -> size.times(possibles.size.toBigInteger()) }
        if (size > RAM_THRESHOLD.toBigInteger()) {
            return null
        }

        return possibilities.allCombinations().map { 10 * it.sum() }.toSet()
    }

    private fun getAcrossPossibilities(): List<Set<Long>>? {
        val acrossIds = crossnumber.solutions.keys.filter { it.orientation == Orientation.ACROSS }
        val possibilities = acrossIds.map(::lookupAnswers)
        if (possibilities.any { it == null }) {
            return null
        }

        return possibilities.filterNotNull()
    }
}

val CROSSNUMBER_6 = factoryCrossnumber(grid, clues, skipSymmetryCheck = true)