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

/**
 * There are n^2 1x1 squares, (n-1)^2 2x2 squares and so on.
 *
 * The sum of squares from 1..n can also be expressed as n(n+1)(2n + 1)/6
 */
fun squaresOnNByNChessboard(n: Long) = (n * (n + 1) * ((2 * n) + 1)) / 6