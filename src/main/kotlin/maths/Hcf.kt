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