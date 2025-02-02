package maths

/**
 * Euclidean algorithm for HCF / GCD
 */
fun hcf(x: Long, y: Long) = if (x == 0L || y == 0L) 0L else computeHcf(x, y)

private tailrec fun computeHcf(x: Long, y: Long, max: Long = maxOf(x, y), min: Long = minOf(x, y)): Long =
    if (x == y) {
        x
    } else if (max % min == 0L) {
        min
    } else {
        computeHcf(x, y, min, max % min)
    }

fun lcm(vararg values: Long) = if (values.size == 2) fastLcm(values[0], values[1]) else lcm(values.toList())

private fun fastLcm(x: Long, y: Long) = (x * y) / hcf(x, y)

fun lcm(values: List<Long>): Long {
    val primeFactorisations = values.map { it.primeFactors() }
    val primes = primeFactorisations.flatten().distinct()
    return primes.flatMap { prime ->
        val count = primeFactorisations.maxOf { it.count { p -> p == prime } }
        List(count) { prime }
    }.fold(1L, Long::times)
}