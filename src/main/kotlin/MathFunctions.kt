package com.github.alyssaruth

import java.math.BigInteger
import kotlin.math.roundToLong
import kotlin.math.sqrt

fun identity(unused: Long): Boolean = true

fun isEqualTo(result: Long): Clue = { value -> value == result }

fun isPrime(value: Long): Boolean {
    if (value == 1L) return false
    if (value == 2L) return true
    if (value % 2 == 0L) return false

    val range = 3..(sqrt(value)) step 2
    return range.all { value % it != 0L }
}

private fun sqrt(value: Long) = sqrt(value.toDouble()).roundToLong()

fun isMultipleOf(divisor: Long): Clue = { value -> value % divisor == 0L }

fun isPalindrome(value: Long): Boolean = value.toString() == value.toString().reversed()

fun containsDigit(digit: Long): Clue = { value -> value.toString().contains(digit.toString()) }

fun isTriangleNumber(value: Long): Boolean = testTriangleNumber(value)

private tailrec fun testTriangleNumber(value: Long, minimum: Long = 1, maximum: Long = value): Boolean {
    val candidate = ((maximum + minimum) / 2)
    val triangleNumber = candidate.toBigInteger().times((candidate + 1).toBigInteger()).divide(2.toBigInteger())

    val diff = triangleNumber.subtract(value.toBigInteger())
    return if (diff == BigInteger.ZERO) {
        true
    } else if (candidate == maximum || candidate == minimum) {
        false
    } else if (diff.signum() == 1) {
        testTriangleNumber(value, minimum, candidate)
    } else {
        testTriangleNumber(value, candidate, maximum)
    }
}

fun Long.primeFactors(): List<Long> = primeFactorise(this)

private tailrec fun primeFactorise(n: Long, factorsSoFar: List<Long> = emptyList(), p: Long = 2): List<Long> =
    if (n == 1L)
        factorsSoFar
    else if (isMultipleOf(p)(n))
        primeFactorise(n / p, factorsSoFar + p, p)
    else
        primeFactorise(n, factorsSoFar, nextPrime(p))

private tailrec fun nextPrime(p: Long): Long = if (p == 2L) 3L else if (isPrime(p + 2)) p + 2 else nextPrime(p + 2)

fun isSquare(value: Long) = value == sqrt(value) * sqrt(value)
