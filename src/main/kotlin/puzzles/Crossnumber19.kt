package puzzles

import maths.digitSum
import maths.digits
import maths.hasDigitRelationship
import maths.hasDigitSum
import maths.isEven
import maths.isMultipleOf
import maths.isOdd
import maths.isPalindrome
import maths.isPrime
import maths.isSquare
import maths.reversed
import solver.ClueConstructor
import solver.clue.BaseClue
import solver.clue.calculationWithReference
import solver.clue.emptyClue
import solver.clue.isEqualTo
import solver.clue.isMultipleOf
import solver.clue.isNotEqualTo
import solver.clue.makeCalculationWithReference
import solver.clue.simpleClue
import solver.clue.singleReference
import solver.clueMap
import solver.digitReducer.DigitReducerConstructor
import solver.digitReducer.allDigitsEqualTo
import solver.factoryCrossnumber

/**
 * https://chalkdustmagazine.com/regulars/crossnumber/prize-crossnumber-issue-19/
 */
fun main() {
    CROSSNUMBER_19.solve()
}

private val grid = """
    ..#..#..#...
    ..#.....#...
    ....#..#..##
    ##..........
    ..#..#...#..
    .......#...#
    #...#.......
    ..#...#..#..
    ..........##
    ##..#..#....
    ...#.....#..
    ...#..#..#..
""".trimIndent()

private val digitReducers: List<DigitReducerConstructor> = listOf(
    "3D".allDigitsEqualTo("18A")
)

private val clueMap = clueMap(
    "1A" to simpleClue(::isPrime),
    "3A" to simpleClue(isMultipleOf(3)),
    "5A" to simpleClue { it < 30 },
    "7A" to simpleClue(isEven),
    "10A" to simpleClue { isMultipleOf(it)(270) },
    "11A" to simpleClue { !isMultipleOf(3)(it) },
    "13A" to simpleClue(isMultipleOf(3)),
    "14A" to simpleClue(isMultipleOf(8)),
    *"16A".singleReference("12D", ::digitSum),
    "17A" to simpleClue(isMultipleOf(3)),
    *"18A".isEqualTo("3D"),
    "23A" to simpleClue { it > 90 },
    "25A" to simpleClue(isMultipleOf(6)),
    "26A" to simpleClue { isMultipleOf(3)(it) && !isMultipleOf(9)(it) },
    *"27A".singleReference("7A", ::digitSum),
    "28A" to simpleClue { it < 1170000 },
    "31A" to hasDigitRelationship(3) { (a, b, c) -> 2 * b == a + c },
    "33A" to simpleClue { it < 222 },
    "34A" to simpleClue { it > 2222222 },
    "37A" to simpleClue { isPrime(it - 1) },
    "38A" to simpleClue { it > 380 },
    "40A" to simpleClue { isMultipleOf(it)(132) },
    "41A" to simpleClue { !isPrime(it) },
    "42A" to simpleClue(hasDigitSum(14)),
    "46A" to simpleClue { !isPrime(it) },
    "47A" to simpleClue(isMultipleOf(9)),
    "48A" to simpleClue { it < 1234 },
    *"51A".isMultipleOf("52D"),
    "53A" to simpleClue { it < 66666 },
    "55A" to simpleClue { it > 90 },
    *"56A".isMultipleOf("51D"),
    "57A" to simpleClue(::isPrime),
    "58A" to simpleClue { isMultipleOf(it)(84) },
    "59A" to makeCalculationWithReference("37A") { value, other -> value < other },

    "1D" to simpleClue(::isSquare),
    "2D" to simpleClue(isMultipleOf(11)),
    "3D" to emptyClue(),
    *"4D".singleReference("11A", ::digitSum),
    "5D" to simpleClue { it < 2222222 },
    *"6D".singleReference("3D", ::digitSum),
    "7D" to simpleClue(isMultipleOf(8)),
    *"8D".isEqualTo("59A"),
    "9D" to simpleClue(isMultipleOf(4)),
    "12D" to simpleClue(isOdd),
    "15D" to simpleClue(::isPrime),
    "17D" to simpleClue(hasDigitSum(10)),
    "19D" to simpleClue { it > 600 },
    "20D" to simpleClue { isMultipleOf(3)(it) && !isMultipleOf(9)(it) },
    "21D" to simpleClue(::isPalindrome),
    "22D" to simpleClue { !isMultipleOf(5)(it) },
    *"23D".calculationWithReference("28A") { d23, a28 -> d23 != a28.digitSum().toLong() },
    "24D" to simpleClue(isOdd),
    "29D" to simpleClue(hasDigitSum(17)),
    "30D" to hasDigitRelationship(3) { (a, b, c) -> 2 * b == a + c },
    *"32D".singleReference("5D", ::digitSum),
    "35D" to simpleClue { it < 300 },
    "36D" to simpleClue(hasDigitSum(5)),
    "37D" to simpleClue { isPrime(it + 1) },
    "39D" to simpleClue(isOdd),
    "43D" to simpleClue(isMultipleOf(1111)),
    "44D" to simpleClue { it < 222 },
    "45D" to simpleClue { !isPrime(it) },
    *"49D".isNotEqualTo("50D"),
    "50D" to simpleClue { !isMultipleOf(3)(it) },
    "51D" to simpleClue(::isPrime),
    "52D" to simpleClue { !isPrime(it) },
    "53D" to simpleClue(isMultipleOf(9)),
    "54D" to simpleClue { isMultipleOf(it)(1612) }
).mapValues { alsoTrueOfReverse(it.value) }

val CROSSNUMBER_19 = factoryCrossnumber(grid, clueMap, digitReducers)

private class AlsoTrueOfReverse(val clue: BaseClue) : BaseClue() {
    override fun check(value: Long) = value.digits().last() != 0 && clue.check(value) && clue.check(value.reversed())

    override fun totalCombinations(solutionCombos: Long) = clue.totalCombinations(solutionCombos)
}

private fun alsoTrueOfReverse(clue: ClueConstructor): ClueConstructor = { crossnumber ->
    AlsoTrueOfReverse(clue(crossnumber))
}


