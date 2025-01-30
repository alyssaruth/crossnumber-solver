package maths


fun Int.distinctIntegerPartitions(): List<List<Int>> = computeIntegerPartitions(this, true)

fun Int.integerPartitions(ofLength: Int? = null): List<List<Int>> =
    if (ofLength == 2) computePartitionsOfTwo() else computeIntegerPartitions(this, false, ofLength)

private fun Int.computePartitionsOfTwo(): List<List<Int>> = (1..this / 2).map { listOf(it, this - it) }

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
