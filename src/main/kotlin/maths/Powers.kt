package maths

import solver.Clue
import java.math.BigInteger
import kotlin.math.pow
import kotlin.math.roundToLong
import kotlin.math.sqrt

fun geometricMean(values: List<Long>): Long? {
    val product = values.fold(1L, Long::times)
    return nthRoot(product, values.size)
}

fun generatePowers(desiredLength: Int): (Long) -> List<Long> = { value -> generatePowersOfLength(value, desiredLength) }

private tailrec fun generatePowersOfLength(
    value: Long,
    desiredLength: Int,
    currentValue: BigInteger = value.toBigInteger(),
    results: List<Long> = emptyList()
): List<Long> {
    val currentLength = currentValue.digits().size
    if (currentLength > desiredLength) {
        return results
    } else {
        val newResults = if (currentLength == desiredLength) results + currentValue.longValueExact() else results
        return generatePowersOfLength(value, desiredLength, currentValue.times(value.toBigInteger()), newResults)
    }
}

fun sqrtWhole(value: Long) = if (!isSquare(value)) null else sqrtRounded(value)

fun sqrtRounded(value: Long) = sqrt(value.toDouble()).roundToLong()

fun sqrtFloor(value: Long) = sqrt(value.toDouble()).toLong()

fun isPowerOf(x: Long): Clue = { isPowerOf(x, it) }

private tailrec fun isPowerOf(x: Long, value: Long): Boolean =
    if (value == 0L) {
        false
    } else if (value == x || value == 1L) {
        true
    } else if (!isMultipleOf(x)(value)) {
        false
    } else {
        isPowerOf(x, value / x)
    }

fun Int.pow(power: Int) = toDouble().pow(power.toDouble()).toLong()

fun Long.bigPow(power: Int): BigInteger = toBigInteger().pow(power)

fun nthRoot(value: Long, power: Int): Long? = binarySearchWithResult(value, { n -> n.pow(power) })

fun hasWholeNthRoot(power: Int): Clue = { value -> nthRoot(value, power) != null }

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