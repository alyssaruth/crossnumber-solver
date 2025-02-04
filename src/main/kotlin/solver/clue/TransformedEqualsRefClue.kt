package solver.clue

import solver.ClueConstructor
import solver.ClueId
import solver.Crossnumber

/**
 * For clues like:
 *
 *  - The sum of this number’s digits is one less than 6D
 *  - The remainder when this number to the power of 91 is divided by 18,793,739 is 12A
 *
 *  Can be equivalently achieved by writing:
 *
 *     calculationWithReference(other) { value, other -> mapper(value) == other }
 *
 *  However, this is more verbose and less efficient to run, as it will do n*m calculations
 */
class TransformedEqualsRefClue(
    crossnumber: Crossnumber,
    other: ClueId,
    private val mapper: (Long) -> List<Long>
) : ContextualClue(crossnumber) {
    private val potentialSolutions = lookupAnswers(other)

    override fun check(value: Long) = potentialSolutions?.let {
        mapper(value).any(potentialSolutions::contains)
    } ?: true
}

fun transformedEqualsRef(clue: String, mapper: (Long) -> Long): ClueConstructor =
    { crossnumber -> TransformedEqualsRefClue(crossnumber, ClueId.fromString(clue)) { value -> listOf(mapper(value)) } }

fun transformedEqualsRefFlattened(clue: String, mapper: (Long) -> List<Long>): ClueConstructor =
    { crossnumber -> TransformedEqualsRefClue(crossnumber, ClueId.fromString(clue), mapper) }