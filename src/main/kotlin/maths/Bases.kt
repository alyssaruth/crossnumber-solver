package maths

import solver.ClueConstructor
import solver.clue.knownPossibilities

fun Long.toBinary() = toString(2)

fun canBeWrittenInSomeBaseAs(value: Long, digitCount: Int): ClueConstructor {
    val possibles = buildBaseCandidates(value, digitCount).toSet()
    return knownPossibilities(possibles)
}

private tailrec fun buildBaseCandidates(
    value: Long,
    digitCount: Int,
    currentBase: Int = value.digits().max() + 1,
    currentCandidates: List<Long> = emptyList()
): List<Long> {
    val candidate = value.toString().toLong(currentBase)
    val candidateLength = candidate.toString().length
    val newCandidates =
        if (candidateLength == digitCount && currentBase != 10) currentCandidates + candidate else currentCandidates
    return if (candidateLength > digitCount) {
        currentCandidates
    } else {
        buildBaseCandidates(value, digitCount, currentBase + 1, newCandidates)
    }
}