package solver

abstract class BaseClue {
    open val onSolve: ((Long) -> Crossnumber)? = null

    abstract fun check(value: Long): Boolean
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
