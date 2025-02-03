package puzzles

import maths.canBeWrittenInSomeBaseAs
import maths.containsDigit
import maths.digitSum
import maths.digits
import maths.distinctDivisors
import maths.facesOfAHypercube
import maths.firstNDigits
import maths.fromDigits
import maths.hcf
import maths.integerFactorial
import maths.integersUpTo
import maths.isMultipleOf
import maths.isNotMultipleOf
import maths.isOdd
import maths.isPalindrome
import maths.isProductOfConsecutive
import maths.isSumOfTwoDistinctSquares
import maths.lastNDigits
import maths.sqrtWhole
import maths.squaresOnNByNChessboard
import solver.ClueConstructor
import solver.Crossnumber
import solver.Orientation
import solver.clue.ContextualClue
import solver.clue.clueMap
import solver.clue.emptyClue
import solver.clue.equalToNumberOfClueWithAnswer
import solver.clue.isEqualTo
import solver.clue.isGeometricMeanOf
import solver.clue.isMultipleOf
import solver.clue.plus
import solver.clue.simpleClue
import solver.clue.transformedEqualsRef
import solver.factoryCrossnumber

/**
 * https://chalkdustmagazine.com/regulars/crossnumber/prize-crossnumber-issue-05/
 */
fun main() {
    CROSSNUMBER_5.solve()
}

private val grid = """
    .....#.#...#...
    .#.#.#.#.#.#.#.
    .##..#.#.#.#.#.
    ...#...........
    ##.###.#.###.#.
    .....#.........
    .#.#####.###.#.
    .......#.......
    .#.###.#####.#.
    .........#.....
    .#.###.#.###.##
    ...........#...
    .#.#.#.#.#..##.
    .#.#.#.#.#.#.#.
    ...#...#.#.....
""".trimIndent()

private val isProductOfFourConsecutiveIntegers = isProductOfConsecutive(4, 6, ::integersUpTo)

private val clueMap: Map<String, ClueConstructor> = clueMap(
    "1A" to simpleClue { it == (it * it).lastNDigits(5) },
    "5A" to simpleClue { isProductOfFourConsecutiveIntegers((it * it) - 1) },
    "7A" to simpleClue { it == it.digits().map(::integerFactorial).sum() },
    "9A" to nineAcross(),
    *"10A".isGeometricMeanOf("27D", "6D"),
    "12A" to twelveAcross(),
    *"13A".isGeometricMeanOf("6D", "4D"),
    "14A" to fourteenAcross(),
    *"15A".isGeometricMeanOf("4D", "5D"),
    "17A" to seventeenAcross(),
    "18A" to emptyClue(),
    "20A" to simpleClue { value -> (value..value + 4).map { distinctDivisors(it).size }.toSet().size == 1 },
    "21A" to simpleClue(isMultipleOf(5_318_008)),
    "24A" to simpleClue { isMultipleOf(9)(it) && it.digits().all { digit -> digit % 2 == 0 } },
    "26A" to equalToNumberOfClueWithAnswer(Orientation.ACROSS, 26),
    "28A" to simpleClue(canBeWrittenInSomeBaseAs(1_000_000, 3)),
    "29A" to simpleClue {
        isSumOfTwoDistinctSquares(it) &&
                isSumOfTwoDistinctSquares(it + 1) &&
                isSumOfTwoDistinctSquares(it + 2)
    },
    "30A" to simpleClue { distinctDivisors(it).sum() == 100_000L },

    "1D" to simpleClue { it == (it * it).lastNDigits(4) },
    "2D" to simpleClue { ((it * it).digits() + (it * it * it).digits()).distinct().size == 10 },
    "3D" to emptyClue(), // TODO - This number can be written as the sum of the fourth powers of two rational numbers, but it cannot be written as the sum of the fourth powers of two integers
    *"4D".isGeometricMeanOf("13A", "15A"),
    *"5D".isGeometricMeanOf("15A", "18A"),
    *"6D".isGeometricMeanOf("10A", "13A"),
    *"7D".isMultipleOf("3D"),
    "7D" to simpleClue(isNotMultipleOf(4)) + transformedEqualsRef("27D", ::digitSum),
    "8D" to emptyClue(), // TODO - This number can be made by concatenating two other answers in this crossnumber
    "11D" to isEqualTo(squaresOnNByNChessboard(13_178)),
    *"13D".isMultipleOf("6D"),
    *"13D".isMultipleOf("20A"),
    "16D" to simpleClue { it.firstNDigits(5).digitSum() == it.lastNDigits(3).digitSum() + 1 },
    "19D" to simpleClue { isPalindrome(it * it) },
    "22D" to isEqualTo(facesOfAHypercube(41, 43)),
    "23D" to twentyThreeDown(),
    "25D" to emptyClue(), // TODO - A number of the form n^2+2^n
    "27D" to equalsTotalCountOfDigits(0, 3, 6, 9),
)

