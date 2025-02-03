package solver.clue

import solver.ClueConstructor
import solver.Crossnumber

class CombinedClue(private val clueOne: BaseClue, private val clueTwo: BaseClue) :
    BaseClue() {
    override fun check(value: Long) = clueOne.check(value) && clueTwo.check(value)

    override val onSolve: ((Long, Crossnumber) -> Crossnumber) = { solution, crossnumber ->
        val oneResult = clueOne.onSolve?.invoke(solution, crossnumber) ?: crossnumber
        val twoResult = clueTwo.onSolve?.invoke(solution, oneResult) ?: oneResult
        twoResult
    }

    override fun attemptCheck(solutionCombos: Long, crossnumber: Crossnumber, value: Long): Boolean {
        return clueOne.attemptCheck(solutionCombos, crossnumber, value) && clueTwo.attemptCheck(
            solutionCombos,
            crossnumber,
            value
        )
    }

    override fun totalCombinations(solutionCombos: Long) =
        minOf(clueOne.totalCombinations(solutionCombos), clueTwo.totalCombinations(solutionCombos))
}

operator fun ClueConstructor.plus(other: ClueConstructor): ClueConstructor = { crossnumber ->
    CombinedClue(this(crossnumber), other(crossnumber))
}