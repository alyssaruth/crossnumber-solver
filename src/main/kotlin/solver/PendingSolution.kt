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

    override fun iterate(clueId: ClueId, crossnumber: Crossnumber): Crossnumber {
        val digitMap = crossnumber.digitMap
        val possibilityCount = computePossibilities(squares, digitMap)
        if (possibilityCount > crossnumber.loopThreshold) {
            // Not narrowed down enough yet, do nothing
            return crossnumber.replaceSolution(clueId, PendingSolution(squares, clue, possibilityCount))
        }

        val possibilities = attemptToComputePossibilities(clue(crossnumber), digitMap.filterKeys(squares::contains))
            ?: return crossnumber.replaceSolution(clueId, PendingSolution(squares, clue, possibilityCount))

        return PartialSolution(squares, clue, possibilities).iterate(clueId, crossnumber)
    }

    private tailrec fun attemptToComputePossibilities(
        clue: BaseClue,
        digitMap: Map<Point, List<Int>>,
        currentValue: Long? = startingValue(squares, digitMap),
        possibleSoFar: MutableList<Long> = mutableListOf()
    ): List<Long>? {
        if (currentValue == null) {
            return possibleSoFar.toList()
        }

        if (possibleSoFar.size > RAM_THRESHOLD) {
            return null
        }

        if (clue.check(currentValue)) {
            possibleSoFar.add(currentValue)
        }

        return attemptToComputePossibilities(
            clue,
            digitMap,
            nextValue(squares, digitMap, currentValue),
            possibleSoFar
        )
    }


    companion object {
        private fun computePossibilities(squares: List<Point>, digitMap: Map<Point, List<Int>>): Long {
            return squares.map { digitMap.getValue(it).size }.product()
        }

        fun nextValue(squares: List<Point>, digitMap: Map<Point, List<Int>>, n: Long): Long? {
            val indices = (squares.size - 1 downTo 0)
            val nStr = n.toString()

            var doneIncrement = false
            val newDigits = indices.map { ix ->
                val current = nStr[ix].digitToInt()
                if (doneIncrement) {
                    current
                } else {
                    val possibles = digitMap.getValue(squares[ix])
                    val next = possibles.filter { it > current }.minOrNull()
                    if (next != null) {
                        doneIncrement = true
                        next
                    } else {
                        possibles.min()
                    }
                }
            }

            val result = newDigits.joinToString("").reversed().toLong()
            return if (!doneIncrement) null else result
        }

        fun startingValue(squares: List<Point>, digitMap: Map<Point, List<Int>>): Long =
            squares.map { digitMap.getValue(it).min() }.joinToString("").toLong()
    }

    override fun status() = "PENDING ($possibilities)"
    override fun isSolved() = false
}
