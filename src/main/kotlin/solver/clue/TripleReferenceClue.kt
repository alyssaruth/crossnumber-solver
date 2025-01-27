package solver.clue

import solver.ClueConstructor
import solver.ClueId
import solver.Crossnumber
import solver.RAM_THRESHOLD

class TripleReferenceClue(
    crossnumber: Crossnumber,
    private val clueA: ClueId,
    private val clueB: ClueId,
    private val clueC: ClueId,
    private val combiner: (Long, Long, Long) -> Long
) :
    ContextualClue(crossnumber) {
    private val potentialSolutions = computePotentialSolutions()

    override fun check(value: Long) = potentialSolutions?.contains(value) ?: true

    override fun totalCombinations(solutionCombos: Long) = solutionCombos

    private fun computePotentialSolutions(): Set<Long>? {
        val aValues = lookupAnswers(clueA) ?: return null
        val bValues = lookupAnswers(clueB) ?: return null
        val cValues = lookupAnswers(clueC) ?: return null

        val totalSize =
            aValues.size.toBigInteger().times(bValues.size.toBigInteger().times(cValues.size.toBigInteger()))
        if (totalSize > RAM_THRESHOLD.toBigInteger()) {
            return null
        }

        return aValues.flatMap { a -> bValues.flatMap { b -> cValues.map { c -> combiner(a, b, c) } } }.toSet()
    }
}

fun tripleReference(
    clueA: String,
    clueB: String,
    clueC: String,
    combiner: (Long, Long, Long) -> Long
): ClueConstructor =
    { crossnumber ->
        TripleReferenceClue(
            crossnumber,
            ClueId.fromString(clueA),
            ClueId.fromString(clueB),
            ClueId.fromString(clueC),
            combiner
        )
    }