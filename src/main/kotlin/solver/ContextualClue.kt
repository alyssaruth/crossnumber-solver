package solver

abstract class BaseClue {
    open val onSolve: ((Long) -> Crossnumber)? = null

    abstract fun totalCombinations(solutionCombos: Long): Long

    abstract fun check(value: Long): Boolean

    fun attemptCheck(solutionCombos: Long, crossnumber: Crossnumber, value: Long): Boolean {
        if (totalCombinations(solutionCombos) > crossnumber.loopThreshold) {
            return true
        }

        return check(value)
    }
}

abstract class ContextualClue(protected val crossnumber: Crossnumber) : BaseClue() {
    protected fun lookupAnswer(clueId: ClueId): Long? {
        val solution = crossnumber.solutions.getValue(clueId)
        return if (solution is PartialSolution && solution.isSolved()) {
            solution.possibilities.first()
        } else null
    }

    protected fun lookupAnswers(clueId: ClueId): List<Long>? {
        val solution = crossnumber.solutions.getValue(clueId)
        return if (solution is PartialSolution) solution.possibilities else null
    }
}
