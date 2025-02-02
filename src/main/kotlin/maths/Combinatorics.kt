package maths

/**
 * N choose K = n! / k! (n-k)!
 *
 * We can pre-cancel this to help reduce the computation
 */
fun Int.choose(k: Int): Long {
    val cancelBy = maxOf(k, this - k) + 1L
    val divideBy = minOf(k, this - k).toLong()

    return factorial(this.toLong(), downTo = cancelBy).divide(factorial(divideBy)).longValueExact()
}