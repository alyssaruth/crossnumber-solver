package maths

import java.math.BigInteger

fun factorialMod(n: Long, base: Long, currentMultiplier: Long = n - 1): Long {
    if (n == 0L) return 0L
    if (currentMultiplier == 1L) return n

    return factorialMod((n * currentMultiplier) % base, base, currentMultiplier - 1)
}

fun integerFactorial(n: Int) = (2..n).toList().product()

fun factorial(n: Long, downTo: Long = 2L) = (downTo..n).map(Long::toBigInteger).fold(BigInteger.ONE, BigInteger::times)

tailrec fun reverseFactorial(n: Long, currentDivisor: Long = 2): Long? {
    val remainder = n / currentDivisor
    return if (n < currentDivisor || !isMultipleOf(currentDivisor)(n)) null else if (remainder == currentDivisor + 1) {
        currentDivisor + 1
    } else reverseFactorial(remainder, currentDivisor + 1)
}