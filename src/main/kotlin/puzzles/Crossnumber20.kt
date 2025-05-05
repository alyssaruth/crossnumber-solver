package puzzles

import maths.areAnagrams
import maths.digitProduct
import maths.digitSum
import maths.digits
import maths.distinctDivisors
import maths.hasDigitRelationship
import maths.hasDigitSum
import maths.isEven
import maths.isMultipleOf
import maths.isOdd
import maths.isPrime
import maths.isSquare
import solver.GlobalClue
import solver.ISolution
import solver.PartialSolution
import solver.clue.calculationWithReference
import solver.clue.emptyClue
import solver.clue.simpleClue
import solver.clue.singleReference
import solver.clueMap
import solver.factoryCrossnumber

/**
 * https://chalkdustmagazine.com/regulars/crossnumber/prize-crossnumber-issue-20/
 */
fun main() {
    CROSSNUMBER_20.solve()
}

private val grid = """
    ##....#....##
    ...#.....#...
    #....#.#....#
    ..#.......#..
    #...#...#...#
    ......#......
    .#.#.....#.#.
    ......#......
    #...#...#...#
    ..#.......#..
    #....#.#....#
    ...#.....#...
    ##....#....##
""".trimIndent()

private val clueMap = clueMap(
    "1A" to simpleClue { isMultipleOf(it)(9999) },
    "4A" to simpleClue(isMultipleOf(32)),
    *"7A".singleReference("22A") { it + 22 },
    "9A" to hasDigitRelationship { (a, b) -> b == a || b == a - 1 },
    *"11A".singleReference("42A") { it + 22 },
    "13A" to simpleClue(isMultipleOf(201)),
    "15A" to simpleClue(hasDigitSum(13)),
    "17A" to simpleClue { it < 33 },
    *"18A".singleReference("44A") { it * 2 },
    *"21A".singleReference("47A") { it - 6 },
    *"22A".singleReference("56A") { it + 22 },
    *"24A".singleReference("40A") { it + 111 },
    *"25A".calculationWithReference("27D") { value, other -> areAnagrams(value, other * 2) },
    *"27A".singleReference("29A") { it * 2 },
    *"29A".singleReference("36A") { it * 2 },
    "32A" to simpleClue { isMultipleOf(100)(it - 34) },
    *"33A".singleReference("27A") { it * 2 },
    *"36A".singleReference("32A") { it * 2 },
    "39A" to simpleClue(isMultipleOf(9)),
    "40A" to simpleClue(isMultipleOf(111)),
    *"42A".singleReference("7A") { it + 22 },
    *"43A".calculationWithReference("34D") { value, other -> distinctDivisors(other).contains(value + 30) },
    *"44A".singleReference("33A") { it * 2 },
    *"47A".singleReference("21A") { it + 6 },
    "48A" to emptyClue(),
    "50A" to simpleClue(isMultipleOf(39)),
    *"52A".singleReference("3D") { it * 36 },
    "53A" to hasDigitRelationship { (a, b) -> b == a || b == a - 1 },
    "56A" to simpleClue(isMultipleOf(50)),
    "57A" to simpleClue(isMultipleOf(1000)),
    *"58A".singleReference("57A") { it + 1 },

    *"1D".singleReference("28D", ::digitProduct),
    "2D" to simpleClue(isMultipleOf(201)),
    *"3D".singleReference("28D", ::digitSum),
    "4D" to simpleClue(::isSquare),
    "5D" to simpleClue(hasDigitSum(12)),
    *"6D".singleReference("11A") { it + 22 },
    "8D" to simpleClue(isMultipleOf(203)),
    "10D" to simpleClue(isMultipleOf(9)),
    "12D" to simpleClue(isMultipleOf(9)),
    "14D" to hasDigitRelationship { (a, b) -> b == a || b == a - 1 },
    "16D" to simpleClue(isMultipleOf(3)),
    "19D" to simpleClue(isMultipleOf(10)),
    "20D" to simpleClue(isMultipleOf(10)),
    "23D" to simpleClue(isMultipleOf(1001)),
    "26D" to simpleClue(isMultipleOf(9)),
    *"27D".calculationWithReference("25A") { value, other -> areAnagrams(value, 5 * other) },
    "28D" to simpleClue(isEven),
    "30D" to emptyClue(),
    "31D" to simpleClue { areAnagrams(it, 468) },
    "34D" to simpleClue { areAnagrams(it, 56789) },
    "35D" to simpleClue(isMultipleOf(5)),
    "37D" to simpleClue(isMultipleOf(5)),
    "38D" to simpleClue(isMultipleOf(601)),
    "41D" to simpleClue(isMultipleOf(1111)),
    "45D" to simpleClue(isMultipleOf(302)),
    "46D" to simpleClue { it.digits().count { digit -> digit == 1 } == 1 },
    "49D" to simpleClue { it < 333 },
    "51D" to simpleClue(hasDigitSum(4)),
    "54D" to simpleClue(isMultipleOf(10)),
    *"55D".singleReference("54D") { it + 3 }
)

private val globalClues = listOf(
    numberOfCluesOfTypeIsAlsoOfType(isEven),
    numberOfCluesOfTypeIsAlsoOfType(isOdd),
    numberOfCluesOfTypeIsAlsoOfType(::isPrime),
    numberOfCluesOfTypeIsAlsoOfType(::isSquare),
    numberOfCluesOfTypeIsAlsoOfType(isMultipleOf(3))
)

private fun numberOfCluesOfTypeIsAlsoOfType(predicate: (Long) -> Boolean): GlobalClue = { crossnumber ->
    val solutions = crossnumber.solutions.values.filterIsInstance<PartialSolution>()
    val knownAnswers = solutions.filter(ISolution::isSolved).flatMap { it.possibilities }
    val unknowns = solutions.filterNot(ISolution::isSolved).map { it.possibilities }
    val knownMatches = knownAnswers.count(predicate)
    val possibleExtras = unknowns.count { it.any(predicate) }

    val allPossibilities = knownMatches..(knownMatches + possibleExtras).toLong()
    allPossibilities.any(predicate)
}

val CROSSNUMBER_20 = factoryCrossnumber(grid, clueMap, globalClues = globalClues)