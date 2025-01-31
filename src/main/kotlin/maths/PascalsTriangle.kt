package maths

import solver.Clue
import java.math.BigInteger

private val ROW_ZERO = listOf(BigInteger.ONE)
private val ROW_ONE = listOf(BigInteger.ONE, BigInteger.ONE)

fun appearsInPascalsTriangle(times: Int): Clue = { value ->
    countNumberInPascalsTriangle(value.toInt()) == times
}

/**
 * Allow overflows since we're testing an int, so those bigger values don't actually matter.
 * Throw away anything that overflows to keep the row size down
 */
tailrec fun countNumberInPascalsTriangle(
    n: Int,
    currentRow: List<Int> = listOf(1, 1),
    currentCount: Int = 0,
): Int {
    return if (currentRow[1] > n) {
        currentCount
    } else {
        val newCount = currentCount + currentRow.count { it == n }
        val nextRow =
            listOf(1) + currentRow.windowed(2).map { (a, b) -> a + b } + listOf(1)
        countNumberInPascalsTriangle(n, nextRow.filterNot { it < 0 }, newCount)
    }
}

tailrec fun generateTriangle(
    toRow: Int,
    rowsSoFar: List<List<BigInteger>> = listOf(ROW_ZERO, ROW_ONE)
): List<List<BigInteger>> {
    return if (rowsSoFar.size >= toRow + 1) {
        rowsSoFar
    } else {
        val nextRow = nextRow(rowsSoFar.last())
        generateTriangle(toRow, rowsSoFar + listOf(nextRow))
    }
}

private fun nextRow(currentRow: List<BigInteger>) =
    listOf(BigInteger.ONE) + currentRow.windowed(2).map { (a, b) -> a.plus(b) } + listOf(BigInteger.ONE)