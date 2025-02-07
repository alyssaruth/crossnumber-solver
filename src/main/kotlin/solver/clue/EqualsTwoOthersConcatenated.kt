package solver.clue

import maths.allCombinations
import maths.concatenate
import solver.ClueConstructor
import solver.ClueId
import solver.Crossnumber
import solver.RAM_THRESHOLD

class EqualsTwoOthersConcatenated(
    myClueId: ClueId,
    private val knownPairing: List<Int>?,
    crossnumber: Crossnumber
) :
    ComputedPossibilitiesClue(crossnumber) {
    private val myLength = crossnumber.solutions.getValue(myClueId).squares.size

    override val possibilities = buildPossibilities()

    private fun buildPossibilities(): Set<Long>? {
        val relevantLengths = knownPairing ?: (2..myLength - 2)

        val solutionsMap = relevantLengths.associateWith { length ->
            crossnumber.solutionsOfLength(length).keys.fold<ClueId, Set<Long>?>(emptySet()) { setSoFar, clueId ->
                val newAnswers = lookupAnswers(clueId)
                if (newAnswers == null || setSoFar == null) {
                    null
                } else {
                    setSoFar + newAnswers
                }
            }
        }

        if (solutionsMap.values.any { it == null }) {
            return null
        }

        val lengthMap = solutionsMap.mapValues { entry -> entry.value!! }

        val lengthPairs =
            knownPairing?.let(::listOf) ?: lengthMap.keys.map { listOf(it, myLength - it).sorted() }.distinct()
        return lengthPairs.fold<List<Int>, Set<Long>?>(setOf()) { setSoFar, (lengthA, lengthB) ->
            if (setSoFar == null) {
                null
            } else {
                val aSolutions = solutionsMap[lengthA]
                val bSolutions = solutionsMap[lengthB]

                if (aSolutions == null || bSolutions == null) {
                    setSoFar
                } else {
                    val newEntries = 2 * aSolutions.size * bSolutions.size
                    if (newEntries + setSoFar.size > RAM_THRESHOLD) {
                        null
                    } else {
                        val newPossibilities = listOf(aSolutions, bSolutions).allCombinations()
                            .flatMap { (a, b) -> listOf(a.concatenate(b), b.concatenate(a)) }
                        setSoFar + newPossibilities
                    }
                }
            }
        }
    }
}

fun String.equalsTwoOthersConcatenated(knownPairing: Pair<Int, Int>? = null): Pair<String, ClueConstructor> =
    this to { crossnumber -> EqualsTwoOthersConcatenated(ClueId.fromString(this), knownPairing?.toList(), crossnumber) }