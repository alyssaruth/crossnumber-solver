package solver.clue

import solver.ClueConstructor
import solver.ClueId
import solver.Crossnumber
import solver.RAM_THRESHOLD

/**
 * For clues like:
 *
 *  - D4 multiplied by D18
 *  - The difference between 10D and 11D
 */
class DualReferenceClue(
    crossnumber: Crossnumber,
    private val clueA: ClueId,
    private val clueB: ClueId,
    private val combiner: (Long, Long) -> Long
) :
    ContextualClue(crossnumber) {
    private val potentialSolutions = computePotentialSolutions()

    override fun check(value: Long) = potentialSolutions?.contains(value) ?: true

    override fun totalCombinations(solutionCombos: Long) = solutionCombos

    private fun computePotentialSolutions(): Set<Long>? {
        val aValues = lookupAnswers(clueA) ?: return null
        val bValues = lookupAnswers(clueB) ?: return null

        val totalSize = aValues.size.toBigInteger().times(bValues.size.toBigInteger())
        if (totalSize > RAM_THRESHOLD.toBigInteger()) {
            return null
        }

        return aValues.flatMap { a -> bValues.map { b -> combiner(a, b) } }.toSet()
    }
}

fun dualReference(clueA: String, clueB: String, combiner: (Long, Long) -> Long): ClueConstructor =
    { crossnumber -> DualReferenceClue(crossnumber, ClueId.fromString(clueA), ClueId.fromString(clueB), combiner) }