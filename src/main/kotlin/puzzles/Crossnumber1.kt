package puzzles

import kotlinx.datetime.Instant
import maths.containsDigit
import maths.digitCounts
import maths.digits
import maths.hasDigitSum
import maths.hasUniqueDigits
import maths.isFibonacci
import maths.isMultipleOf
import maths.isPalindrome
import maths.isPerfect
import maths.isPrime
import maths.isSquare
import maths.isTriangleNumber
import maths.nextPrime
import maths.primeFactors
import maths.primesUpTo
import maths.product
import maths.reversed
import maths.toRomanNumerals
import solver.ClueConstructor
import solver.ClueId
import solver.clue.ContextualClue
import solver.Crossnumber
import solver.Orientation
import solver.PartialSolution
import solver.PendingSolution
import solver.RAM_THRESHOLD
import solver.clue.dualReference
import solver.clue.isEqualTo
import solver.factoryCrossnumber
import solver.clue.plus
import solver.clue.simpleClue
import solver.clue.calculationWithReference
import solver.clue.singleReference
import solver.clue.tripleReference
import kotlin.math.abs

/**
 * https://chalkdustmagazine.com/regulars/100-prize-crossnumber-issue-01/
 */
fun main() {
    CROSSNUMBER_1.solve()
}

private val grid = """
        #..........####
        #.#.##.###.#...
        #..........#.#.
        ###.#.####.....
        .....#.....#.#.
        .#.#.#..###..#.
        .#.#.##.#..#...
        .#.#.......#.#.
        ...#..#.##.#.#.
        .#..###..#.#.#.
        .#.#.....#.....
        .....####.#.###
        .#.#..........#
        ...#.###.##.#.#
        ####..........#
    """.trimIndent()

private val consecutivePrimeSums = primesUpTo(999).windowed(7).map { it.sum() }

private val a32Options = (7..20).map { "5331005655".toLong(it) }.toSet()

private val clueMap: Map<String, ClueConstructor> = mapOf(
    "1A" to dualReference("4D", "18D", Long::times),
    "5A" to simpleClue(isMultipleOf(101)),
    "7A" to dualReference("10D", "11D") { a, b -> abs(a - b) },
    "9A" to simpleClue { value -> isPalindrome(value) && containsDigit(0)(value) },
    "10A" to singleReference("24A") { other -> 100000 - (other * other.reversed()) },
    "13A" to tripleReference("35A", "8D", "17A") { a35, d8, a17 -> (a35 - d8) * a17 },
    "15A" to calculationWithReference("13D") { value, other -> isPerfect(value * other) },
    "16A" to simpleClue { n: Long -> n.primeFactors().size == 2 },
    "17A" to simpleClue(::isTriangleNumber),
    "19A" to calculationWithReference("6D") { value, other -> other % value == 0L },
    "20A" to singleReference("30A") { it + 5134240 },
    "22A" to simpleClue { value -> consecutivePrimeSums.contains(value) },
    "23A" to simpleClue { toRomanNumerals(it).toCharArray().sorted().joinToString("") == "ILXXX" },
    "24A" to isEqualTo(733626510400L.primeFactors().max()),
    "25A" to simpleClue(::isSquare),
    "27A" to singleReference("7A") { it.digits().product() },
    "28A" to simpleClue(isMultipleOf(107)),
    "30A" to isEqualTo(Instant.parse("1970-01-02T01:29:41+00:00").epochSeconds),
    "32A" to simpleClue { a32Options.contains(it) },
    "35A" to simpleClue { value -> value == 1 + (3 * value.reversed()) },
    "36A" to simpleClue { value -> value.digitCounts().let { it.size == 2 && it.values.contains(1) } },

    "1D" to singleReference("3D") { it - 700 },
    "2D" to simpleClue(hasDigitSum(16)),
    "3D" to simpleClue(::isFibonacci) + singleReference("1D") { it + 700 },
    "4D" to ::FourDown,
    "5D" to simpleClue { value -> isSquare(value) && hasUniqueDigits(10)(value) },
    "6D" to simpleClue(::sixDown),
    "8D" to singleReference("25A") { nextPrime(it) },
    "10D" to simpleClue { n -> n > 9990000000 && isPrime(n) && nextPrime(n).toString().length > 10 },
    "11D" to simpleClue(isMultipleOf(396533)),
    "12D" to simpleClue { 3 * "1$it".toLong() == "${it}1".toLong() },
    "13D" to calculationWithReference("15A") { value, other -> isPerfect(value * other) },
    "14D" to ::FourteenDown,
    "17D" to isEqualTo(42),
    "18D" to simpleClue(isMultipleOf(5)) + dualReference("1A", "4D", Long::div),
    "21D" to ::TwentyOneDown,
    "26D" to isEqualTo(4 + 8 + 6 + 20 + 12),
    "27D" to singleReference("29D") { it + 2 },
    "29D" to simpleClue { it.toString().first() == it.toString().last() },
    "31D" to calculationWithReference("24A") { value, other -> value % other == 0L },
    "33D" to simpleClue { hasUniqueDigits(3)(it) && it.digits().map(Int::toLong).all { it > 0 && isSquare(it) } },
    "34D" to simpleClue(::isSquare)
)

