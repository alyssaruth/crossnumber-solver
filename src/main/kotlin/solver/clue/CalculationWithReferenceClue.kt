package solver.clue

import maths.isMultipleOf
import solver.ClueConstructor
import solver.ClueId
import solver.Crossnumber

/**
 * For clues like:
 *
 *  - Multiply this by 13D to get a perfect number.
 *  - A factor of 6D
 *  - The sum of this number’s digits is 2D
 *
 * N.B. This will scale like O(n*m) - use "transformedEqualsRef" or "singleRef" instead where possible
 */
class CalculationWithReferenceClue(
    crossnumber: Crossnumber,
    otherClueId: ClueId,
    private val checker: (Long, Long) -> Boolean
) : ContextualClue(crossnumber) {

    private val otherAnswers = lookupAnswers(otherClueId)

    override fun totalCombinations(solutionCombos: Long) = solutionCombos * (otherAnswers?.size ?: 1)

    override fun check(value: Long) = otherAnswers?.any { checker(value, it) } ?: true
}

fun calculationWithReference(clueId: String, checker: (Long, Long) -> Boolean): ClueConstructor =
    { crossnumber -> CalculationWithReferenceClue(crossnumber, ClueId.fromString(clueId), checker) }

fun isMultipleOfRef(clueId: String) = calculationWithReference(clueId) { value, other -> isMultipleOf(other)(value) }
fun isFactorOfRef(clueId: String) = calculationWithReference(clueId) { value, other -> isMultipleOf(value)(other) }

fun doesNotEqualRef(clueId: String) = calculationWithReference(clueId) { x, y -> x != y }