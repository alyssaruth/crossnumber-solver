package solver.clue

import solver.ClueConstructor
import solver.ClueId
import solver.Crossnumber

/**
 * For clues like:
 *
 *  - The sum of the proper factors of 32D
 *  - The product of all the digits of 7A
 *  - 700 less than 3D
 */
class SingleReferenceClue(
    crossnumber: Crossnumber,
    private val other: ClueId,
    private val mapper: (Long) -> List<Long>
) : ComputedPossibilitiesClue(crossnumber) {
    override val possibilities = computePotentialSolutions()

    private fun computePotentialSolutions(): Set<Long>? {
        val values = lookupAnswers(other) ?: return null

        return values.flatMap(mapper).toSet()
    }
}

fun makeSingleReference(clue: String, mapper: (Long) -> Long?): ClueConstructor =
    { crossnumber ->
        SingleReferenceClue(crossnumber, ClueId.fromString(clue)) { l ->
            mapper(l)?.let(::listOf) ?: emptyList()
        }
    }

fun makeSingleReferenceFlattened(clue: String, mapper: (Long) -> List<Long>): ClueConstructor =
    { crossnumber -> SingleReferenceClue(crossnumber, ClueId.fromString(clue), mapper) }