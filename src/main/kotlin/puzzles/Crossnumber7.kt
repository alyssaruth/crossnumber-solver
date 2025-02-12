package puzzles

import maths.containsDigit
import maths.digitProduct
import maths.digitSum
import maths.digits
import maths.digitsSameExceptOne
import maths.fromDigits
import maths.generatePowers
import maths.hasDigitRelationship
import maths.hasDigitSum
import maths.isEven
import maths.isMultipleOf
import maths.isOdd
import maths.isSumOfTwoNthPowers
import maths.isTriangleNumber
import maths.lastNDigits
import maths.pow
import maths.product
import maths.sumOfCubesOfDigits
import maths.sumOfNthPowerOfDigits
import solver.ClueConstructor
import solver.ClueId
import solver.Crossnumber
import solver.DigitMap
import solver.clue.ContextualClue
import solver.clue.emptyClue
import solver.clue.isGreaterThan
import solver.clue.isNotEqualTo
import solver.clue.lookupAnswers
import solver.clue.plus
import solver.clue.simpleClue
import solver.clue.simplyNot
import solver.clue.singleReference
import solver.clue.singleReferenceFlattened
import solver.clue.transformedEquals
import solver.clueMap
import solver.digitReducer.AbstractDigitReducer
import solver.digitReducer.DigitReducerConstructor
import solver.digitReducer.allDigits
import solver.digitReducer.digitReference
import solver.digitReducer.firstNDigits
import solver.digitReducer.lastDigit
import solver.digitReducer.simpleReducer
import solver.factoryCrossnumber
import java.math.BigInteger

/**
 * https://chalkdustmagazine.com/regulars/crossnumber/prize-crossnumber-issue-07/
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
    "31A".simpleReducer(allDigits()) { it > 0 }, // 7D is the digit product of this
    "31A".digitProductIs("7D"),
    "3D".simpleReducer(firstNDigits(3)) { isEven(it.toLong()) },
    "3D".simpleReducer(allDigits()) { it > 0 }, // 16D is the digit product of this
    "16D".simpleReducer(allDigits()) { it > 0 }, // 18A is the digit product of this

    "1A".digitSumIs("9A"),
    "1A".digitReference(firstNDigits(1), "34A", lastDigit()) { myDigit, otherDigits -> otherDigits.contains(myDigit) },
    "1A".mightBeMultipleOf25("34A"),
    "34A".mightBeMultipleOf25("1A"),
    "34A".digitSumIs("32A"),
    "34A".digitReference(
        allDigits(),
        "13A",
        { it[1] }) { myDigit, otherDigits -> if (otherDigits.size == 1) !otherDigits.contains(myDigit) else true }
)

private val clueMap = clueMap(
    "1A" to productWithOtherIsMultipleOfOneTrillion("34A"),
    *"9A".singleReference("30A", ::digitProduct),
    *"9A".singleReference("1A", ::digitSum),
    "10A" to simpleClue { isMultipleOf(1_000_000)(digitProduct(it)) },
    "13A" to emptyClue(), // Covered by digit reducers
    "14A" to digitsSameExceptOne(10),
    "17A" to simpleClue(isSumOfTwoNthPowers(3)),
    *"18A".singleReference("16D", ::digitProduct),
    "21A" to digitsSameExceptOne(5),
    *"22A".isGreaterThan("13A"),
    *"23A".singleReference("6D", ::digitProduct),
    "25A" to simplyNot(containsDigit(0)) + simpleClue { digitSum(it) > digitProduct(it) },
    "26A" to simpleClue { it == BigInteger.TWO.pow(it.toInt()).digits().takeLast(3).fromDigits() },
    *"27A".singleReference("23A") { digitProduct(it) + 1 },
    "28A" to simpleClue(isMultipleOf(9)),
    *"30A".singleReference("27A") { digitProduct(it - 1) },
    *"31A".transformedEquals("7D", ::digitProduct),
    *"32A".singleReference("34A", ::digitSum),
    "34A" to productWithOtherIsMultipleOfOneTrillion("1A"),

    "2D" to simpleClue { it == sumOfCubesOfDigits(it) },
    "3D" to simpleClue { digitProduct(it).digits().size == 12 },
    *"4D".singleReference("5D", ::sumOfCubesOfDigits),
    *"5D".singleReference("4D", ::sumOfCubesOfDigits),
    *"5D".isNotEqualTo("4D"),
    *"6D".singleReference("18A", ::digitProduct),
    "7D" to simpleClue(::isTriangleNumber) + simpleClue(hasDigitSum(18)),
    *"8D".singleReferenceFlattened("13A", generatePowers(16)),
    *"10D".singleReferenceFlattened("22A", generatePowers(14)),
    "11D" to simpleClue { it == sumOfCubesOfDigits(it) },
    *"12D".singleReferenceFlattened("13A", generatePowers(14)),
    *"15D".singleReference("28A") { it.lastNDigits(8) },
    *"16D".singleReference("3D", ::digitProduct),
    "19D" to simpleClue { !containsDigit(0)(it) && isMultipleOf(9)(digitProduct(it)) },
    "20D" to hasDigitRelationship { (a, b) -> isMultipleOf(b.toLong())(a.toLong()) },
    "24D" to simpleClue { it == sumOfNthPowerOfDigits(4)(it) },
    "29D" to simpleClue { it == sumOfNthPowerOfDigits(5)(it) },
    *"33D".singleReference("9A", ::digitProduct)
)

val CROSSNUMBER_7 = factoryCrossnumber(grid, clueMap, digitReducers, skipSymmetryCheck = true)

/**
 * 1A and 34A's product are a multiple of 1,000,000,000,000 = 2^12 * 5^12
 *
 * If one of them is odd, then the other must divide by 2^12
 * If one of them doesn't end with a 5 or 0, then the other must divide by 5^12
 */
