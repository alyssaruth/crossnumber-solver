package solver

class SingleReferenceEqualityClue(
    crossnumber: Crossnumber,
    private val other: ClueId,
    private val mapper: (Long) -> Long
) :
    ContextualClue(crossnumber) {
    private val potentialSolutions = computePotentialSolutions()

    override fun check(value: Long) = potentialSolutions?.contains(value) ?: true

    override fun totalCombinations(solutionCombos: Long) = solutionCombos

    private fun computePotentialSolutions(): Set<Long>? {
        val values = lookupAnswers(other) ?: return null

        return values.map(mapper).toSet()
    }
}

fun singleReferenceEquals(clue: String, mapper: (Long) -> Long): ClueConstructor =
    { crossnumber -> SingleReferenceEqualityClue(crossnumber, ClueId.fromString(clue), mapper) }