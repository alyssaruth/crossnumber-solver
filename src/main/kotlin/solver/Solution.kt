package solver

import maths.product

sealed interface ISolution {
    val squares: List<Point>
    val clue: ClueConstructor

    fun iterate(clueId: ClueId, crossnumber: Crossnumber): Crossnumber

    fun status(): String

    fun isSolved(): Boolean
}

// Pretty conservative, I think - could crank higher with a larger heap
const val BRUTE_FORCE_THRESHOLD = 6_000_000

/**
 * A solution that hasn't yet been exploded out into remaining possibilities (because there are too many)
 */
data class PendingSolution(
    override val squares: List<Point>,
    override val clue: ClueConstructor,
    private val possibilities: Long
) : ISolution {
    constructor(squares: List<Point>, clue: ClueConstructor, digitMap: Map<Point, List<Int>>) : this(
        squares,
        clue,
        computePossibilities(squares, digitMap)
    )

    override fun iterate(clueId: ClueId, crossnumber: Crossnumber): Crossnumber {
        val digitMap = crossnumber.digitMap
        val possibilityCount = computePossibilities(squares, digitMap)
        if (possibilityCount > BRUTE_FORCE_THRESHOLD) {
            // Not narrowed down enough yet, do nothing
            return crossnumber.replaceSolution(clueId, PendingSolution(squares, clue, possibilityCount))
        }

        // Explode out
        val possibilities = squares.fold(listOf("")) { possibilities, square ->
            val digitOptions = digitMap.getValue(square)

            possibilities.flatMap { possibility ->
                digitOptions.map { nextDigit -> possibility + nextDigit }
            }
        }

        return PartialSolution(squares, clue, possibilities.map(String::toLong)).iterate(clueId, crossnumber)
    }

    companion object {
        private fun computePossibilities(squares: List<Point>, digitMap: Map<Point, List<Int>>): Long {
            return squares.map { digitMap.getValue(it).size }.product()
        }
    }

    override fun status() = "PENDING ($possibilities)"
    override fun isSolved() = false
}

/**
 * An "in-progress" solution with a list of the current possibilities we've narrowed down to
 */
data class PartialSolution(
    override val squares: List<Point>,
    override val clue: ClueConstructor,
    val possibilities: List<Long>
) : ISolution {
    override fun iterate(clueId: ClueId, crossnumber: Crossnumber): Crossnumber {
        val reduced = applyDigitMap(crossnumber.digitMap).applyClue(crossnumber)

        if (reduced.possibilities.isEmpty()) {
            throw Exception("Reduced to 0 possibilities!")
        }

        val restrictedDigitMap = reduced.restrictDigitMap(crossnumber.digitMap)
        val newCrossnumber = crossnumber.copy(digitMap = restrictedDigitMap).replaceSolution(clueId, reduced)

        val finalCrossnumber = if (reduced.isSolved()) {
            val solution = reduced.possibilities.first()
            clue(newCrossnumber).onSolve?.invoke(solution) ?: newCrossnumber
        } else newCrossnumber

        return finalCrossnumber
    }

    override fun status() = if (isSolved()) "solved!" else "${possibilities.size} possibilities"
    override fun isSolved() = possibilities.size == 1

    /**
     * Step 1: Narrow down our possibilities based on the current digit map
     *
     * (e.g. perhaps another clue has restricted some digits)
     */
    private fun applyDigitMap(digitMap: Map<Point, List<Int>>): PartialSolution {
        val digitValidator = squares.map(digitMap::getValue)

        val filtered = possibilities.filter { possibility ->
            val validated = possibility.toString().mapIndexed { ix, digit ->
                digitValidator[ix].contains(digit.digitToInt())
            }

            !validated.contains(false)
        }

        return PartialSolution(squares, clue, filtered)
    }

    /**
     * Step 2: Apply our own clues to narrow the list further
     *
     * Most of the time this will do nothing after the first run, but not always - e.g. a clue like "Is divisible by 3A"
     */
    private fun applyClue(crossnumber: Crossnumber): PartialSolution {
        val constructedClue = clue(crossnumber)
        val filtered = possibilities.filter { possibility ->
            constructedClue.check(possibility)
        }

        return PartialSolution(squares, clue, filtered)
    }

    /**
     * Step 3: Use our reduced solution space to narrow down the global digit map for other clues
     */
    private fun restrictDigitMap(digitMap: Map<Point, List<Int>>): Map<Point, List<Int>> {
        val possibilityStrings = possibilities.map(Long::toString)

        val updates = squares.mapIndexed { ix, square ->
            val possibleDigits = possibilityStrings.map { it[ix].digitToInt() }.distinct()
            square to possibleDigits
        }.toMap()

        return digitMap + updates
    }

}