val CROSSNUMBER_1 = factoryCrossnumber(grid, clueMap)

/**
 * This number’s first digit tells you how many 0s are in this number, the second digit how many 1s, the third digit how many 2s, and so on
 */
private fun sixDown(value: Long): Boolean {
    val digits = value.digits()
    return digits.indices.all { i -> digits.count { it == i } == digits[i] }
}

/**
 * This is the same as another number in the crossnumber
 */
class FourDown(crossnumber: Crossnumber) : ContextualClue(crossnumber) {
    private val id = ClueId(4, Orientation.DOWN)
    private val myLength = crossnumber.solutions.getValue(id).squares.size

    private val potentialSolutions = computePotentialSolutions()

    override fun totalCombinations(solutionCombos: Long) = solutionCombos

    override fun check(value: Long) = potentialSolutions?.contains(value) ?: true

    override val onSolve: ((Long) -> Crossnumber) = { solution ->
        val clues =
            (crossnumber.solutions - id).filterValues { it is PartialSolution && it.possibilities.contains(solution) }
        if (clues.size == 1) {
            val (clueId, currentSolution) = clues.toList().first()
            crossnumber.replaceSolution(
                clueId,
                PartialSolution(currentSolution.squares, currentSolution.clue, listOf(solution))
            )
        } else {
            crossnumber
        }
    }

    private fun computePotentialSolutions(): Set<Long>? {
        val potentialSolutions =
            crossnumber.solutions.filterNot { (clueId, _) -> clueId == id }.values.filter { it.squares.size == myLength }

        val partialSolutions = potentialSolutions.filterIsInstance<PartialSolution>()
        if (partialSolutions.size < potentialSolutions.size) {
            // At least one still pending, can't tell anything
            return null
        }

        return partialSolutions.flatMap { it.possibilities }.toSet()
    }
}

/**
 * The factorial of 17D divided by the factorial of 16A
 */
class FourteenDown(crossnumber: Crossnumber) : ContextualClue(crossnumber) {
    private val d17s = lookupAnswers(ClueId(17, Orientation.DOWN))
    private val a16s = lookupAnswers(ClueId(16, Orientation.ACROSS))

    override fun totalCombinations(solutionCombos: Long) = solutionCombos

    override val onSolve = { solution: Long ->
        val d17 = lookupAnswer(ClueId(17, Orientation.DOWN))
        if (d17 != null) {
            val a16id = ClueId(16, Orientation.ACROSS)
            val a16 = findSixteenAcross(solution, d17)
            val existingSolution = crossnumber.solutions.getValue(a16id)
            crossnumber.replaceSolution(
                a16id,
                PartialSolution(existingSolution.squares, existingSolution.clue, listOf(a16))
            )
        } else {
            crossnumber
        }
    }

    private tailrec fun findSixteenAcross(
        currentValue: Long,
        currentDivisor: Long
    ): Long {
        val divisionResult = currentValue / currentDivisor
        return if (a16s!!.contains(divisionResult - 1)) {
            divisionResult - 1
        } else {
            findSixteenAcross(divisionResult, currentDivisor - 1)
        }
    }

    override fun check(value: Long): Boolean {
        if (d17s == null || a16s == null) {
            return true
        }

        val size = d17s.size * a16s.size
        if (size > RAM_THRESHOLD) {
            return true
        }

        val possibles =
            d17s.flatMap { d17 ->
                a16s.mapNotNull { a16 ->
                    if (d17 <= a16) null else ((a16 + 1)..d17).fold(1L, Long::times)
                }
            }.toSet()

        return possibles.contains(value)
    }
}

/**
 * The number of the D clue which has the answer 91199
 */
class TwentyOneDown(crossnumber: Crossnumber) : ContextualClue(crossnumber) {
    private val candidates = calculatePossibilities()

    override fun totalCombinations(solutionCombos: Long) = solutionCombos

    override val onSolve = { solution: Long ->
        val clueId = ClueId(solution.toInt(), Orientation.DOWN)
        val existingSolution = crossnumber.solutions.getValue(clueId)
        val newSolution = PartialSolution(existingSolution.squares, existingSolution.clue, listOf(91199))
        crossnumber.replaceSolution(clueId, newSolution)
    }

    private fun calculatePossibilities(): List<Long> {
        val cluesOfRightLength =
            crossnumber.solutions.filter { it.key.orientation == Orientation.DOWN && it.value.squares.size == 5 }
        val viableClues =
            cluesOfRightLength.filter { (_, value) -> (value is PartialSolution && value.possibilities.contains(91199)) || value is PendingSolution }
        return viableClues.keys.map { it.number.toLong() }
    }

    override fun check(value: Long) = candidates.contains(value)
}