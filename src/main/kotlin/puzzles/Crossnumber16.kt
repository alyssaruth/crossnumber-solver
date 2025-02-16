package puzzles

import maths.digits
import maths.hasDigitRelationship
import maths.hasDigitSum
import maths.hcf
import maths.isEven
import maths.isMultipleOf
import maths.isPrime
import maths.isSquare
import maths.longDigits
import maths.tryAllCombinations
import solver.ClueConstructor
import solver.Crossnumber
import solver.clue.ComputedPossibilitiesClue
import solver.clue.calculationWithReference
import solver.clue.emptyClue
import solver.clue.isAnagramOf
import solver.clue.isFactorOf
import solver.clue.isGreaterThan
import solver.clue.isNTimes
import solver.clue.isProductOf
import solver.clue.simpleClue
import solver.clue.simplyNot
import solver.clue.singleReference
import solver.clue.singleReferenceFlattened
import solver.clueMap
import solver.factoryCrossnumber

/**
 * https://chalkdustmagazine.com/regulars/crossnumber/prize-crossnumber-issue-16/
 */
fun main() {
    CROSSNUMBER_16.solve()
}

private val grid = """
    ...#.....#...
    .#...#.#...#.
    ...#.###.#...
    #.##..#..##.#
    ....#...#....
    .##..#.#..##.
    ..##.....##..
    .##..#.#..##.
    ....#...#....
    #.##..#..##.#
    ...#.###.#...
    .#...#.#...#.
    ...#.....#...
""".trimIndent()

/**
 * 8 lies in total - 4 across and 4 down.
 *
 * Already coded:
 *
 *  - 16D is a lie - no two-digit number has four distinct prime factors. 2*3*5*7 = 210 is the smallest.
 *  - 1 of 3D or 38D is a lie  -> guaranteed D lie
 *  - 1 of 21D or 29D is a lie -> guaranteed D lie
 *  - 1 of 25D or 31D is a lie -> guaranteed D lie
 *  - 1 of 1A or 1D is a lie   -> 1A is a lie
 *  - 1 of 12A or 7D is a lie (can't have an even 3-digit prime) -> 12A is a lie
 *
 *  That left this series of clues - seems the two across ones are the other two lies:
 *
 *  - 41A = 2 * 41D
 *  - 41D = 2 * 48A  (definitely true)
 *  - 48A = 2 * 42D
 *  - 42D = 2 * 41A (definitely true)
 */
private val clueMap = clueMap(
    *"1A".singleReferenceFlattened("1D") { listOf(it + 1, it - 1) },
    "3A" to simpleClue(isMultipleOf(11111)),
    "6A" to hasDigitRelationship { (a, b) -> b == a - 1 },
    "8A" to simpleClue(isMultipleOf(111)),
    *"9A".singleReference("8A") { it + 111 },
    *"10A".calculationWithReference("48A") { x, y -> hcf(x, y) == 6L },
    *"12A".calculationWithReference("7D") { a12, d7 -> isEven(a12) || isPrime(d7) },
    *"14A".isNTimes(2, "4D"),
    *"16A".isFactorOf("19A"),
    "17A" to simpleClue(hasDigitSum(11)),
    *"19A".isGreaterThan("1A"),
    "21A" to simpleClue(hasDigitSum(13)),
    "23A" to simpleClue(::isSquare),
    *"25A".singleReference("23A") { it / 4 },
    *"26A".isNTimes(2, "28A"),
    *"27A".isProductOf("24D", "25D"),
    "28A" to simplyNot(::isPrime),
    *"29A".calculationWithReference("38D") { a29, d38 -> a29.longDigits().all { isMultipleOf(it)(d38) }},
    *"30A".singleReference("29A") { it / 4 },
    *"32A".isAnagramOf("17A"),
    *"34A".singleReference("10A") { it - 15 },
    *"36A".isAnagramOf("21A"),
    "38A" to simpleClue(isMultipleOf(3)),
    *"38A".calculationWithReference("12A") { x, y -> x.digits().last() == y.digits().last() },
    "39A" to simpleClue(::isSquare),
    // *"41A".isNTimes(2, "41D"),
    "43A" to simpleClue(hasDigitSum(10)),
    *"45A".isNTimes(2, "47A"),
    "47A" to simpleClue(isMultipleOf(9)),
    // *"48A".isNTimes(2, "42D"),
    "49A" to simpleClue(isMultipleOf(11111)),
    "50A" to simpleClue(isMultipleOf(55)),

    "1D" to emptyClue(), // Covered by 1A
    *"2D".isNTimes(2, "10A"),
    *"3D".singleReferenceFlattened("38D") { listOf(it * 2, it / 2) },
    *"4D".isFactorOf("1A"),
    *"5D".isNTimes(2, "1D"),
    *"6D".isNTimes(2, "7D"),
    "7D" to simpleClue(::isPrime),
    *"11D".isNTimes(2, "13D"),
    "13D" to simpleClue(isMultipleOf(111)),
    *"15D".isNTimes(2, "14A"),
    "16D" to emptyClue(), // This was a lie!
    "17D" to simpleClue(isMultipleOf(11111)),
    "18D" to simpleClue(::isSquare),
    *"20D".isProductOf("19A", "34A"),
    *"21D".singleReferenceFlattened("29D") { listOf(it + 5, it - 5) },
    "22D" to simpleClue(isMultipleOf(11111)),
    *"24D".singleReference("6D") { it + 30 },
    *"25D".calculationWithReference("31D") { d25, d31 -> isPrime(d25) || isMultipleOf(d31)(d25) },
    "29D" to emptyClue(), // Covered by 21D
    "31D" to emptyClue(), // Covered by 25D
    *"33D".isNTimes(2, "11D"),
    "34D" to emptyClue(), // A factor of an anagram of 6D, seemingly not needed
    *"35D".isGreaterThan("15D"),
    *"37D".isNTimes(2, "33D"),
    "38D" to emptyClue(), // Covered by 3D
    "40D" to simpleClue(isMultipleOf(1111)),
    *"41D".isNTimes(2, "48A"),
    *"42D".isNTimes(2, "41A"),
    "43D" to simpleClue(isMultipleOf(55)),
    "44D" to simpleClue { it < 200 },
    "46D" to fortySixDown(),
)

val CROSSNUMBER_16 = factoryCrossnumber(grid, clueMap)

/**
 * The sum of the digits marked by arrows (row 11)
 */
private class FortySixDown(crossnumber: Crossnumber): ComputedPossibilitiesClue(crossnumber) {
    override val possibilities = computePossibilities()

    private fun computePossibilities(): Set<Long>? {
        val squares = crossnumber.digitMap.filterKeys { it.y == 11 }.values.toList()
        return squares.tryAllCombinations()?.map { it.sum().toLong() }?.toSet()
    }
}

private fun fortySixDown(): ClueConstructor = { FortySixDown(it) }