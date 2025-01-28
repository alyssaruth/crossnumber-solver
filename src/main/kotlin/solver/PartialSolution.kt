package solver

/**
 * An "in-progress" solution with a list of the current possibilities we've narrowed down to
 */
data class PartialSolution(
    override val squares: List<Point>,
    override val clue: ClueConstructor,
    val possibilities: List<Long>
) : ISolution {

    override fun possibilityCount(digitMap: Map<Point, List<Int>>) = possibilities.size.toLong()

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

    override fun status() =
        if (isSolved()) "solved! (${possibilities.first()})" else "${possibilities.size} possibilities"

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

        val filtered = constructedClue.filterFunction(possibilities) { possibility ->
            constructedClue.attemptCheck(possibilities.size.toLong(), crossnumber, possibility)
        }

        return PartialSolution(squares, clue, filtered)
    }

    /**
     * Step 3: Use our reduced solution space to narrow down the global digit map for other clues
     */
    private fun restrictDigitMap(digitMap: Map<Point, List<Int>>): Map<Point, List<Int>> {
        val possibilityStrings = possibilities.map(Long::toString)

        val updates = squares.mapIndexed { ix, square ->
            val possibleDigits = possibilityStrings.map { it[ix].digitToInt() }.distinct().sorted()
            square to possibleDigits
        }.toMap()

        return digitMap + updates
    }
}