package maths


fun Int.distinctIntegerPartitions(): List<List<Int>> = computeIntegerPartitions(this)

private tailrec fun computeIntegerPartitions(
    n: Int,
    wipPartitions: List<List<Int>> = (1..(n + 1) / 2).map(::listOf),
    completedPartitions: List<List<Int>> = emptyList(),
): List<List<Int>> {
    if (wipPartitions.isEmpty()) return completedPartitions.distinct()

    val (newlyFinished, newlyWip) =
        wipPartitions.flatMap { wipPartition ->
            val last = wipPartition.last()
            val currentSum = wipPartition.sum()
            val nextCandidates = (last + 1..(n - currentSum))
            nextCandidates.map { candidate -> wipPartition + candidate }
        }.partition { it.sum() == n }

    return computeIntegerPartitions(n, newlyWip, completedPartitions + newlyFinished)
}
