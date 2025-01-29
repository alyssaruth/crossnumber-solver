package maths

import java.math.BigInteger

fun binarySearch(
    value: Long,
    computeNthThing: (BigInteger) -> BigInteger,
): Boolean = binarySearchWithResult(value, computeNthThing) != null

tailrec fun binarySearchWithResult(
    value: Long,
    computeNthThing: (BigInteger) -> BigInteger,
    minimum: Long = 1,
    maximum: Long = value
): Long? {
    val candidate = ((maximum + minimum) / 2)
    val nthThing = computeNthThing(candidate.toBigInteger())

    val diff = nthThing.subtract(value.toBigInteger())
    return if (diff == BigInteger.ZERO) {
        candidate
    } else if (candidate == maximum || candidate == minimum) {
        null
    } else if (diff.signum() == 1) {
        binarySearchWithResult(value, computeNthThing, minimum, candidate)
    } else {
        binarySearchWithResult(value, computeNthThing, candidate, maximum)
    }
}