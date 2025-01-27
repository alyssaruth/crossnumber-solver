package solver.clue

import solver.ClueConstructor

class CombinedClue(private val clueOne: BaseClue, private val clueTwo: BaseClue) : BaseClue() {
    override fun check(value: Long) = clueOne.check(value) && clueTwo.check(value)

    override fun totalCombinations(solutionCombos: Long) =
        maxOf(clueOne.totalCombinations(solutionCombos), clueTwo.totalCombinations(solutionCombos))
}

operator fun ClueConstructor.plus(other: ClueConstructor): ClueConstructor = { crossnumber ->
    CombinedClue(this(crossnumber), other(crossnumber))
}