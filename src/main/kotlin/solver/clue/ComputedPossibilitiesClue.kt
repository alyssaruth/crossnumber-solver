package solver.clue

import solver.ClueConstructor
import solver.Crossnumber

/**
 * Any type of clue where we do some up-front work with the crossnumber to calculate a set of possibilities
 *
 * The set will be null if we're unable to yet, e.g. because the total amount would exceed RAM_THRESHOLD or something we
 * rely on is still pending.
 */
abstract class ComputedPossibilitiesClue(crossnumber: Crossnumber) : ContextualClue(crossnumber) {
    protected abstract val possibilities: Set<Long>?

    override fun check(value: Long) = possibilities?.contains(value) ?: true

    override fun knownPossibilities() = possibilities?.toList()?.sorted()
}

class KnownPossibilitiesClue(override val possibilities: Set<Long>, crossnumber: Crossnumber) :
    ComputedPossibilitiesClue(crossnumber)

fun knownPossibilities(possibilities: Set<Long>): ClueConstructor =
    { crossnumber -> KnownPossibilitiesClue(possibilities, crossnumber) }