package maths

import solver.Clue

fun Long.digits() = toString().toCharArray().map(Char::digitToInt)

fun Long.digitCounts() = digits().groupBy { it }.mapValues { it.value.size }

fun Long.firstNDigits(n: Int) = digits().subList(0, n).joinToString("").toLong()

fun Long.lastNDigits(n: Int): Long {
    val digits = digits()
    val length = digits.size

    return digits.subList(length - n, length).joinToString("").toLong()
}

fun containsDigit(digit: Int): Clue = { value -> value.digits().contains(digit) }