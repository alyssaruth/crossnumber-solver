package puzzles

import logging.completionString
import logging.dumpFailureInfo
import maths.isMultipleOf
import maths.wholeDiv
import solver.ClueConstructor
import solver.ClueId
import solver.Crossnumber
import solver.Orientation
import solver.clue.plus
import solver.clue.singleReference
import solver.factoryCrossnumber

/**
 * https://chalkdustmagazine.com/regulars/crossnumber/prize-crossnumber-issue-21/
 */
fun solveCrossnumber21(): Crossnumber {
    val potentialClues = mangledClues.mapValues { (_, mangled) -> computeOptions(CROSSNUMBER_21, mangled) }
    return CROSSNUMBER_21.solve(potentialClues)
}

private tailrec fun Crossnumber.solve(potentialClues: Map<ClueId, List<ClueCandidate>>): Crossnumber {
    val crossnumber = this.solve(log = false)
    println(crossnumber.completionString())
    val filteredClues = filterOptions(crossnumber, potentialClues)
    val cluesToSubstitute = filteredClues.filter { it.value.size == 1 }

    return if (crossnumber.isSolved()) {
        crossnumber
    } else if (cluesToSubstitute.isEmpty()) {
        println("*****************************")
        println("Failed, left with:")
        println(filteredClues.forEach(::println))
        crossnumber.dumpFailureInfo()
        crossnumber
    } else {
        val newCrossnumber =
            cluesToSubstitute.entries.fold(crossnumber) { currentCrossnumber, (clueId, clueCandidate) ->
                substituteCandidate(currentCrossnumber, clueId, clueCandidate.first())
            }

        val newClues = filteredClues - cluesToSubstitute.keys
        println("--------------------------------------------")
        println("Substituted ${cluesToSubstitute.size} clues, ${newClues.size} remaining")
        newCrossnumber.solve(newClues)
    }
}

private fun substituteCandidate(crossnumber: Crossnumber, clueId: ClueId, candidate: ClueCandidate): Crossnumber {
    val clues = candidate.toClueConstructors(clueId)

    return clues.fold(crossnumber) { currentCrossnumber, (clueId, clueConstructor) ->
        addClue(currentCrossnumber, ClueId.fromString(clueId), clueConstructor)
    }
}

private fun addClue(crossnumber: Crossnumber, clueId: ClueId, clueConstructor: ClueConstructor): Crossnumber {
    val current = crossnumber.partialSolutions().getValue(clueId)
    val new = current.copy(clue = current.clue + clueConstructor)
    return crossnumber.replaceSolution(clueId, new)
}

private val mangledClues = mapOf(
    "1A" to "997A",
    "3A" to "81A",
    "6A" to "1D",
    "9A" to "286A",
    "10A" to "16D",
    "11A" to "2A",
    "12A" to "6A",
    "13A" to "1D",
    "16A" to "16A",
    "18A" to "360D",
    "20A" to "2A",
    "22A" to "8A",
    "24A" to "15104D",
    "27A" to "4A",
    "29A" to "40A",
    "31A" to "30A",
    "33A" to "6A",
    "35A" to "4A",
    "37A" to "668997D",
    "40A" to "34459245389A",
    "43A" to "7A",
    "45A" to "4A",
    "46A" to "8A",
    "48A" to "6A",
    "51A" to "4A",
    "53A" to "5A",
    "55A" to "14750A",
    "57A" to "4A",
    "58A" to "56D",
    "59A" to "256D",
    "60A" to "2A",
    "61A" to "4A",
    "62A" to "5A",
    "63A" to "64D",

    "1D" to "1D",
    "2D" to "6172344439D",
    "3D" to "3D",
    "4D" to "4D",
    "5D" to "81707A",
    "6D" to "3A",
    "7D" to "72D",
    "8D" to "60A",
    "14D" to "15094A",
    "15D" to "4D",
    "17D" to "5901148A",
    "19D" to "19893A",
    "21D" to "725D",
    "23D" to "23D",
    "25D" to "3038A",
    "26D" to "3661706D",
    "28D" to "1925D",
    "30D" to "1009262A",
    "31D" to "1061A",
    "32D" to "41A",
    "34D" to "7568A",
    "36D" to "11727A",
    "38D" to "6468D",
    "39D" to "705159A",
    "41D" to "2302929D",
    "42D" to "766A",
    "44D" to "584828A",
    "47D" to "47D",
    "49D" to "59921D",
    "50D" to "21D",
    "52D" to "52D",
    "54D" to "54D",
    "55D" to "98D",
    "56D" to "944D"
).mapKeys { ClueId.fromString(it.key) }

