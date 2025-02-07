package solver.clue

import maths.tryAllCombinations
import solver.ClueConstructor
import solver.ClueId
import solver.Crossnumber

/**
 * For clues like:
 *
 *  - The sum of this number, 23A and 30D is greater than 1,000,000,000
 *
 * N.B. This will scale like O(a*b*c*...)
 */
class CalculationWithReferencesClue(
    crossnumber: Crossnumber,
    otherClueIds: List<ClueId>,
    private val checker: (List<Long>) -> Boolean
) : ContextualClue(crossnumber) {

    private val otherCombos = prepareCombos(otherClueIds)

    override fun totalCombinations(solutionCombos: Long) = solutionCombos * (otherCombos?.size ?: 1)

    override fun check(value: Long) = otherCombos?.any { checker(listOf(value) + it) } ?: true

    private fun prepareCombos(otherClueIds: List<ClueId>): List<List<Long>>? {
        val answers = otherClueIds.map(::lookupAnswers)
        if (answers.any { it == null }) {
            return null
        }

        return answers.filterNotNull().tryAllCombinations()
    }
}

fun makeCalculationWithReferences(vararg clueIds: String, checker: (List<Long>) -> Boolean): ClueConstructor =
    { crossnumber -> CalculationWithReferencesClue(crossnumber, clueIds.map(ClueId::fromString), checker) }
