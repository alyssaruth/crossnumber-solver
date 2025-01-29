package maths

/**
 * Euclidean algorithm for HCF / GCD
 */
fun hcf(x: Long, y: Long) = computeHcf(x, y)

private tailrec fun computeHcf(x: Long, y: Long, max: Long = maxOf(x, y), min: Long = minOf(x, y)): Long =
    if (x == y) {
        x
    } else if (max % min == 0L) {
        min
    } else {
        computeHcf(x, y, min, max % min)
    }


fun lcm(values: List<Int>): Long {
    val primeFactorisations = values.map { it.toLong().primeFactors() }
    val primes = primeFactorisations.flatten().distinct()
    return primes.flatMap { prime ->
        val count = primeFactorisations.maxOf { it.count { p -> p == prime } }
        List(count) { prime }
    }.fold(1L, Long::times)
}