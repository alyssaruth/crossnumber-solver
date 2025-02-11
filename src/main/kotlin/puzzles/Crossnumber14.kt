package puzzles

import logging.green
import logging.printLoopBanner
import logging.red
import maths.areAnagrams
import maths.isCube
import maths.isEven
import maths.isMultipleOf
import maths.isOdd
import maths.isPalindrome
import maths.isPowerOf
import maths.isPrime
import maths.isSquare
import maths.longDigits
import solver.ClueConstructor
import solver.ClueId
import solver.Crossnumber
import solver.Orientation
import solver.PartialSolution
import solver.clue.isAnagramOf
import solver.clue.isEqualTo
import solver.clue.isLessThan
import solver.clue.makeCalculationWithReference
import solver.clue.makeSingleReference
import solver.clue.plus
import solver.clue.simpleClue
import solver.clue.singleReference
import solver.clueMap
import solver.factoryCrossnumber

/**
 * https://chalkdustmagazine.com/regulars/crossnumber/prize-crossnumber-issue-12/
 */
fun main() {
    solveCrossnumber14()
}

private val grid = """
    ..###...###..
    #.#.#.#.#.#.#
    ###...###...#
    ##...###...##
    .#.#.#.#.#.#.
    ...###...###.
    ..###...###..
    #.#.#.#.#.#.#
    ###...###...#
    ##...###...##
    .#.#.#.#.#.#.
    ...###...###.
    ..###...###..
""".trimIndent()

private val isSquare = simpleClue(::isSquare)

private val acrossClues: List<ClueConstructor> = listOf(
    isEqualTo(217),
    isEqualTo(394),
    isEqualTo(555),
    isEqualTo(777),
    isEqualTo(790),
    isEqualTo(935),
    makeCalculationWithReference("2D") { value, other -> isMultipleOf(value)(other) },
    simpleClue(isMultipleOf(51)),
    simpleClue(isPowerOf(2)),
    isSquare,
    isSquare,
    makeCalculationWithReference("25D") { x, y -> x != y && areAnagrams(x, y) },
    makeSingleReference("18D") { it },
    makeSingleReference("34D") { it },
    makeSingleReference("1A") { it - 5 },
    makeSingleReference("1A") { it - 4 },
    makeSingleReference("1A") { it - 3 },
    makeSingleReference("1A") { it - 2 },
    makeSingleReference("1A") { it - 1 },
    makeSingleReference("6D") { it / 3 },
    makeSingleReference("4D") { it * 2 }
)

private val clueMap = clueMap(
    "1A" to simpleClue { it >= 15 },
    "2D" to simpleClue(::isSquare),
    *"3D".isAnagramOf("16D"),
    *"4D".isLessThan("38D"),
    "5D" to simpleClue(isEven),
    "6D" to simpleClue(isMultipleOf(3)),
    *"6D".singleReference("12D") { it * 2 },
    *"7D".isEqualTo("5D"),
    "9D" to simpleClue(::isSquare),
    "11D" to simpleClue(::isSquare),
    *"12D".singleReference("18D") { it * 2 },
    *"13D".isEqualTo("5D"),
    *"14D".isLessThan("18D"),
    "15D" to simpleClue(isMultipleOf(23)) + simpleClue { it.longDigits().all(isOdd) },
    *"16D".singleReference("17A") { it - 1 },
    *"18D".singleReference("14D") { it * 2 },
    "20D" to simpleClue(::isPalindrome) + simpleClue(::isPrime),
    "22D" to simpleClue(isMultipleOf(23)) + simpleClue { it.longDigits().all(isOdd) },
    "23D" to simpleClue(isMultipleOf(23)) + simpleClue { it.longDigits().all(isOdd) },
    *"24D".singleReference("30D") { it * 3 },
    "25D" to simpleClue(isMultipleOf(23)) + simpleClue { it.longDigits().all(isOdd) },
    *"27D".singleReference("21A") { it * it },
    "29D" to simpleClue(isMultipleOf(25)) + simpleClue { it.longDigits().all(isOdd) },
    *"30D".singleReference("32D") { it * 3 },
    *"31D".singleReference("3A") { it + 1 },
    *"32D".isLessThan("30D"),
    "33D" to simpleClue(isMultipleOf(23)) + simpleClue { it.longDigits().all(isOdd) },
    "34D" to simpleClue(::isCube),
    *"36D".singleReference("2D") { it - 10 },
    "38D" to simpleClue(isMultipleOf(5))
)

