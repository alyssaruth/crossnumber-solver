package maths

import java.math.BigInteger

tailrec fun binarySearch(
    value: Long,
    computeNthThing: (BigInteger) -> BigInteger,
    minimum: Long = 1,
    maximum: Long = value
): Boolean {
    val candidate = ((maximum + minimum) / 2)
    val nthThing = computeNthThing(candidate.toBigInteger())

    val diff = nthThing.subtract(value.toBigInteger())
    return if (diff == BigInteger.ZERO) {
        true
    } else if (candidate == maximum || candidate == minimum) {
        false
    } else if (diff.signum() == 1) {
        binarySearch(value, computeNthThing, minimum, candidate)
    } else {
        binarySearch(value, computeNthThing, candidate, maximum)
    }
}