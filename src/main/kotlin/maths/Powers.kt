package maths

import java.math.BigInteger
import kotlin.math.pow

fun Int.pow(power: Int) = toDouble().pow(power.toDouble()).toLong()

fun hasWholeNthRoot(value: Long, power: Int): Boolean = binarySearch(value, { n -> n.pow(power) })

fun Long.modPow(power: Long, base: Long) =
    modPow(this.toBigInteger(), power.toBigInteger(), base.toBigInteger()).toLong()

/**
 * Thank you Advent of Code 2019!
 *
 * https://en.wikipedia.org/wiki/Modular_exponentiation#Right-to-left_binary_method
 */
tailrec fun modPow(
    n: BigInteger,
    power: BigInteger,
    base: BigInteger,
    currentResult: BigInteger = BigInteger.ONE,
): BigInteger {
    if (power == BigInteger.ZERO) {
        return currentResult
    } else {
        val newResult =
            if (power.mod(BigInteger.TWO) == BigInteger.ZERO) currentResult else n.times(currentResult).mod(base)
        val newN = n.times(n).mod(base)
        val newPower = power.shiftRight(1)

        return modPow(newN, newPower, base, newResult)
    }
}