package puzzles

import maths.digits
import maths.hasDigitProduct
import maths.hasDigitRelationship
import maths.hasDigitSum
import maths.hasDistinctDigits
import maths.isEven
import maths.isMultipleOf
import maths.isOdd
import maths.isPalindrome
import maths.isPrime
import maths.isSquare
import maths.onlyContainsDigits
import solver.Crossnumber
import solver.DigitMap
import solver.GlobalClue
import solver.Point
import solver.clue.emptyClue
import solver.clue.isGreaterThan
import solver.clue.isLessThan
import solver.clue.plus
import solver.clue.simpleClue
import solver.clue.simplyNot
import solver.clue.singleReference
import solver.clueMap
import solver.digitReducer.AbstractDigitReducer
import solver.digitReducer.DigitReducerConstructor
import solver.factoryCrossnumber
import kotlin.collections.any

/**
 * https://chalkdustmagazine.com/regulars/crossnumber/prize-crossnumber-issue-23/
 */
fun main() {
    CROSSNUMBER_23.solve()
}

private val grid = """
    #..#......#..#
    .#...#..#...#.
    .....####.....
    #..#......#..#
    ....#.##.#....
    .##........##.
    ..#.#....#.#..
    ..#.#....#.#..
    .##........##.
    ....#.##.#....
    #..#......#..#
    .....####.....
    .#...#..#...#.
    #..#......#..#
""".trimIndent()

private val clueMap = clueMap(
    "1A" to simpleClue(onlyContainsDigits(6, 7)),
    "3A" to simpleClue(isMultipleOf(49)),
    "7A" to simpleClue(isMultipleOf(9)),
    "9A" to simpleClue { isMultipleOf(10)(it - 4) },
    "11A" to emptyClue(),
    "12A" to simpleClue(isMultipleOf(3)),
    "15A" to simpleClue(hasDigitSum(12)),
    "17A" to simpleClue(isEven),
    "19A" to simpleClue(isEven),
    "20A" to simpleClue(isMultipleOf(10)),
    "23A" to simpleClue(::isPrime),
    "24A" to simpleClue(::isSquare),
    "26A" to simplyNot(::isPrime),
    "28A" to simpleClue(hasDigitSum(33)),
    "31A" to simpleClue(::isPrime),
    "33A" to simpleClue(isEven),
    "34A" to simpleClue(::isPrime),
    "35A" to simpleClue(isMultipleOf(3)),
    "36A" to simpleClue(isOdd),
    "37A" to simpleClue(::isSquare),
    "38A" to simpleClue(isEven),
    "39A" to simpleClue(isEven),
    "42A" to hasDigitRelationship { (a, b) -> b == a+1 || b == a+2 },
    "45A" to simpleClue(isMultipleOf(3)),
    "46A" to hasDigitRelationship { (a, b) -> b == a + 1 },
    "48A" to simpleClue(isMultipleOf(3)),
    "49A" to simpleClue { isOdd(it) && !it.digits().contains(0) },
    "51A" to simpleClue(hasDigitSum(17)),
    "54A" to simpleClue(isEven),
    "55A" to simpleClue(isEven),
    "57A" to simpleClue { isPalindrome(it + 5) },
    "58A" to simpleClue(isMultipleOf(15)),
    "59A" to simpleClue { isEven(it) && isPalindrome(it/2) },
    "60A" to simpleClue { isOdd(it) && isSquare(it + 11) },

    "2D" to simpleClue(hasDigitSum(29)),
    "3D" to simpleClue(onlyContainsDigits(0, 2, 4, 6, 8)),
    *"4D".isLessThan("5D"),
    "5D" to emptyClue(),
    "6D" to simpleClue { isPalindrome(it - 11) },
    "7D" to simpleClue(hasDigitSum(25)),
    "8D" to simpleClue(::isSquare),
    "10D" to simpleClue(::isSquare),
    "13D" to simpleClue(isMultipleOf(3)),
    "14D" to simpleClue(hasDigitProduct(56)),
    "16D" to simpleClue(hasDigitSum(14)),
    "18D" to simplyNot(::isPrime),
    "21D" to simpleClue(isEven),
    "22D" to simpleClue(isMultipleOf(7)),
    "24D" to simplyNot(::isPrime),
    "25D" to hasDigitRelationship { (a, b) -> b < a },
    "26D" to simpleClue(isOdd),
    "27D" to simpleClue { isPalindrome(it - 11) },
    *"29D".isGreaterThan("30D"),
    "30D" to emptyClue(),
    "32D" to simpleClue { isMultipleOf(it)(28886) },
    *"34D".isGreaterThan("32D"),
    "40D" to simpleClue(isEven),
    "41D" to simpleClue(isEven),
    "43D" to emptyClue(),
    "46D" to simpleClue { isMultipleOf(4)(it - 1) },
    "47D" to hasDigitRelationship { (a, b) -> b < a },
    "49D" to simpleClue(isMultipleOf(8)),
    "50D" to simpleClue(::isPrime),
    "52D" to simplyNot(::isPrime),
    "53D" to simpleClue { isMultipleOf(4)(it) && isPrime(it/4) },
    *"55D".singleReference("56D") { it * 2 },
    "56D" to emptyClue(),
).mapValues { (clueId, clue) -> (clue + simpleClue(::hasDistinctDigits)) }

private val digitReducers: List<DigitReducerConstructor> = (0..12).flatMap { listOf(
    { crossnumber -> LineIsDistinct(crossnumber, Point::x, it, "Col") },
    { crossnumber -> LineIsDistinct(crossnumber, Point::y, it, "Row") },
)}

private class LineIsDistinct(crossnumber: Crossnumber, selector: (Point) -> Int, index: Int, descriptor: String): AbstractDigitReducer(crossnumber) {
    override val squares = crossnumber.digitMap.keys.filter { selector(it) == index }
    override val descriptor = "$descriptor $index"

    override fun apply(digitMap: DigitMap): DigitMap {
        val relevantSquares = digitMap.filter { squares.contains(it.key) }

        val singles = relevantSquares.filter { it.value.size == 1 }.flatMap { it.value }
        val singlesRemoved = relevantSquares.mapValues { if (it.value.size > 1 ) it.value.filterNot(singles::contains) else it.value }

        val furtherReduced = (0..9).fold(singlesRemoved) { currentMap, digit ->
            val squaresThatCanContainDigit = currentMap.filter { it.value.contains(digit) }
            if (squaresThatCanContainDigit.size > 1) {
                currentMap
            } else {
                currentMap.mapValues { if (it.value.contains(digit)) listOf(digit) else it.value }
            }
        }

        return digitMap + furtherReduced
    }
}

val CROSSNUMBER_23 = factoryCrossnumber(grid, clueMap, digitReducers = digitReducers)