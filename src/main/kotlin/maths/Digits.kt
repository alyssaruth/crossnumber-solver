package maths

import solver.Clue
import java.math.BigInteger

fun Long.digitsAreStrictlyIncreasing() = digits().windowed(2).all { it[0] < it[1] }

fun BigInteger.digits() = collectDigits(this)

fun BigInteger.digitSum() = digits().sum()

private fun collectDigits(remaining: BigInteger, digitsSoFar: List<Int> = emptyList()): List<Int> {
    if (remaining == BigInteger.ZERO) {
        return digitsSoFar
    } else {
        val lastDigit = remaining.mod(BigInteger.TEN).intValueExact()
        return collectDigits(remaining.divide(BigInteger.TEN), listOf(lastDigit) + digitsSoFar)
    }
}

fun middleNDigits(n: Int, value: Long) = findMiddleNDigits(n, value.digits())

private tailrec fun findMiddleNDigits(n: Int, digits: List<Int>): Long =
    if (digits.size == n) {
        digits.fromDigits()
    } else if (digits.size < n) {
        throw Exception("Unable to get middle $n digits!")
    } else {
        findMiddleNDigits(n, digits.drop(1).dropLast(1))
    }

fun Long.isAnagramOf(other: Long) = digits().sorted() == other.digits().sorted()

fun Long.digits() = toString().toCharArray().map(Char::digitToInt)

fun Long.nonZeroDigits() = digits().filter { it > 0 }

fun Long.digitCounts(): Map<Int, Int> = digits().groupBy { it }.mapValues { it.value.size }

fun hasUniqueDigits(n: Int): Clue = { it.digits().toSet().size == n }

fun Long.firstNDigits(n: Int) = digits().subList(0, n).fromDigits()

fun List<Int>.fromDigits() = joinToString("").toLong()

fun Long.lastNDigits(n: Int): Long {
    val digits = digits()
    val length = digits.size

    return digits.subList(length - n, length).fromDigits()
}

fun Long.concatenate(other: Long) = "$this$other".toLong()

fun containsDigit(digit: Int): Clue = { value -> value.digits().contains(digit) }

fun canBePermutedSuchThat(condition: Clue): Clue = { value ->
    permuteDigits(value).any(condition)
}

fun permuteDigits(n: Long) = n.digits().indices.flatMap { permuteDigit(n, it) }

private fun permuteDigit(n: Long, digitIndex: Int): List<Long> {
    val digits = n.digits()
    val replacementDigits = getViableDigits(digitIndex == 0) - n.digits()[digitIndex]

    return replacementDigits.map { newDigit ->
        digits.mapIndexed { index, digit -> if (index == digitIndex) newDigit else digit }.fromDigits()
    }
}

fun getViableDigits(leading: Boolean) = if (leading) (1..9).toList() else (0..9).toList()