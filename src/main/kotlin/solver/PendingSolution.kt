package solver

import maths.product

// Limits to prevent OOMs or slowness due to doomed loops
const val EXTREME_LOOP_THRESHOLD = 5_000_000_000
const val LOOP_THRESHOLD = 100_000_000L
const val RAM_THRESHOLD = 6_000_000

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

    override fun possibilityCount(digitMap: Map<Point, List<Int>>) = computePossibilities(squares, digitMap)

    override fun iterate(clueId: ClueId, crossnumber: Crossnumber): Crossnumber {
        val digitMap = crossnumber.digitMap
        val possibilityCount = possibilityCount(digitMap)
        if (possibilityCount > crossnumber.loopThreshold) {
            // Not narrowed down enough yet, do nothing
            return crossnumber.replaceSolution(clueId, PendingSolution(squares, clue, possibilityCount))
        }

        val digitList = squares.map(digitMap::getValue)
        val possibilities = attemptToComputePossibilities(clue(crossnumber), digitList, possibilityCount, crossnumber)
            ?: return crossnumber.replaceSolution(clueId, PendingSolution(squares, clue, possibilityCount))

        return PartialSolution(squares, clue, possibilities).iterate(clueId, crossnumber)
    }

    private tailrec fun attemptToComputePossibilities(
        clue: BaseClue,
        digitList: List<List<Int>>,
        possibilities: Long,
        crossnumber: Crossnumber,
        currentValueStr: String? = startingValue(digitList),
        possibleSoFar: MutableList<Long> = mutableListOf()
    ): List<Long>? {
        if (currentValueStr == null) {
            return possibleSoFar
        }

        if (possibleSoFar.size > RAM_THRESHOLD) {
            return null
        }

        val currentValue = currentValueStr.toLong()
        if (clue.attemptCheck(possibilities, crossnumber, currentValue)) {
            possibleSoFar.add(currentValue)
        }

        return attemptToComputePossibilities(
            clue,
            digitList,
            possibilities,
            crossnumber,
            nextValue(digitList, currentValueStr),
            possibleSoFar
        )
    }


    companion object {
        private fun computePossibilities(squares: List<Point>, digitMap: Map<Point, List<Int>>): Long {
            return squares.map { digitMap.getValue(it).size }.product()
        }

        fun nextValue(digitList: List<List<Int>>, n: String): String? {
            val indices = (digitList.size - 1 downTo 0)

            var newStr = ""
            var doneIncrement = false
            indices.forEach { ix ->
                val current = n[ix].digitToInt()
                if (doneIncrement) {
                    newStr = "$current$newStr"
                } else {
                    val possibles = digitList[ix]
                    val next = if (current == possibles.last()) {
                        possibles.first()
                    } else {
                        doneIncrement = true
                        possibles[possibles.indexOf(current) + 1]
                    }

                    newStr = "$next$newStr"
                }
            }

            return if (!doneIncrement) null else newStr
        }

        fun startingValue(digitList: List<List<Int>>): String =
            digitList.map { it.first() }.joinToString("")
    }

    override fun status() = "PENDING ($possibilities)"
    override fun isSolved() = false
}