val CROSSNUMBER_5 = factoryCrossnumber(grid, clueMap)

/**
 * A number a such that the equation 3x^2+ax+75 has a repeated root
 *
 * Only way to factorise this is (3x + m)(x + n) for some m, n
 * Repeated root => m = 3n.
 * So 3n^2 = 75
 *
 * And the quadratic becomes 3x^2 + 6nx + 75, hence a = 6n
 */
private fun nineAcross() = isEqualTo(6 * sqrtWhole(75 / 3))

/**
 * The 2nd, 4th, 6th, 8th, and 10th digits of this number are the highest common factors of the digits either side of them
 */
private fun twelveAcross() = simpleClue { value ->
    val digitWindows = value.digits().map(Int::toLong).windowed(3).filterIndexed { ix, _ -> isOdd(ix.toLong()) }
    digitWindows.all { (a, b, c) -> hcf(a, c) == b }
}

/**
 * This number contains each of the digits 1 to 9 exactly once.
 * The number formed by the first n digits of this number is divisible by n (for all n)
 */
private val oneToNine = (1..9).toSet()
private fun fourteenAcross() = simpleClue { value ->
    val digits = value.digits()
    if (!isMultipleOf(digits.size.toLong())(value) || digits.toSet() != oneToNine) {
        false
    } else {
        (digits.size - 1 downTo 2).all { n ->
            isMultipleOf(n.toLong())(digits.subList(0, n).fromDigits())
        }
    }
}

/**
 * Each digit of this number (after the first two) is a digit in the product of the previous two digits of this number. This number’s second digit is 9
 */
private fun seventeenAcross() = simpleClue { value ->
    val digits = value.digits()
    digits[1] == 9 && digits.windowed(3).all { (a, b, c) -> containsDigit(c)((a * b).toLong()) }
}

/**
 * Each digit of this number is either a factor or a multiple of the previous digit
 */
private fun twentyThreeDown() = simpleClue { value ->
    val digitWindows = value.digits().map(Int::toLong).windowed(2)
    digitWindows.all { (a, b) -> isMultipleOf(a)(b) || isMultipleOf(b)(a) }
}

/**
 * 27D: The total number of zeros, threes, sixes, and nines that appear in the completed crossnumber
 */
class EqualsTotalCountOfDigits(crossnumber: Crossnumber, digits: List<Int>) : ContextualClue(crossnumber) {
    private val digitSet = digits.toSet()
    private val range = computeRange()

    private fun computeRange(): IntRange {
        val minimum = crossnumber.digitMap.values.count { it.intersect(digitSet) == it.toSet() }
        val maximum = crossnumber.digitMap.values.count { it.intersect(digitSet).isNotEmpty() }
        return minimum..maximum
    }

    override fun check(value: Long) = range.contains(value)
}

private fun equalsTotalCountOfDigits(vararg digits: Int): ClueConstructor =
    { crossnumber -> EqualsTotalCountOfDigits(crossnumber, digits.toList()) }