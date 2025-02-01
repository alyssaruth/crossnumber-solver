package puzzles

import maths.canBeWrittenInSomeBaseAs
import maths.digitSum
import maths.digits
import maths.distinctDivisors
import maths.doesNotContainDigit
import maths.firstNDigits
import maths.geometricMean
import maths.hasUniqueDigits
import maths.integerFactorial
import maths.integersUpTo
import maths.isMultipleOf
import maths.isNotMultipleOf
import maths.isPalindrome
import maths.isProductOfConsecutive
import maths.isSumOfTwoDistinctSquares
import maths.lastNDigits
import solver.ClueConstructor
import solver.Orientation
import solver.clue.emptyClue
import solver.clue.equalToNumberOfClueWithAnswer
import solver.clue.isFactorOfRef
import solver.clue.isMultipleOfRef
import solver.clue.multiReference
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

private val clueMap: Map<String, ClueConstructor> = mapOf(
    "1A" to simpleClue { it == (it * it).lastNDigits(5) },
    "5A" to simpleClue { isProductOfFourConsecutiveIntegers((it * it) - 1) },
    "7A" to simpleClue { it == it.digits().map(::integerFactorial).sum() },
    "9A" to emptyClue(), // TODO - A number a such that the equation 3x^2+ax+75 has a repeated root
    "10A" to geometricMeanOf("6D", "4D"),
    "12A" to emptyClue(), // TODO - The 2nd, 4th, 6th, 8th, and 10th digits of this number are the highest common factors of the digits either side of them
    "13A" to geometricMeanOf("4D", "5D"),
    "14A" to simpleClue(hasUniqueDigits(9)) +
            simpleClue(doesNotContainDigit(0)), // TODO - The number formed by the first n digits of this number is divisible by n (for all n)
    "15A" to geometricMeanOf("4D", "5D"),
    "17A" to simpleClue { it.digits()[1] == 9 }, // TODO - Each digit of this number (after the first two) is a digit in the product of the previous two digits of this number
    "18A" to emptyClue(),
    "20A" to emptyClue() + isFactorOfRef("13D"), // TODO - A number, n, such that n, n+1, n+2, n+3 and n+4 all have the same number of factors
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
    "3D" to isFactorOfRef("7D") + emptyClue(), // TODO - This number can be written as the sum of the fourth powers of two rational numbers, but it cannot be written as the sum of the fourth powers of two integers
    "4D" to geometricMeanOf("13A", "15A"),
    "5D" to geometricMeanOf("15A", "18A"),
    "6D" to geometricMeanOf("10A", "13A") + isFactorOfRef("13D"),
    "7D" to isMultipleOfRef("3D") + simpleClue(isNotMultipleOf(4)) + transformedEqualsRef("27D", ::digitSum),
    "8D" to emptyClue(), // TODO - This number can be made by concatenating two other answers in this crossnumber
    "11D" to emptyClue(), // TODO - The number of squares (of any size) on a 13,178-by-13,178 chessboard
    "13D" to isMultipleOfRef("6D") + isMultipleOfRef("20A"),
    "16D" to simpleClue { it.firstNDigits(5).digitSum() == it.lastNDigits(3).digitSum() + 1 },
    "19D" to simpleClue { isPalindrome(it * it) },
    "22D" to emptyClue(), // TODO - The number of 41-dimensional sides on a 43-dimensional hypercube
    "23D" to emptyClue(), // TODO - Each digit of this number is either a factor or a multiple of the previous digit.
    "25D" to emptyClue(), // TODO - A number of the form n^2+2^n
    "27D" to emptyClue(), // TODO - The total number of zeros, threes, sixes, and nines that appear in the completed crossnumber
)

private fun geometricMeanOf(a: String, b: String) = multiReference(a, b, combiner = ::geometricMean)

val CROSSNUMBER_5 = factoryCrossnumber(grid, clueMap)