package maths

import solver.Clue

fun wholeDiv(x: Long, y: Long) = if (isMultipleOf(y)(x)) x / y else null

fun isMultipleOf(divisor: Long): Clue = { value -> divisor != 0L && value % divisor == 0L }

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

fun isSquare(value: Long) = value > 0 && value == sqrtRounded(value) * sqrtRounded(value)

fun isCube(value: Long) = hasWholeNthRoot(3)(value)

fun List<Long>.mean() = average().longOrNull()

fun meanOf(vararg values: Long) = values.toList().mean()

fun geometricMeanOf(vararg values: Long) = values.toList().geometricMean()

fun List<Long>.geometricMean(): Long? {
    val product = longProduct()
    return if (!hasWholeNthRoot(size)(product)) {
        null
    } else {
        nthRoot(product, size)
    }
}

fun distinctDivisors(n: Long): Set<Long> =
    (1..sqrtRounded(n)).filter { n % it == 0L }.flatMap { listOf(it, n / it) }.toSet()

fun properFactors(n: Long): Set<Long> = distinctDivisors(n) - n

fun isPerfect(n: Long) = properFactors(n).sum() == n

fun isAbundant(n: Long) = properFactors(n).sum() > n
