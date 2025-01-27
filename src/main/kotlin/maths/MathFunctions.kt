package maths

import solver.Clue
import kotlin.math.roundToLong
import kotlin.math.sqrt

fun isEqualTo(result: Long): Clue = { value -> value == result }

fun sqrtWhole(value: Long) = sqrt(value.toDouble()).roundToLong()

fun isMultipleOf(divisor: Long): Clue = { value -> value % divisor == 0L }

fun isPowerOf(x: Long): Clue = { isPowerOf(x, it) }

private tailrec fun isPowerOf(x: Long, value: Long): Boolean =
    if (value == x) {
        true
    } else if (!isMultipleOf(x)(value)) {
        false
    } else {
        isPowerOf(x, value / x)
    }

fun hasMultiplicativePersistence(persistence: Int): Clue = { value -> multiplicativePersistence(value) == persistence }

tailrec fun multiplicativePersistence(n: Long, persistenceSoFar: Int = 0): Int {
    val digits = n.digits()
    return if (digits.size == 1) {
        persistenceSoFar
    } else {
        multiplicativePersistence(digits.product(), persistenceSoFar + 1)
    }
}

fun List<Int>.product() = fold(1, Long::times)

fun Long.reversed(): Long = toString().reversed().toLong()

fun isPalindrome(value: Long): Boolean = value.toString() == value.toString().reversed()

fun Long.digits() = toString().toCharArray().map(Char::digitToInt)

fun Long.digitCounts() = digits().groupBy { it }.mapValues { it.value.size }

fun containsDigit(digit: Int): Clue = { value -> value.digits().contains(digit) }

fun isTriangleNumber(value: Long): Boolean =
    binarySearch(value, { it.times(it.plus(1.toBigInteger())).divide(2.toBigInteger()) })

fun isTetrahedralNumber(value: Long): Boolean =
    binarySearch(
        value,
        { it.times(it.plus(1.toBigInteger())).times(it.plus(2.toBigInteger())).divide(6.toBigInteger()) })

fun isSquare(value: Long) = value == sqrtWhole(value) * sqrtWhole(value)

fun hasUniqueDigits(n: Int): Clue = { value -> value.toString().toCharArray().distinct().size == n }

fun hasDigitSum(n: Int): Clue = { value -> value.digitSum() == n }

fun Long.digitSum() = digits().sum()

fun distinctDivisors(n: Long): Set<Long> =
    (1..sqrtWhole(n)).filter { n % it == 0L }.flatMap { listOf(it, n / it) }.toSet()

fun properFactors(n: Long): Set<Long> = distinctDivisors(n) - n

fun isPerfect(n: Long) = properFactors(n).sum() == n

fun isAbundant(n: Long) = properFactors(n).sum() > n

fun factorial(n: Long) = (1..n).toList().fold(1L, Long::times)

tailrec fun reverseFactorial(n: Long, currentDivisor: Long = 2): Long? {
    val remainder = n / currentDivisor
    return if (n < currentDivisor || !isMultipleOf(currentDivisor)(n)) null else if (remainder == currentDivisor + 1) {
        currentDivisor + 1
    } else reverseFactorial(remainder, currentDivisor + 1)
}
