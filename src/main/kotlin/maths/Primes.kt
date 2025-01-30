package maths

import solver.Clue
import kotlin.math.log2

/**
 * https://oeis.org/A076336
 *
 * Testing for these is pretty bespoke per number, various problems around these are unsolved (including if this list is
 * even complete up to 3 million) so I can't program anything for it :(
 *
 * I *could* hit the OEIS API, but I don't really want to require an internet connection to run the solves
 */
fun isKnownSierpinskiNumber(value: Long) =
    setOf<Long>(
        78557,
        271129,
        271577,
        322523,
        327739,
        482719,
        575041,
        603713,
        903983,
        934909,
        965431,
        1259779,
        1290677,
        1518781,
        1624097,
        1639459,
        1777613,
        2131043,
        2131099,
        2191531,
        2510177,
        2541601,
        2576089,
        2931767,
        2931991,
        3083723,
        3098059,
        3555593,
        3608251
    ).contains(value)

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
