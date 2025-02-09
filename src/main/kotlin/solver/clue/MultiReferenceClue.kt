package solver.clue

import maths.tryAllCombinations
import solver.ClueConstructor
import solver.ClueId
import solver.Crossnumber

/**
 * For clues like:
 *
 *  - The sum of 6D, 8D, 31D, 37D and 43D
 */
open class MultiReferenceClue(
    crossnumber: Crossnumber,
    protected val clues: List<ClueId>,
    private val combiner: (List<Long>) -> Long?
) : ComputedPossibilitiesClue(crossnumber) {
    override val possibilities = computePotentialSolutions()

    private fun computePotentialSolutions(): Set<Long>? {
        val answers = clues.map(::lookupAnswers)
        if (answers.any { it == null }) {
            return null
        }

        val allCombinations = answers.filterNotNull().tryAllCombinations() ?: return null
        return allCombinations.mapNotNull(combiner).toSet()
    }
}

fun dualReference(clueA: String, clueB: String, combiner: (Long, Long) -> Long?) =
    multiReference(clueA, clueB) { (a, b) -> combiner(a, b) }

fun multiReference(
    vararg clues: String,
    combiner: (List<Long>) -> Long?
): ClueConstructor =
    { crossnumber ->
        MultiReferenceClue(
            crossnumber,
            clues.map(ClueId::fromString),
            combiner
        )
    }