private fun computeOptions(crossnumber: Crossnumber, mangled: String): List<ClueCandidate> {
    val clueIds = crossnumber.solutions.keys
    val orientation = if (mangled.endsWith("A")) Orientation.ACROSS else Orientation.DOWN
    val relevantClueIds = clueIds.filter { it.orientation == orientation }
    val number = mangled.dropLast(1).toLong()
    return computePlusOptions(number, relevantClueIds) +
            computeMinusOptions(number, relevantClueIds) +
            computeTimesOptions(number, relevantClueIds) +
            computeDivideOptions(number, relevantClueIds)
}

private fun filterOptions(
    crossnumber: Crossnumber,
    options: Map<ClueId, List<ClueCandidate>>
): Map<ClueId, List<ClueCandidate>> {
    return options.mapValues { (clueId, rawOptions) ->
        val myClue = crossnumber.partialSolutions().getValue(clueId)
        val myLength = myClue.squares.size
        val myPossibilities = myClue.possibilities
        val filtered = rawOptions.filter { candidate ->
            val otherSolution = crossnumber.partialSolutions().getValue(candidate.other)
            val runDetailedCheck = otherSolution.possibilities.size < 10000 || rawOptions.size < 10

            val min = otherSolution.possibilities.min()
            val max = otherSolution.possibilities.max()

            val minResult = candidate.getEstimate(min)
            val maxResult = candidate.getEstimate(max)
            if (minResult < 1 && maxResult < 1) {
                false
            } else {
                val minLength = minResult.toString().length
                val maxLength = maxResult.toString().length
                (myLength in minLength..maxLength || myLength in maxLength..minLength)
                        && (!runDetailedCheck || otherSolution.possibilities.any { otherOption ->
                    myPossibilities.contains(candidate.substituteValue(otherOption))
                })
            }
        }

        if (filtered.size < rawOptions.size) {
            println("$clueId: Filtered from ${rawOptions.size} -> ${filtered.size}")
        }

        filtered
    }
}

private fun computePlusOptions(number: Long, clueIds: List<ClueId>) =
    clueIds.mapNotNull {
        val diff = number - it.number
        if (diff <= 0) null else ClueCandidate(diff, Operation.PLUS, it) // Ignore 0 + foo, it's equivalent to 1 * foo
    }

private fun computeMinusOptions(number: Long, clueIds: List<ClueId>) =
    clueIds.map { ClueCandidate(number + it.number, Operation.MINUS, it) }

private fun computeTimesOptions(number: Long, clueIds: List<ClueId>) =
    clueIds.mapNotNull {
        if (!isMultipleOf(it.number.toLong())(number)) null else ClueCandidate(
            number / it.number,
            Operation.MULTIPLY,
            it
        )
    }

private fun computeDivideOptions(number: Long, clueIds: List<ClueId>) =
    clueIds.map {
        ClueCandidate(number * it.number, Operation.DIVIDE, it)
    }

private enum class Operation(
    val desc: String,
    val estimateFn: (Long, Long) -> Long,
    val fn: (Long, Long) -> Long? = estimateFn
) {
    PLUS("+", Long::plus),
    MINUS("-", Long::minus),
    MULTIPLY("×", Long::times),
    DIVIDE("÷", Long::div, ::wholeDiv);
}

private data class ClueCandidate(val a: Long, val op: Operation, val other: ClueId) {
    override fun toString() = "$a ${op.desc} $other"

    fun getEstimate(value: Long) = op.estimateFn(a, value)

    fun substituteValue(value: Long) = op.fn(a, value)

    fun toClueConstructors(clueId: ClueId) = clueId.toString().singleReference(other.toString()) { op.fn(a, it) }
}

private val grid = """
    ...#....#.....
    .#....#...#.#.
    ...#....#.....
    .#....#....#.#
    ....#....#....
    #.#....#......
    .....#....#.#.
    .#.#....#.....
    ......#....#.#
    ....#....#....
    #.#....#....#.
    .....#....#...
    .#.#...#....#.
    .....#....#...
""".trimIndent()

val CROSSNUMBER_21 = factoryCrossnumber(grid, emptyMap(), allowGuessing = false)