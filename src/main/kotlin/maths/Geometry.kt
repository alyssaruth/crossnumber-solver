package maths

/**
 * https://oeis.org/A003401
 *
 * n-gon is constructible iff n is the product of a power of 2 and any number of distinct Fermat primes
 */
fun nGonIsConstructible(n: Long): Boolean {
    val primeFactors = n.primeFactors()
    val factorsOtherThan2 = primeFactors.filterNot { it == 2L }
    return n > 2 &&
            factorsOtherThan2.distinct().size == factorsOtherThan2.size &&
            factorsOtherThan2.all(::isFermatPrime)
}
