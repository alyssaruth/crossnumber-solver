package solver.clue

import solver.ClueConstructor
import solver.ClueId
import solver.Crossnumber
import solver.PartialSolution

/**
 * This is the same as another number in the crossnumber
 */
class EqualsSomeOther(private val myClueId: ClueId, crossnumber: Crossnumber) : ComputedPossibilitiesClue(crossnumber) {
    private val myLength = crossnumber.solutions.getValue(myClueId).squares.size

    override val possibilities = computePotentialSolutions()

    private fun computePotentialSolutions(): Set<Long>? {
        val potentialSolutions = otherSolutions().values
        val partialSolutions = potentialSolutions.filterIsInstance<PartialSolution>()
        if (partialSolutions.size < potentialSolutions.size) {
            // At least one still pending, can't tell anything
            return null
        }

        return partialSolutions.flatMap { it.possibilities }.toSet()
    }

    private fun otherSolutions() = crossnumber.solutionsOfLength(myLength) - myClueId
}

fun String.equalsSomeOther(): Pair<String, ClueConstructor> =
    this to { crossnumber -> EqualsSomeOther(ClueId.fromString(this), crossnumber) }