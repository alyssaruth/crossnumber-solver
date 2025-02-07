package puzzles

import maths.areAnagrams
import maths.digitCounts
import maths.digitSum
import maths.digits
import maths.digitsAllTheSame
import maths.hasDigitRelationship
import maths.hasDigitSum
import maths.hcf
import maths.integerPartitions
import maths.isMultipleOf
import maths.isPalindrome
import maths.isPrime
import maths.isSquare
import maths.lastNDigits
import maths.lcm
import maths.pow
import maths.primeFactors
import maths.product
import maths.reversed
import solver.clue.calculationWithReference
import solver.clue.calculationWithReferences
import solver.clue.dualReference
import solver.clue.emptyClue
import solver.clue.isDifferenceBetween
import solver.clue.isEqualTo
import solver.clue.isFactorOf
import solver.clue.isGreaterThan
import solver.clue.isLessThan
import solver.clue.isMultipleOf
import solver.clue.isNotEqualTo
import solver.clue.isSumOf
import solver.clue.makeCalculationWithReferences
import solver.clue.simpleClue
import solver.clue.singleReference
import solver.clueMap
import solver.factoryCrossnumber
import kotlin.math.abs

/**
 * https://chalkdustmagazine.com/regulars/crossnumber/prize-crossnumber-issue-10/
 */
fun main() {
    CROSSNUMBER_10.solve()
}

private val grid = """
    .....#.........
    .##.#.##..##...
    ....#.#...##.##
    .##.......##...
    .##.#.##....#..
    .#...#.........
    #..######...#..
    ...............
    ..#...######..#
    .........#...#.
    ..#....##.#.##.
    ...##.......##.
    ##.##...#.#....
    ...##..##.#.##.
    .........#.....
""".trimIndent()

private val clueMap = clueMap(
    "1A" to simpleClue { areAnagrams(it, it * 9) },
    "3A" to digitsAllTheSame(9),
    *"10A".isNotEqualTo("32A"),
    *"10A".isNotEqualTo("40A"),
    *"11A".isMultipleOf("8D"),
    *"12A".isSumOf("25A", "43A"),
    *"13A".singleReference("49A") { it.reversed() },
    "14A" to hasDigitRelationship { (a, b) -> b == a - 1 || b == a - 2 },
    *"15A".singleReference("31D") { it.reversed() },
    "18A" to simpleClue { isPalindrome(it - 5) },
    "21A" to dualReference("26A", "34A", ::hcf),
    "22A" to simpleClue { isPalindrome(it - 6) },
    "23A" to hasDigitRelationship { (a, b) -> b == a + 1 || b == a - 3 },
    *"24A".isNotEqualTo("32A"),
    *"24A".isNotEqualTo("40A"),
    *"25A".isDifferenceBetween("31D", "15A"),
    "26A" to dualReference("21A", "34A", ::hcf),
    "27A" to hasDigitRelationship(3) { (a, b, c) -> b == a + c || b == abs(a - c) },
    "32A" to dualReference("10A", "24A", ::hcf),
    "33A" to simpleClue { isPalindrome(it - 3) },
    "34A" to dualReference("21A", "26A", ::hcf),
    "35A" to hasDigitRelationship { (a, b) -> b == a + 1 || b == a - 3 },
    *"37A".isDifferenceBetween("49A", "13A"),
    "40A" to dualReference("10A", "24A", ::lcm),
    "41A" to simpleClue { isPalindrome(it - 4) },
    *"43A".singleReference("25A") { it.reversed() },
    "45A" to simpleClue { isPalindrome(it - 7) },
    *"47A".singleReference("37A") { it.reversed() },
    *"48A".isSumOf("47A", "37A"),
    "49A" to hasDigitRelationship { (a, b) -> b < a },
    *"51A".singleReference("23A", ::digitSum),
    "52A" to digitsAllTheSame(9),
    *"53A".isEqualTo("1A"),

    *"1D".isMultipleOf("9D"),
    *"2D".isMultipleOf("1D"),
    *"4D".calculationWithReference("33A") { value, other ->
        other.digits().count { digit -> (value.digitCounts()[digit] ?: 0) > 1 } == 1 },
    *"5D".calculationWithReferences("23A", "30D") { it.sum() > 1_000_000_000 },
    "6D" to simpleClue { isSquare(2665.pow(2) - (it * it)) },
    "7D" to emptyClue(), // TODO - A solution of x^2-(50D)x+(11A)=0
    "8D" to emptyClue(), // TODO - A solution of x^2-(50D)x+(11A)=0
    "50D" to makeCalculationWithReferences("11A", "7D") { (d50, a11, d7) ->
        d50.toInt().integerPartitions(2).any { partition -> partition.product() == a11 && partition.contains(d7.toInt()) }
    },
    "50D" to makeCalculationWithReferences("11A", "8D") { (d50, a11, d8) ->
        d50.toInt().integerPartitions(2).any { partition -> partition.product() == a11 && partition.contains(d8.toInt()) }
    },
    "9D" to simpleClue(::isPalindrome),
    "13D" to simpleClue(::isPrime),
    "16D" to hasDigitRelationship(3) { (a, b, c) -> b == a + c || b == abs(a - c) },
    "17D" to simpleClue { it.digitSum().toLong() == it.digits()[0].pow(2 ) },
    "19D" to simpleClue { isPalindrome(it - 2) },
    *"20D".calculationWithReference("33A") { x, y -> x + y > 10000 },
    "22D" to simpleClue { !isPrime(it) && it.primeFactors().distinct().size == 1 },
    "24D" to simpleClue { it.digitCounts().size == 2 },
    *"27D".isMultipleOf("13D"),
    "28D" to hasDigitRelationship { (a, b) -> b == a - 1 },
    "29D" to simpleClue { isPalindrome(it - 8) },
    *"30D".calculationWithReference("22A") { value, other -> isMultipleOf(other.lastNDigits(1))(value) },
    "31D" to simpleClue(hasDigitSum(7)),
    "36D" to simpleClue(isMultipleOf(9)),
    *"36D".isLessThan("4D"),
    *"39D".isSumOf("38D", "33A"),
    *"39D".isEqualTo("2D"),
    *"42D".isEqualTo("18A"),
    "44D" to simpleClue { isPalindrome(it - 1) },
    *"46D".isFactorOf("14A"),
    *"49D".calculationWithReference("45A") { value, other -> other.digits().contains(value.digits().first()) },
    *"50D".isGreaterThan("8D")
)

val CROSSNUMBER_10 = factoryCrossnumber(grid, clueMap)