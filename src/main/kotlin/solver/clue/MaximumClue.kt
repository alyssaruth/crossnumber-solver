package solver.clue

import solver.ClueConstructor

class MaximumClue(private val clue: BaseClue) : BaseClue() {
    override val filterFunction: (possibles: List<Long>, predicate: (Long) -> Boolean) -> List<Long> =
        { possibles, predicate -> listOf(possibles.last(predicate)) }

    override fun check(value: Long) = clue.check(value)

    override fun totalCombinations(solutionCombos: Long) = clue.totalCombinations(solutionCombos)
}

fun largest(other: ClueConstructor): ClueConstructor = { crossnumber -> MaximumClue(other(crossnumber)) }