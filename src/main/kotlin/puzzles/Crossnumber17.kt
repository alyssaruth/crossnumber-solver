package puzzles

import maths.areAnagrams
import maths.digitSum
import maths.digitsAllTheSame
import maths.firstNDigits
import maths.hasDigitRelationship
import maths.hasDigitSum
import maths.isEven
import maths.isMultipleOf
import maths.isOdd
import maths.isPalindrome
import maths.isSquare
import maths.lastNDigits
import maths.longDigits
import solver.ClueConstructor
import solver.ClueId
import solver.Crossnumber
import solver.Orientation
import solver.PartialSolution
import solver.PendingSolution
import solver.clue.ComputedPossibilitiesClue
import solver.clue.calculationWithDualReference
import solver.clue.calculationWithReference
import solver.clue.equalToNumberOfClueWithAnswer
import solver.clue.isAnagramOf
import solver.clue.isEqualTo
import solver.clue.isGreaterThan
import solver.clue.isMultipleOf
import solver.clue.isNTimes
import solver.clue.isProductOf
import solver.clue.simpleClue
import solver.clue.singleReference
import solver.clueMap
import solver.factoryCrossnumber

/**
 * https://chalkdustmagazine.com/regulars/crossnumber/prize-crossnumber-issue-17/
 */
fun main() {
    CROSSNUMBER_17.solve()
}

private val grid = """
    ##.....###....#
    ###.##.#...##.#
    ....##.#.###..#
    ##...#.......##
    .###...#..#....
    .....###..###.#
    ####.#......#.#
    .#......#.....#
    .......###..#.#
    .#......#..##.#
    ####.#....##...
    .....###.....#.
    .#...#...##.##.
    .##....#..#..##
    ##..####.##....
""".trimIndent()

private val clueMap = clueMap(
    *"1A".isAnagramOf("3D"),
    "1A" to simpleClue(::isPalindrome),
    *"4A".isAnagramOf("7A"),
    *"6A".isMultipleOf("51A"),
    *"7A".isNTimes(7, "4A"),
    "9A" to equalToNumberOfClueWithAnswer(Orientation.DOWN, grid.count { it == '.' }.toLong()),
    "10A" to simpleClue { it.longDigits().all(isEven) },
    "12A" to simpleClue { isPalindrome(it - 5) },
    *"16A".isNTimes(7, "6A"),
    "17A" to equalToNumberOfClueWithAnswer(Orientation.ACROSS, 17),
    *"18A".singleReference("19D") { it.firstNDigits(4) },
    *"20A".calculationWithDualReference("38A", "38D") { a20, a38, d38 -> a20 == 1 + (a38 * d38) },
    "21A" to equalToNumberOfClueWithAnswer(Orientation.ACROSS, 21),
    "22A" to digitsAllTheSame(6),
    "27A" to simpleClue(hasDigitSum(5)),
    "30A" to simpleClue { areAnagrams(it, 86420) },
    *"31A".isAnagramOf("19D"),
    "32A" to equalToNumberOfClueWithAnswer(Orientation.DOWN, 131),
    "33A" to simpleClue(hasDigitSum(5)),
    *"35A".singleReference("35D", ::digitSum),
    *"36A".isMultipleOf("46A"),
    "38A" to simpleClue(isEven),
    *"40A".isMultipleOf("51A"),
    "43A" to simpleClue(isMultipleOf(11111)),
    "45A" to simpleClue { isPalindrome(it - 5) },
    "46A" to simpleClue(isMultipleOf(3)),
    "47A" to simpleClue { areAnagrams(it, 9630) },
    "48A" to simpleClue(isOdd),
    "49A" to equalToNumberOfClueWithAnswer(Orientation.DOWN, 653),
    *"51A".isGreaterThan("9A"),
    "52A" to simpleClue { isPalindrome(it - 5) },

    "2D" to simpleClue(::isPalindrome),
    "3D" to simpleClue { isPalindrome(it - 5) },
    *"4D".singleReference("24D", ::digitSum),
    "5D" to simpleClue(::isSquare),
    *"6D".singleReference("12A") { it.lastNDigits(6) },
    "8D" to isEqualTo(18), // A000988
    "9D" to simpleClue(isOdd),
    "11D" to simpleClue { isPalindrome(it - 5) },
    "13D" to simpleClue(isMultipleOf(3)),
    "14D" to equalToNumberOfClueWithAnswer(Orientation.DOWN, 11),
    "15D" to equalToNumberOfClueWithAnswer(Orientation.DOWN, 15),
    "19D" to hasDigitRelationship { (a, b) -> b <= a },
    *"22D".isProductOf("39D", "17A"),
    *"23D".singleReference("34D") { it + 1 },
    "24D" to simpleClue(isOdd),
    *"25D".calculationWithReference("5D") { value, other -> value.lastNDigits(1) == other.firstNDigits(1) },
    *"26D".singleReference("15D") { it * it },
    *"27D".singleReference("29D") { it - 20 },
    *"28D".singleReference("27D") { it + 10 },
    *"29D".singleReference("28D") { it + 10 },
    *"34D".singleReference("23D") { it - 1 },
    "35D" to simpleClue(isOdd),
    "37D" to simpleClue(isMultipleOf(9)),
    "38D" to simpleClue(isOdd),
    "39D" to simpleClue(isOdd),
    *"40D".calculationWithDualReference("50D", "26D") { d40, d50, d26 -> d40 + d50 == d26 },
    "41D" to equalToNumberOfClueWithRef(Orientation.DOWN, "48A") { it * it },
    "42D" to simpleClue(hasDigitSum(11)),
    "44D" to simpleClue(isMultipleOf(1111)),
    "46D" to equalToNumberOfClueWithAnswer(Orientation.ACROSS, 1089),
    *"50D".isNTimes(5, "8D"),
)

val CROSSNUMBER_17 = factoryCrossnumber(grid, clueMap, skipSymmetryCheck = true)

class EqualToNumberOfClueWithRef(
    crossnumber: Crossnumber,
    private val orientation: Orientation,
    otherClue: ClueId,
    mapper: (Long) -> Long
) : ComputedPossibilitiesClue(crossnumber) {
    private val answers = lookupAnswers(otherClue)?.map(mapper)

    override val possibilities = calculatePossibilities()

    private fun calculatePossibilities(): Set<Long>? {
        if (answers == null) {
            return null
        }

        val clues = crossnumber.solutions.filterKeys { it.orientation == orientation }
        val viableClues =
            clues.filter { (_, value) ->
                (value is PartialSolution && value.possibilities.intersect(answers)
                    .isNotEmpty()) || value is PendingSolution
            }
        return viableClues.keys.map { it.number.toLong() }.toSet()
    }
}

fun equalToNumberOfClueWithRef(orientation: Orientation, otherClue: String, mapper: (Long) -> Long): ClueConstructor =
    { crossnumber -> EqualToNumberOfClueWithRef(crossnumber, orientation, ClueId.fromString(otherClue), mapper) }
