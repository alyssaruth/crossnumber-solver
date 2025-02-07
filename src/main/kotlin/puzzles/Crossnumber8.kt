package puzzles

import maths.digitSum
import maths.digits
import maths.distinctDivisors
import maths.firstNDigits
import maths.hcf
import maths.isKnownSierpinskiNumber
import maths.isMultipleOf
import maths.isOdd
import maths.isPalindrome
import maths.isPrime
import maths.isSumOfConsecutive
import maths.primeFactors
import maths.squaresUpTo
import solver.ClueConstructor
import solver.clue.dualReference
import solver.clue.emptyClue
import solver.clue.isCoprimeWith
import solver.clue.isFactorOf
import solver.clue.isGreaterThan
import solver.clue.isLessThan
import solver.clue.isMultipleOf
import solver.clue.isSumOf
import solver.clue.plus
import solver.clue.simpleClue
import solver.clue.singleReference
import solver.clue.transformedEquals
import solver.clueMap
import solver.digitReducer.DigitReducerConstructor
import solver.digitReducer.allDigits
import solver.digitReducer.digitReference
import solver.digitReducer.firstNDigits
import solver.digitReducer.simpleReducer
import solver.factoryCrossnumber

/**
 * https://chalkdustmagazine.com/regulars/crossnumber/prize-crossnumber-issue-08/
 */
fun main() {
    CROSSNUMBER_8.solve()
}

private val grid = """
    ..#........
    .....#.....
    #.......#..
    .......#...
    ..#...#....
    .#...#...#.
    ....#...#..
    ...#.......
    ..#.......#
    .....#.....
    ........#..
""".trimIndent()

private val digitReducers: List<DigitReducerConstructor> = listOf(
    "10A".simpleReducer(allDigits()) { isOdd(it.toLong()) },
    "32A".digitReference(firstNDigits(1), "33D", { it.first() }) { digit, digits -> digits.contains(digit) },
)

private val clueMap = clueMap(
    "1A" to simpleClue { ((it * it) - 1).primeFactors().max() == 3L },
    *"3A".transformedEquals("18A", ::digitSum),
    "10A" to emptyClue(), // Covered by digit reducer
    *"12A".isSumOf("2D", "25D", "19A"),
    *"13A".isMultipleOf("1D"),
    "13A" to simpleClue { it.digits().first() == it.digits()[1] },
    *"15A".isLessThan("1A"),
    "16A" to simpleClue(::isPalindrome),
    *"17A".singleReference("17D") { it + 20 },
    "18A" to simpleClue(isMultipleOf(3)),
    "19A" to simpleClue(::isPalindrome),
    *"20A".isCoprimeWith("17D"),
    "21A" to simpleClue(::isPrime),
    *"22A".isCoprimeWith("20A"),
    *"23A".primeThatIsSixteenMoreThanThreeTimes("5D"),
    "25A" to simpleClue(isMultipleOf(10)),
    *"26A".isLessThan("1A"),
    *"27A".isFactorOf("24D"),
    "28A" to emptyClue(),
    "30A" to dualReference("17A", "20A", ::hcf),
    *"31A".isMultipleOf("32A"),
    "32A" to emptyClue(), // Covered by digit reducer
    "34A" to simpleClue(::isKnownSierpinskiNumber),
    *"36A".primeThatIsSixteenMoreThanThreeTimes("20D"),
    "37A" to simpleClue { !isPalindrome(it) },

    *"1D".singleReference("11D") { it.firstNDigits(2) },
    "2D" to simpleClue(::isPalindrome),
    *"3D".primeThatIsSixteenMoreThanThreeTimes("4D"),
    *"4D".primeThatIsSixteenMoreThanThreeTimes("22D"),
    *"5D".primeThatIsSixteenMoreThanThreeTimes("6D"),
    "6D" to simpleClue(::isPrime),
    *"7D".isGreaterThan("1A"),
    *"8D".primeThatIsSixteenMoreThanThreeTimes("26D"),
    *"9D".primeThatIsSixteenMoreThanThreeTimes("36A"),
    *"11D".isLessThan("21D"),
    "14D" to simpleClue { distinctDivisors(it).sum() == it * 3 },
    *"16D".transformedEquals("18A", ::digitSum),
    *"17D".isCoprimeWith("22A"),
    *"20D".primeThatIsSixteenMoreThanThreeTimes("3D"),
    *"21D".isFactorOf("3A"),
    *"22D".primeThatIsSixteenMoreThanThreeTimes("8D"),
    *"24D".isMultipleOf("27A"),
    "25D" to simpleClue(::isPalindrome),
    *"26D".primeThatIsSixteenMoreThanThreeTimes("23A"),
    "28D" to simpleClue { ((it * it) - 1).primeFactors().max() == 7L },
    "29D" to simpleClue(isSumOfConsecutive(2, digits = 3, ::squaresUpTo)) +
            simpleClue(isSumOfConsecutive(3, digits = 3, ::squaresUpTo)),
    "31D" to simpleClue { ((it * it) - 1).primeFactors().max() == 5L },
    *"33D".isMultipleOf("30A"),
    "35D" to simpleClue { isOdd(it) && !isPrime(it) }
)

private fun String.primeThatIsSixteenMoreThanThreeTimes(other: String): Array<Pair<String, ClueConstructor>> =
    singleReference(other) { (it * 3) + 16 } + (this to simpleClue(::isPrime))

val CROSSNUMBER_8 = factoryCrossnumber(grid, clueMap, digitReducers)