package maths

import solver.Clue
import kotlin.math.log2

fun isCoprimeWith(other: Long): Clue = { areCoprime(it, other) }

fun areCoprime(x: Long, y: Long) = hcf(x, y) == 1L

fun isPrime(value: Long): Boolean {
    if (value == 1L) return false
    if (value == 2L) return true
    if (value % 2 == 0L) return false

    val range = 3..(sqrtFloor(value)) step 2
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

fun primesUpTo(n: Long) = numbersUpTo(n, listOf(2)) { nextPrime(it.last()) }

fun countTwinPrimesUpTo(n: Long): Int {
    return primesUpTo(n).windowed(2).count { it[1] - it[0] == 2L }
}

fun firstPrimeFactor(n: Long) = findFirstPrimeFactor(n)

private tailrec fun findFirstPrimeFactor(n: Long, currentPrime: Long = 2): Long =
    if (isMultipleOf(currentPrime)(n)) currentPrime else findFirstPrimeFactor(n, nextPrime(currentPrime))

fun countPrimesUpTo(n: Long) = (3..n step 2).count(::isPrime) + 1

/**
 * Of the form 2^(2^k) + 1 for some k
 * https://oeis.org/A019434
 */
fun isFermatPrime(p: Long) =
    isPowerOf(2)(p - 1) && isPowerOf(2)(log2((p - 1).toDouble()).toLong()) && isPrime(p)

/**
 * Goldbach's conjecture was: Every odd number can be written in the form p+2a^2, where p is prime or 1 and a is >= 0
 *
 * For a value to violate this it must be:
 *
 *  - Odd
 *  - N - 2a^2 is not prime or 1 for all 0 <= a <= sqrt(n/2)
 */
fun violatesGoldbachConjecture(value: Long) =
    isOdd(value) && (0..sqrtWhole(value / 2)).none {
        val result = value - (2 * it * it)
        result == 1L || (result > 0 && isPrime(result))
    }

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
