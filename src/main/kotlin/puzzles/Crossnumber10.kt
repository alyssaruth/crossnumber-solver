package puzzles

import maths.allCombinations
import maths.areAnagrams
import maths.digitCounts
import maths.digitSum
import maths.digits
import maths.digitsAllTheSame
import maths.hasDigitRelationship
import maths.hasDigitSum
import maths.isMultipleOf
import maths.isPalindrome
import maths.isPrime
import maths.isSquare
import maths.lastNDigits
import maths.pow
import maths.primeFactors
import maths.reversed
import solver.ClueId
import solver.Crossnumber
import solver.DigitMap
import solver.Orientation
import solver.clue.calculationWithReference
import solver.clue.isDifferenceBetween
import solver.clue.isEqualTo
import solver.clue.isFactorOf
import solver.clue.isGreaterThan
import solver.clue.isHcfOf
import solver.clue.isLcmOf
import solver.clue.isLessThan
import solver.clue.isMultipleOf
import solver.clue.isNotEqualTo
import solver.clue.isSumOf
import solver.clue.makeCalculationWithReferences
import solver.clue.simpleClue
import solver.clue.singleReference
import solver.clue.unorderedCalculationWithReferences
import solver.clueMap
import solver.digitReducer.AbstractDigitReducer
import solver.digitReducer.DigitReducerConstructor
import solver.digitReducer.allDigits
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

private val digitReducers: List<DigitReducerConstructor> = listOf(::TwentySevenAcross)

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
    *"21A".isHcfOf("26A", "34A"),
    "22A" to simpleClue { isPalindrome(it - 6) },
    "23A" to hasDigitRelationship { (a, b) -> b == a + 1 || b == a - 3 },
    *"24A".isNotEqualTo("32A"),
    *"24A".isNotEqualTo("40A"),
    *"25A".isDifferenceBetween("31D", "15A"),
    *"26A".isHcfOf("21A", "34A"),
    "27A" to hasDigitRelationship(3) { (a, b, c) -> b == a + c || b == abs(a - c) },
    *"32A".isHcfOf("10A", "24A"),
    "33A" to simpleClue { isPalindrome(it - 3) },
    *"34A".isHcfOf("21A", "26A"),
    "35A" to hasDigitRelationship { (a, b) -> b == a + 1 || b == a - 3 },
    *"37A".isDifferenceBetween("49A", "13A"),
    *"40A".isLcmOf("10A", "24A"),
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
        other.digits().count { digit -> (value.digitCounts()[digit] ?: 0) > 1 } == 1
    },
    *"5D".unorderedCalculationWithReferences("23A", "30D") { it.sum() > 1_000_000_000 },
    "6D" to simpleClue { isSquare(2665.pow(2) - (it * it)) },
    "7D" to makeCalculationWithReferences("8D", "11A", "50D") { (d7, d8, a11, d50) ->
        quadraticStuff(d7, d8, d50, a11)
    },
    "8D" to makeCalculationWithReferences("7D", "11A", "50D") { (d8, d7, a11, d50) ->
        quadraticStuff(d7, d8, d50, a11)
    },
    "11A" to makeCalculationWithReferences("50D", "7D", "8D") { (a11, d50, d7, d8) ->
        quadraticStuff(d7, d8, d50, a11)
    },
    "50D" to makeCalculationWithReferences("11A", "7D", "8D") { (d50, a11, d7, d8) ->
        quadraticStuff(d7, d8, d50, a11)
    },
    "9D" to simpleClue(::isPalindrome),
    "13D" to simpleClue(::isPrime),
    "16D" to hasDigitRelationship(3) { (a, b, c) -> b == a + c || b == abs(a - c) },
    "17D" to simpleClue { it.digitSum().toLong() == it.digits()[0].pow(2) },
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

val CROSSNUMBER_10 = factoryCrossnumber(grid, clueMap, digitReducers = digitReducers)

/**
 * Pure optimisation, it's not needed but helps to reduce 27A to something more manageable
 */
private class TwentySevenAcross(crossnumber: Crossnumber) :
    AbstractDigitReducer(ClueId(27, Orientation.ACROSS), allDigits(), crossnumber) {

    override fun apply(digitMap: DigitMap): DigitMap {
        val lambda: (Int, Int, Int) -> Boolean = { a, b, c -> b == a + c || b == abs(a - c) }

        return squares.windowed(3).fold(digitMap) { currentMap, (ptA, ptB, ptC) ->
            val neighbourDigits = listOf(currentMap.getValue(ptA), currentMap.getValue(ptC))
            val neighbourCombos = neighbourDigits.allCombinations()

            val middleDigits = digitMap.getValue(ptB)
            val newMiddleDigits =
                middleDigits.filter { b -> neighbourCombos.any { (a, c) -> lambda(a, b, c) } }

            val newCombos = neighbourCombos.filter { (a, c) -> newMiddleDigits.any { b -> lambda(a, b, c) } }
            val newAValues = newCombos.map { it.first() }.distinct().sorted()
            val newCValues = newCombos.map { it.last() }.distinct().sorted()

            currentMap + (ptA to newAValues) + (ptB to newMiddleDigits) + (ptC to newCValues)
        }
    }

}

/**
 * 7D: A solution of x^2-(50D)x+(11A)=0
 * 8D: A solution of x^2-(50D)x+(11A)=0
 *
 * (x-a)(x-b) = 0 where 11A = a*b, 50D = a + b, and 7D/8D are one of a or b
 *
 * So D50 = 7D + 8D and 11A = 7D*8D
 */
private fun quadraticStuff(d7: Long, d8: Long, d50: Long, a11: Long) = d50 - d7 == d8 && d7 * d8 == a11
