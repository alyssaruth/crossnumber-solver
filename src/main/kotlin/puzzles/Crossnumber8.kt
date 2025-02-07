package puzzles

import maths.digitSum
import maths.digits
import maths.firstNDigits
import maths.hcf
import maths.isCoprimeWith
import maths.isMultipleOf
import maths.isOdd
import maths.isPalindrome
import maths.isPrime
import maths.primeFactors
import solver.ClueConstructor
import solver.clue.calculationWithReference
import solver.clue.dualReference
import solver.clue.emptyClue
import solver.clue.isFactorOf
import solver.clue.isMultipleOf
import solver.clue.isSumOf
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
    "32A".digitReference(firstNDigits(1), "33D", { it.first() }) { digit, digits -> digits.contains(digit) }
)

private val clueMap = clueMap(
    "1A" to simpleClue { ((it * it) - 1).primeFactors().max() == 3L },
    *"3A".transformedEquals("18A", ::digitSum),
    "10A" to emptyClue(), // Covered by digit reducer
    *"12A".isSumOf("2D", "25D", "19A"),
    *"13A".isMultipleOf("1D"),
    "13A" to simpleClue { it.digits().first() == it.digits()[1] },
    "15A" to calculationWithReference("1A") { value, other -> value < other },
    "16A" to simpleClue(::isPalindrome),
    *"17A".singleReference("17D") { it + 20 },
    "18A" to simpleClue(isMultipleOf(3)),
    "19A" to simpleClue(::isPalindrome),
    "20A" to calculationWithReference("17D") { value, other -> isCoprimeWith(other)(value) },
    "21A" to simpleClue(::isPrime), // TODO - A prime number of the form nn+1 for some integer n
    "22A" to calculationWithReference("20A") { value, other -> isCoprimeWith(other)(value) },
    *"23A".primeThatIsSixteenMoreThanThreeTimes("5D"),
    "25A" to simpleClue(isMultipleOf(10)),
    "26A" to calculationWithReference("1A") { value, other -> value < other },
    *"27A".isFactorOf("24D"),
    "28A" to emptyClue(),
    "30A" to dualReference("17A", "20A", ::hcf),
    *"31A".isMultipleOf("32A"),
    "32A" to emptyClue(), // Covered by digit reducer
    "34A" to emptyClue(), // A number k such that k×2^n+1 is not prime for any integer n>0
    *"36A".primeThatIsSixteenMoreThanThreeTimes("20D"),
    "37A" to simpleClue { !isPalindrome(it) },

    *"1D".singleReference("11D") { it.firstNDigits(2) },
    "2D" to simpleClue(::isPalindrome),
    *"3D".primeThatIsSixteenMoreThanThreeTimes("4D"),
    *"4D".primeThatIsSixteenMoreThanThreeTimes("22D"),
    *"5D".primeThatIsSixteenMoreThanThreeTimes("6D"),
    "6D" to simpleClue(::isPrime),
    "7D" to calculationWithReference("1A") { value, other -> value > other },
    *"8D".primeThatIsSixteenMoreThanThreeTimes("26D"),
    *"9D".primeThatIsSixteenMoreThanThreeTimes("36A"),
)

private fun String.primeThatIsSixteenMoreThanThreeTimes(other: String): Array<Pair<String, ClueConstructor>> =
    singleReference(other) { (it * 3) + 16 } + (this to simpleClue(::isPrime))

val CROSSNUMBER_8 = factoryCrossnumber(grid, clueMap, digitReducers)