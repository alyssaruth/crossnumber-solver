package maths

import solver.Clue
import solver.ClueConstructor
import solver.clue.knownPossibilities
import java.math.BigInteger

fun Long.digitsAreStrictlyIncreasing() = digits().windowed(2).all { it[0] < it[1] }

fun BigInteger.digits() = collectDigits(this)

fun BigInteger.digitSum() = digits().sum()

fun hasDigitSum(n: Int): Clue = { value -> value.digitSum() == n }

fun Long.digitSum() = digits().sum()

fun digitSum(n: Long) = n.digitSum().toLong()

fun digitProduct(n: Long) = n.digits().product()


private fun collectDigits(remaining: BigInteger, digitsSoFar: List<Int> = emptyList()): List<Int> {
    if (remaining == BigInteger.ZERO) {
        return digitsSoFar
    } else {
        val lastDigit = remaining.mod(BigInteger.TEN).intValueExact()
        return collectDigits(remaining.divide(BigInteger.TEN), listOf(lastDigit) + digitsSoFar)
    }
}

fun digitsSameExceptOne(length: Int): ClueConstructor {
    val digits = (0..9).toList()
    val digitPairs = listOf(digits, digits).allCombinations().filterNot { (a, b) -> a == b }
    val numbers = digitPairs.flatMap { (a, b) ->
        val allAs = List(length) { a }
        allAs.indices.map { ix -> allAs.replaceAt(ix, b).fromDigits() }
    }
    return knownPossibilities(numbers.filter { it.digits().size == length }.toSet())
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

fun Number.digits() = toString().toCharArray().map(Char::digitToInt)

fun Number.longDigits() = digits().map(Int::toLong)

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

fun permuteDigits(n: Number) = n.digits().indices.flatMap { permuteDigit(n, it) }

private fun permuteDigit(n: Number, digitIndex: Int): List<Long> {
    val digits = n.digits()
    val replacementDigits = getViableDigits(digitIndex == 0) - n.digits()[digitIndex]

    return replacementDigits.map { newDigit ->
        digits.replaceAt(digitIndex, newDigit).fromDigits()
    }
}

fun getViableDigits(leading: Boolean) = if (leading) (1..9).toList() else (0..9).toList()