private class ProductWithOtherIsMultipleOfOneTrillion(private val other: ClueId, crossnumber: Crossnumber) :
    ContextualClue(crossnumber) {
    private val divisor = computeRequiredDivisor()

    override fun check(value: Long) = divisor?.let { isMultipleOf(divisor)(value) } ?: true

    private fun computeRequiredDivisor(): Long? {
        val otherEndDigit = crossnumber.solutions.getValue(other).squares.last()
        val otherDigitOptions = crossnumber.digitMap.getValue(otherEndDigit)
        return if (!otherDigitOptions.contains(0) && !otherDigitOptions.contains(5)) {
            5.pow(12)
        } else if (otherDigitOptions.all { isOdd(it.toLong()) }) {
            2.pow(12)
        } else {
            null
        }
    }
}

private fun productWithOtherIsMultipleOfOneTrillion(other: String): ClueConstructor = { crossnumber ->
    ProductWithOtherIsMultipleOfOneTrillion(ClueId.fromString(other), crossnumber)
}

/**
 * Similar to above, if we find one of 1A/34A aren't a multiple of 5, then the other must be a multiple of 5^12.
 * In particular this means it's a multiple of 25, so its second-to-last digit has to be one of 0, 2, 5 or 7.
 */
private fun String.mightBeMultipleOf25(other: String) = digitReference(
    { it.takeLast(2).take(1) },
    other,
    lastDigit()
) { myDigit, otherDigits -> if (!otherDigits.contains(5)) listOf(0, 2, 5, 7).contains(myDigit) else true }

/**
 * The digit product of 31A gives 7D. When some digits of 31A are known and 7D is narrowed, we can rule out lots of
 * digit options for 31A
 */
private class DigitProductIs(clueId: ClueId, otherClue: ClueId, crossnumber: Crossnumber) :
    AbstractDigitReducer(clueId, allDigits(), crossnumber) {
    private val otherAnswers = crossnumber.lookupAnswers(otherClue)

    override fun apply(digitMap: DigitMap): DigitMap {
        if (otherAnswers == null) {
            return digitMap
        }

        val (knownSquares, unknownSquares) = squares.partition { digitMap.getValue(it).size == 1 }
        val knownDivisor = knownSquares.map { digitMap.getValue(it).first() }.product()
        val relevantAnswers = otherAnswers.filter(isMultipleOf(knownDivisor)).map { it / knownDivisor }

        val updatedPairs = unknownSquares.map { unknownSquare ->
            val newOptions = digitMap.getValue(unknownSquare)
                .filter { relevantAnswers.any { answer -> isMultipleOf(it.toLong())(answer) } }
            unknownSquare to newOptions
        }

        return digitMap + updatedPairs
    }
}

private fun String.digitProductIs(other: String): DigitReducerConstructor = { crossnumber ->
    DigitProductIs(ClueId.fromString(this), ClueId.fromString(other), crossnumber)
}

/**
 * The digit sum of 34A gives 32A. Take the max possible value of 32A and subtract off all known digits - what's left is
 * the maximum any remaining digit of 34A can be
 */
private class DigitSumIs(clueId: ClueId, otherClue: ClueId, crossnumber: Crossnumber) :
    AbstractDigitReducer(clueId, allDigits(), crossnumber) {
    private val otherAnswers = crossnumber.lookupAnswers(otherClue)

    override fun apply(digitMap: DigitMap): DigitMap {
        if (otherAnswers == null) {
            return digitMap
        }

        val maxSum = otherAnswers.max()

        val (knownSquares, unknownSquares) = squares.partition { digitMap.getValue(it).size == 1 }
        val knownTotal = knownSquares.map { digitMap.getValue(it).first() }.sum()
        val maxDigit = maxSum - knownTotal

        val updatedPairs = unknownSquares.map { unknownSquare ->
            val newOptions = digitMap.getValue(unknownSquare).filter { it <= maxDigit }
            unknownSquare to newOptions
        }

        return digitMap + updatedPairs
    }
}

private fun String.digitSumIs(other: String): DigitReducerConstructor = { crossnumber ->
    DigitSumIs(ClueId.fromString(this), ClueId.fromString(other), crossnumber)
}