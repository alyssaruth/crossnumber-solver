package puzzles

import maths.containsDigit
import maths.digitProduct
import maths.digitSum
import maths.digits
import maths.digitsSameExceptOne
import maths.fromDigits
import maths.generatePowers
import maths.hasDigitSum
import maths.isEven
import maths.isMultipleOf
import maths.isSumOfTwoNthPowers
import maths.isTriangleNumber
import maths.lastNDigits
import maths.longDigits
import maths.pow
import solver.clue.calculationWithReference
import solver.clue.emptyClue
import solver.clue.greaterThan
import solver.clue.notEqualTo
import solver.clue.plus
import solver.clue.simpleClue
import solver.clue.singleReference
import solver.clue.singleReferenceFlattened
import solver.clue.transformedEquals
import solver.clueMap
import solver.digitReducer.DigitReducerConstructor
import solver.digitReducer.allDigits
import solver.digitReducer.simpleReducer
import solver.factoryCrossnumber
import java.math.BigInteger

/**
 * https://chalkdustmagazine.com/blog/crossnumber-winners-issue-07/
 */
fun main() {
    CROSSNUMBER_7.solve()
}

private val grid = """
    ................
    #..#.#.###.#.##.
    ..............#.
    .#.#####.#.##...
    .#..........#.#.
    .#.#.######.#...
    .#.#......#.#.#.
    .#.#.#.##.....#.
    ...#....#.#.#.#.
    .#.#.#....#.#...
    .#...##.#.#.#.#.
    .#.#..........#.
    ...##.#####.#.#.
    .#............#.
    ..###.#####.#.#.
    ................
""".trimIndent()

private val digitReducers: List<DigitReducerConstructor> = listOf(
    "10A".simpleReducer(allDigits()) { it > 0 },
    "31A".simpleReducer(allDigits()) { it > 0 }, // 7A is the digit product of this
    "3D".simpleReducer({ it.take(3) }, { isEven(it.toLong()) }),
    "3D".simpleReducer(allDigits()) { it > 0 }, // 16D is the digit product of this
    "16D".simpleReducer(allDigits()) { it > 0 }, // 18A is the digit product of this
)

private val clueMap = clueMap(
    "1A" to calculationWithReference("34A") { value, other -> oneAcrossAndThirtyFourAcross(value, other) },
    *"9A".singleReference("30A", ::digitProduct),
    *"9A".singleReference("1A", ::digitSum),
    "10A" to simpleClue { isMultipleOf(1_000_000)(digitProduct(it)) },
    "13A" to calculationWithReference("34A") { value, other -> !containsDigit(value.digits()[1])(other) },
    "14A" to digitsSameExceptOne(10),
    "17A" to simpleClue(isSumOfTwoNthPowers(3)),
    *"18A".singleReference("16D", ::digitProduct),
    "21A" to digitsSameExceptOne(5),
    *"22A".greaterThan("13A"),
    *"23A".singleReference("6D", ::digitProduct),
    "25A" to simpleClue { !containsDigit(0)(it) && digitSum(it) > digitProduct(it) },
    "26A" to simpleClue { it == BigInteger.TWO.pow(it.toInt()).digits().takeLast(3).fromDigits() },
    *"27A".singleReference("23A") { digitProduct(it) + 1 },
    "28A" to simpleClue(isMultipleOf(9)),
    *"30A".singleReference("27A") { digitProduct(it - 1) },
    *"31A".transformedEquals("7D", ::digitProduct),
    *"32A".singleReference("34A", ::digitSum),
    "34A" to calculationWithReference("1A") { value, other -> oneAcrossAndThirtyFourAcross(other, value) } +
            calculationWithReference("13A") { value, other -> !containsDigit(other.digits()[1])(value) },

    "2D" to simpleClue { it == sumOfCubesOfDigits(it) },
    "3D" to emptyClue(), // Covered in digit reducers
    *"4D".singleReference("5D", ::sumOfCubesOfDigits),
    *"5D".singleReference("4D", ::sumOfCubesOfDigits),
    *"5D".notEqualTo("4D"),
    *"6D".singleReference("18A", ::digitProduct),
    "7D" to simpleClue(::isTriangleNumber) + simpleClue(hasDigitSum(18)),
    *"8D".singleReferenceFlattened("13A", generatePowers(16)),
    *"10D".singleReferenceFlattened("22A", generatePowers(14)),
    "11D" to simpleClue { it == sumOfCubesOfDigits(it) },
    *"12D".singleReferenceFlattened("13A", generatePowers(14)),
    *"15D".singleReference("28A") { it.lastNDigits(8) },
    *"16D".singleReference("3D", ::digitProduct),
    "19D" to simpleClue { !containsDigit(0)(it) && isMultipleOf(9)(digitProduct(it)) },
    "20D" to simpleClue { it.longDigits().windowed(2).all { (a, b) -> isMultipleOf(b)(a) } },
    "24D" to simpleClue { it == sumOfNthPowerOfDigits(4)(it) },
    "29D" to simpleClue { it == sumOfNthPowerOfDigits(5)(it) },
    *"33D".singleReference("9A", ::digitProduct)
)

private fun sumOfCubesOfDigits(value: Long) = sumOfNthPowerOfDigits(3)(value)

private fun sumOfNthPowerOfDigits(n: Int): (Long) -> Long = { value -> value.digits().sumOf { it.pow(n) } }

private fun oneAcrossAndThirtyFourAcross(a1: Long, a34: Long) =
    a1.digits().first() == a34.digits().last() && a1.toBigInteger().times(a34.toBigInteger())
        .mod(1_000_000_000_000.toBigInteger()) == BigInteger.ZERO

val CROSSNUMBER_7 = factoryCrossnumber(grid, clueMap, digitReducers, skipSymmetryCheck = true)