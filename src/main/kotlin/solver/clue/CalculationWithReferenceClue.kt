package solver.clue

import solver.ClueConstructor
import solver.ClueId
import solver.Crossnumber
import solver.PartialSolution
import solver.PendingSolution

/**
 * For clues like:
 *
 *  - Multiply this by 13D to get a perfect number.
 *  - A factor of 6D
 *  - The sum of this number’s digits is 2D
 */
class CalculationWithReferenceClue(
    crossnumber: Crossnumber,
    otherClueId: ClueId,
    private val checker: (Long, Long) -> Boolean
) : ContextualClue(crossnumber) {

    private val otherSolution = crossnumber.solutions.getValue(otherClueId)

    override fun totalCombinations(solutionCombos: Long) = when (otherSolution) {
        is PendingSolution -> solutionCombos
        is PartialSolution -> otherSolution.possibilities.size * solutionCombos
    }

    override fun check(value: Long) =
        when (otherSolution) {
            is PendingSolution -> true
            is PartialSolution -> otherSolution.possibilities.any { checker(value, it) }
        }
}

fun calculationWithReference(clueId: String, checker: (Long, Long) -> Boolean): ClueConstructor =
    { crossnumber -> CalculationWithReferenceClue(crossnumber, ClueId.fromString(clueId), checker) }