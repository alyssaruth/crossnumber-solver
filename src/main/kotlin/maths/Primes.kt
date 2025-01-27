package maths

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

fun primesUpTo(n: Long, primesSoFar: List<Long> = listOf(2)): List<Long> {
    val nextPrime = nextPrime(primesSoFar.last())
    return if (nextPrime > n) {
        primesSoFar
    } else {
        primesUpTo(n, primesSoFar + nextPrime)
    }
}