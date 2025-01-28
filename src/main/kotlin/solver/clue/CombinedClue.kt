package solver.clue

import solver.ClueConstructor
import solver.Crossnumber

class CombinedClue(private val clueOne: BaseClue, private val clueTwo: BaseClue) : BaseClue() {
    override fun check(value: Long) = clueOne.check(value) && clueTwo.check(value)

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