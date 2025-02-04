package solver.clue

import maths.digits
import solver.ClueConstructor
import solver.Crossnumber
import solver.Orientation
import solver.PartialSolution
import solver.PendingSolution

/**
 * Seems niche, but has come up more than one! Clues like:
 *
 *  - The number of the D clue which has the answer 91199
 *  - The number of the across clue whose answer is 26
 */
class EqualToNumberOfClueWithAnswer(
    crossnumber: Crossnumber,
    private val orientation: Orientation,
    private val answer: Long
) : ContextualClue(crossnumber) {
    private val candidates = calculatePossibilities()

    private fun calculatePossibilities(): List<Long> {
        val cluesOfRightLength =
            crossnumber.solutionsOfLength(answer.digits().size).filterKeys { it.orientation == orientation }
        val viableClues =
            cluesOfRightLength.filter { (_, value) -> (value is PartialSolution && value.possibilities.contains(answer)) || value is PendingSolution }
        return viableClues.keys.map { it.number.toLong() }
    }

    override fun check(value: Long) = candidates.contains(value)
}

fun equalToNumberOfClueWithAnswer(orientation: Orientation, answer: Long): ClueConstructor =
    { crossnumber -> EqualToNumberOfClueWithAnswer(crossnumber, orientation, answer) }