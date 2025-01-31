package solver.clue

import maths.allCombinations
import solver.ClueConstructor
import solver.ClueId
import solver.Crossnumber
import solver.RAM_THRESHOLD
import java.math.BigInteger

/**
 * For clues like:
 *
 *  - The sum of 6D, 8D, 31D, 37D and 43D
 */
open class MultiReferenceClue(
    crossnumber: Crossnumber,
    protected val clues: List<ClueId>,
    private val combiner: (List<Long>) -> Long
) :
    ContextualClue(crossnumber) {
    private val potentialSolutions = computePotentialSolutions()

    override fun check(value: Long) = potentialSolutions?.contains(value) ?: true

    override fun totalCombinations(solutionCombos: Long) = solutionCombos

    private fun computePotentialSolutions(): Set<Long>? {
        val answers = clues.map(::lookupAnswers)
        if (answers.any { it == null }) {
            return null
        }

        val nonNullAnswers = answers.filterNotNull()
        val totalSize =
            nonNullAnswers.map { it.size.toBigInteger() }.fold(BigInteger.ONE) { product, n -> product.times(n) }

        if (totalSize > RAM_THRESHOLD.toBigInteger()) {
            return null
        }

        return nonNullAnswers.allCombinations().map(combiner).toSet()
    }
}

fun multiReference(
    vararg clues: String,
    combiner: (List<Long>) -> Long
): ClueConstructor =
    { crossnumber ->
        MultiReferenceClue(
            crossnumber,
            clues.map(ClueId::fromString),
            combiner
        )
    }