val CROSSNUMBER_14 = factoryCrossnumber(grid, clueMap, skipSymmetryCheck = true)

data class SolveState(
    val crossnumber: Crossnumber,
    val unplacedAcrossClues: List<ClueConstructor>,
    val matchedSolutions: List<ClueId>
)

fun solveCrossnumber14() = solve()

private tailrec fun solve(
    solveState: SolveState = SolveState(CROSSNUMBER_14, acrossClues, emptyList()),
    pass: Int = 1
): Crossnumber {
    solveState.crossnumber.printLoopBanner(pass)
    val newSolveState = solveState.copy(crossnumber = solveState.crossnumber.solve(log = false))
    if (newSolveState.crossnumber.isSolved()) {
        return newSolveState.crossnumber
    }

    println(newSolveState.crossnumber.completionString())
    println("----------------------")
    println(newSolveState.crossnumber.substituteKnownDigits().prettyString())
    println("----------------------")
    println("Attempting to match up across clues...")

    val nextSolveState = newSolveState.placeCluesWithOnlyOneSolution().placeSolutionsWithOnlyOneClue()
    if (newSolveState == nextSolveState) {
        println("Failed to place any across clues, ${newSolveState.unplacedAcrossClues.size} still left :(".red())
        return newSolveState.crossnumber
    }

    return solve(nextSolveState, pass + 1)
}

private fun SolveState.placeCluesWithOnlyOneSolution(): SolveState =
    unplacedAcrossClues.fold(this) { currentState, clueConstructor ->
        val currentCrossnumber = currentState.crossnumber
        val clue = clueConstructor(currentCrossnumber)
        val viableSolutions =
            getAcrossSolutionsWithNoClues(currentState).filter { it.value.possibilities.any(clue::check) }
        if (viableSolutions.size > 1) {
            currentState
        } else {
            val (clueId, cluelessSolution) = viableSolutions.entries.first()
            currentState.matchClueWithSolution(clueId, cluelessSolution, clueConstructor)
        }
    }

private fun SolveState.placeSolutionsWithOnlyOneClue(): SolveState =
    getAcrossSolutionsWithNoClues(this).entries.fold(this) { currentState, (clueId, soln) ->
        val viableClues =
            currentState.unplacedAcrossClues.filter { soln.possibilities.any(it(currentState.crossnumber)::check) }
                .distinct()
        if (viableClues.size > 1) {
            currentState
        } else {
            val clue = viableClues.first()
            currentState.matchClueWithSolution(clueId, soln, clue)
        }
    }

private fun SolveState.matchClueWithSolution(
    clueId: ClueId,
    solution: PartialSolution,
    clueConstructor: ClueConstructor
): SolveState {
    val newCrossnumber = crossnumber.replaceSolution(clueId, solution.copy(clue = clueConstructor))
    val newUnplacedClues = unplacedAcrossClues - clueConstructor
    val newMatchedClueIds = matchedSolutions + clueId
    println("${clueConstructor(newCrossnumber).javaClass.simpleName} -> $clueId".green())
    return SolveState(newCrossnumber, newUnplacedClues, newMatchedClueIds)
}

private fun getAcrossSolutionsWithNoClues(state: SolveState) =
    state.crossnumber
        .partialSolutions()
        .filterKeys { it.orientation == Orientation.ACROSS && !state.matchedSolutions.contains(it) }


