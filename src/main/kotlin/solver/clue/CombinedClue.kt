package solver.clue

import solver.ClueConstructor
import solver.Crossnumber

class CombinedClue(clueOne: BaseClue, clueTwo: BaseClue) :
    BaseClue() {

    private val clueList = listOf(clueOne, clueTwo).sortedBy { it.totalCombinations(2) }

    override fun check(value: Long) = clueList.all { it.check(value) }

    override val onSolve: ((Long, Crossnumber) -> Crossnumber) = { solution, crossnumber ->
        clueList.fold(crossnumber) { currentCrossnumber, clue ->
            clue.onSolve?.invoke(solution, currentCrossnumber) ?: crossnumber
        }
    }

    override fun attemptCheck(solutionCombos: Long, crossnumber: Crossnumber, value: Long) =
        clueList.all { it.attemptCheck(solutionCombos, crossnumber, value) }

    override fun totalCombinations(solutionCombos: Long) = clueList.minOf { it.totalCombinations(solutionCombos) }
}

operator fun ClueConstructor.plus(other: ClueConstructor): ClueConstructor = { crossnumber ->
    CombinedClue(this(crossnumber), other(crossnumber))
}