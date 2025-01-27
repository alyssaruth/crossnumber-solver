package solver.clue

import solver.ClueConstructor
import solver.ClueId
import solver.Crossnumber
import solver.PartialSolution
import solver.PendingSolution

class SingleReferenceClue(
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

fun simpleReference(clueId: String, checker: (Long, Long) -> Boolean): ClueConstructor =
    { crossnumber -> SingleReferenceClue(crossnumber, ClueId.fromString(clueId), checker) }