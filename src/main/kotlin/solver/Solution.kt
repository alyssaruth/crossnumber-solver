package solver

sealed interface ISolution {
    val squares: List<Point>
    val clues: List<Clue>

    fun iterate(digitMap: Map<Point, List<Int>>): Pair<ISolution, Map<Point, List<Int>>>

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
    override val clues: List<Clue>,
    private val possibilities: Long
) : ISolution {
    constructor(squares: List<Point>, clues: List<Clue>, digitMap: Map<Point, List<Int>>) : this(
        squares,
        clues,
        computePossibilities(squares, digitMap)
    )

    override fun iterate(digitMap: Map<Point, List<Int>>): Pair<ISolution, Map<Point, List<Int>>> {
        val possibilityCount = computePossibilities(squares, digitMap)
        if (possibilityCount > BRUTE_FORCE_THRESHOLD) {
            // Not narrowed down enough yet, do nothing
            return PendingSolution(squares, clues, possibilityCount) to digitMap
        }

        // Explode out
        val possibilities = squares.fold(listOf("")) { possibilities, square ->
            val digitOptions = digitMap.getValue(square)

            possibilities.flatMap { possibility ->
                digitOptions.map { nextDigit -> possibility + nextDigit }
            }
        }

        return PartialSolution(squares, clues, possibilities.map(String::toLong)).iterate(digitMap)
    }

    companion object {
        private fun computePossibilities(squares: List<Point>, digitMap: Map<Point, List<Int>>): Long {
            return squares.map { digitMap.getValue(it).size }.fold(1, Long::times)
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
    override val clues: List<Clue>,
    val possibilities: List<Long>
) : ISolution {
    override fun iterate(digitMap: Map<Point, List<Int>>): Pair<ISolution, Map<Point, List<Int>>> {
        val reduced = applyDigitMap(digitMap).applyClues()

        if (reduced.possibilities.isEmpty()) {
            throw Exception("Reduced to 0 possibilities!")
        }

        return reduced to reduced.restrictDigitMap(digitMap)
    }

    override fun status() = "${possibilities.size} possibilities"
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

        return PartialSolution(squares, clues, filtered)
    }

    /**
     * Step 2: Apply our own clues to narrow the list further
     *
     * Most of the time this will do nothing after the first run, but not always - e.g. a clue like "Is divisible by 3A"
     */
    private fun applyClues(): PartialSolution {
        val filtered = clues.fold(possibilities) { currentPossibilities, clue ->
            currentPossibilities.filter(clue)
        }

        return PartialSolution(squares, clues, filtered)
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