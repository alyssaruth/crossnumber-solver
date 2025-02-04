package solver.clue

import solver.Crossnumber

abstract class BaseClue {
    open val filterFunction: (possibles: List<Long>, predicate: (Long) -> Boolean) -> List<Long> =
        { possibles, predicate -> possibles.filter(predicate) }

    open fun knownPossibilities(): List<Long>? = null

    open fun totalCombinations(solutionCombos: Long) = solutionCombos

    abstract fun check(value: Long): Boolean

    open fun attemptCheck(solutionCombos: Long, crossnumber: Crossnumber, value: Long): Boolean {
        if (totalCombinations(solutionCombos) > crossnumber.loopThreshold) {
            return true
        }

        return check(value)
    }
}