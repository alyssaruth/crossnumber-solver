package solver.clue

import solver.ClueConstructor

class MinimumClue(private val clue: BaseClue) : BaseClue() {
    override val filterFunction: (possibles: List<Long>, predicate: (Long) -> Boolean) -> List<Long> =
        { possibles, predicate -> listOf(possibles.first(predicate)) }

    override fun check(value: Long) = clue.check(value)

    override fun totalCombinations(solutionCombos: Long) = clue.totalCombinations(solutionCombos)
}

fun minimumOf(other: ClueConstructor): ClueConstructor = { crossnumber -> MinimumClue(other(crossnumber)) }
