package maths

import solver.Clue
import kotlin.math.log2

fun isSumOfConsecutivePrimes(primes: Int, digitCount: Int): Clue {
    val max = 10.pow(digitCount)
    val candidates = primesUpTo(max).windowed(primes).map { it.sum() }

    return { candidates.contains(it) }
}

fun isPrime(value: Long): Boolean {
    if (value == 1L) return false
    if (value == 2L) return true
    if (value % 2 == 0L) return false

    val range = 3..(sqrtWhole(value)) step 2
    return range.all { value % it != 0L }
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
    if (n == 1L) return 2L
    if (n == 2L) return 3L

    val nextCandidate = n + if (n % 2 == 0L) 1 else 2
    return if (isPrime(nextCandidate)) nextCandidate else nextPrime(nextCandidate)
}

tailrec fun primesUpTo(n: Long, primesSoFar: List<Long> = listOf(2)): List<Long> {
    val nextPrime = nextPrime(primesSoFar.last())
    return if (nextPrime > n) {
        primesSoFar
    } else {
        primesUpTo(n, primesSoFar + nextPrime)
    }
}

fun countTwinPrimesUpTo(n: Long): Int {
    return primesUpTo(n).windowed(2).count { it[1] - it[0] == 2L }
}

fun firstPrimeFactor(n: Long) = findFirstPrimeFactor(n)

private tailrec fun findFirstPrimeFactor(n: Long, currentPrime: Long = 2): Long =
    if (isMultipleOf(currentPrime)(n)) currentPrime else findFirstPrimeFactor(n, nextPrime(currentPrime))

fun countPrimesUpTo(n: Long) = (2..n).filter(::isPrime).size

/**
 * Of the form 2^(2^k) + 1 for some k
 * https://oeis.org/A019434
 */
fun isFermatPrime(p: Long) =
    isPowerOf(2)(p - 1) && isPowerOf(2)(log2((p - 1).toDouble()).toLong()) && isPrime(p)
