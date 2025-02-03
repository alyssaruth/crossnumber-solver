package solver.clue

import solver.ClueId
import solver.Crossnumber
import solver.PartialSolution

abstract class ContextualClue(protected val crossnumber: Crossnumber) : BaseClue() {
    protected fun lookupAnswer(clueId: ClueId): Long? {
        val solution = crossnumber.solutions.getValue(clueId)
        return if (solution is PartialSolution && solution.isSolved()) {
            solution.possibilities.first()
        } else null
    }

    protected fun lookupAnswers(clueId: ClueId): Set<Long>? {
        val solution = crossnumber.solutions.getValue(clueId)
        return if (solution is PartialSolution) solution.possibilities.toSet() else null
    }
}
