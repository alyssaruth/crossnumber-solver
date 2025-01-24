package maths

import solver.Clue
import java.math.BigInteger
import kotlin.math.roundToLong
import kotlin.math.sqrt

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

fun List<Int>.product() = fold(1, Long::times)

fun Long.reversed(): Long = toString().reversed().toLong()

fun isPalindrome(value: Long): Boolean = value.toString() == value.toString().reversed()

fun Long.digits() = toString().toCharArray().map(Char::digitToInt)

fun containsDigit(digit: Int): Clue = { value -> value.digits().contains(digit) }

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

tailrec fun nextPrime(n: Long): Long {
    if (n == 2L) return 3L

    val nextCandidate = n + if (n % 2 == 0L) 1 else 2
    return if (isPrime(nextCandidate)) nextCandidate else nextPrime(nextCandidate)
}

fun primesUpTo(n: Long, primesSoFar: List<Long> = listOf(2)): List<Long> {
    val nextPrime = nextPrime(primesSoFar.last())
    return if (nextPrime > n) {
        primesSoFar
    } else {
        primesUpTo(n, primesSoFar + nextPrime)
    }
}

fun isSquare(value: Long) = value == sqrt(value) * sqrt(value)

fun hasUniqueDigits(n: Int): Clue = { value -> value.toString().toCharArray().distinct().size == n }

fun hasDigitSum(n: Int): Clue = { value -> value.digitSum() == n }

fun Long.digitSum() = digits().sum()

fun distinctDivisors(n: Long): Set<Long> = (1..sqrt(n)).filter { n % it == 0L }.flatMap { listOf(it, n / it) }.toSet()

fun isPerfect(n: Long) = distinctDivisors(n).minus(n).sum() == n

fun factorial(n: Long) = (1..n).toList().fold(1L, Long::times)

tailrec fun reverseFactorial(n: Long, currentDivisor: Long = 2): Long? {
    val remainder = n / currentDivisor
    return if (n < currentDivisor || !isMultipleOf(currentDivisor)(n)) null else if (remainder == currentDivisor + 1) {
        currentDivisor + 1
    } else reverseFactorial(remainder, currentDivisor + 1)
}
