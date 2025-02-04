package maths

import solver.Clue

fun isSumOfTwoDistinctSquares(n: Long): Boolean =
    n.toInt().distinctIntegerPartitions(2).any { partition -> partition.all { isSquare(it.toLong()) } }

fun isSumOfTwoNthPowers(n: Int): Clue = { value ->
    value.toInt().integerPartitions(2).any { partition -> partition.all { hasWholeNthRoot(n)(it.toLong()) } }
}

fun Int.distinctIntegerPartitions(ofLength: Int? = null): List<List<Int>> =
    if (ofLength == 2) computePartitionsOfTwo(true) else computeIntegerPartitions(this, true, ofLength)

fun Int.integerPartitions(ofLength: Int? = null): List<List<Int>> =
    if (ofLength == 2) computePartitionsOfTwo(false) else computeIntegerPartitions(this, false, ofLength)

private fun Int.computePartitionsOfTwo(distinct: Boolean): List<List<Int>> {
    val upperLimit = if (distinct && this % 2 == 0) this / 2 - 1 else this / 2
    return (1..upperLimit).map { listOf(it, this - it) }
}

private tailrec fun computeIntegerPartitions(
    n: Int,
    distinct: Boolean,
    desiredLength: Int? = null,
    wipPartitions: List<List<Int>> = (1..(n + 1) / 2).map(::listOf),
    completedPartitions: List<List<Int>> = emptyList(),
): List<List<Int>> {
    if (wipPartitions.isEmpty()) return completedPartitions.distinct()
        .filter { desiredLength == null || it.size == desiredLength }

    val (newlyFinished, newlyWip) =
        wipPartitions.flatMap { wipPartition ->
            val last = wipPartition.last()
            val currentSum = wipPartition.sum()
            val nextCandidate = if (distinct) last + 1 else last
            val nextCandidates = (nextCandidate..(n - currentSum))
            nextCandidates.map { candidate -> wipPartition + candidate }
        }.filter { desiredLength == null || it.size <= desiredLength }.partition { it.sum() == n }

    return computeIntegerPartitions(n, distinct, desiredLength, newlyWip, completedPartitions + newlyFinished)
}
