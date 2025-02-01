package maths

import solver.Clue

fun isMultipleOf(divisor: Long): Clue = { value -> value % divisor == 0L }

fun isNotMultipleOf(divisor: Long): Clue = { value -> !isMultipleOf(divisor)(value) }

fun hasMultiplicativePersistence(persistence: Int): Clue = { value -> multiplicativePersistence(value) == persistence }

tailrec fun multiplicativePersistence(n: Long, persistenceSoFar: Int = 0): Int {
    val digits = n.digits()
    return if (digits.size == 1) {
        persistenceSoFar
    } else {
        multiplicativePersistence(digits.product(), persistenceSoFar + 1)
    }
}

val isEven: Clue = isMultipleOf(2L)
val isOdd: Clue = { x -> !isMultipleOf(2L)(x) }

fun List<Int>.product() = fold(1, Long::times)

fun Long.reversed(): Long = toString().reversed().toLong()

fun isPalindrome(value: Long): Boolean = value.toString().isPalindrome()

fun isTriangleNumber(value: Long): Boolean =
    binarySearch(value) { it.times(it.plus(1.toBigInteger())).divide(2.toBigInteger()) }

fun isTetrahedralNumber(value: Long): Boolean =
    binarySearch(
        value
    ) { it.times(it.plus(1.toBigInteger())).times(it.plus(2.toBigInteger())).divide(6.toBigInteger()) }

fun isSquare(value: Long) = value == sqrtWhole(value) * sqrtWhole(value)

fun hasDigitSum(n: Int): Clue = { value -> value.digitSum() == n }

fun Long.digitSum() = digits().sum()

fun digitSum(n: Long) = n.digitSum().toLong()

fun distinctDivisors(n: Long): Set<Long> =
    (1..sqrtWhole(n)).filter { n % it == 0L }.flatMap { listOf(it, n / it) }.toSet()

fun properFactors(n: Long): Set<Long> = distinctDivisors(n) - n

fun isPerfect(n: Long) = properFactors(n).sum() == n

fun isAbundant(n: Long) = properFactors(n).sum() > n
