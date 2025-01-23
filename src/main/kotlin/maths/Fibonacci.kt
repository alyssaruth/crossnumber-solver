package maths

/**
 * Efficiently test if a number is Fibonacci or not by using the fact that the nth Fibonacci number is the
 * top-left element of the matrix calculation:
 *
 * |1 1| ^ n
 * |1 0|
 */

data class Matrix2d(val tl: Long, val tr: Long, val bl: Long, val br: Long)

operator fun Matrix2d.times(other: Matrix2d) = Matrix2d(
    (tl * other.tl) + (tr * other.bl),
    (tl * other.tr) + (tr * other.br),
    (bl * other.tl) + (br * other.bl),
    (bl * other.tr) + (br * other.br),
)

fun Matrix2d.pow(n: Int): Matrix2d {
    if (n == 0) return Matrix2d(1, 0, 0, 1) // Identity matrix
    if (n == 1) return this

    return if (n % 2 != 0) {
        pow(n / 2) * pow(n / 2) * this
    } else {
        pow(n / 2) * pow(n / 2)
    }
}

fun nthFibonacci(n: Int) = Matrix2d(1, 1, 1, 0).pow(n - 1).tl

fun isFibonacci(value: Long) = testIsFibonacci(value, 1)

tailrec fun testIsFibonacci(candidate: Long, n: Int): Boolean {
    val nthFibonacci = nthFibonacci(n)
    return if (nthFibonacci == candidate) {
        true
    } else if (nthFibonacci > candidate) {
        false
    } else {
        testIsFibonacci(candidate, n + 1)
    }
}