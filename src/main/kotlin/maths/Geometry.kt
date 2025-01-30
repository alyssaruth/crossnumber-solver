package maths

import solver.Point

/**
 * https://oeis.org/A003401
 *
 * n-gon is constructible iff n is the product of a power of 2 and any number of distinct Fermat primes
 */
fun nGonIsConstructible(n: Long): Boolean {
    val primeFactors = n.primeFactors()
    val factorsOtherThan2 = primeFactors.filterNot { it == 2L }
    return n > 2 &&
            factorsOtherThan2.distinct().size == factorsOtherThan2.size &&
            factorsOtherThan2.all(::isFermatPrime)
}

/**
 * The number of straight lines that go through at least two points of a NxN grid of points
 */
fun countStraightLinesThroughGrid(n: Int): Long {
    val pts = (0 until n).flatMap { x -> (0 until n).map { y -> Point(x, y) } }

    val permutations = pts.flatMap { a -> (pts - a).map { b -> setOf(a, b) } }.distinct()
    val lines = permutations.map(::toLine).distinct()
    return lines.size.toLong()
}

private fun toLine(points: Set<Point>): Line {
    val (a, b) = points.toList()
    val slope = if (a.x == b.x) null else Fraction(a.y.toLong() - b.y, a.x.toLong() - b.x).normalise()
    val c = if (slope == null) a.x.toFraction() else a.y.toFraction() - (slope * a.x.toFraction())
    return Line(slope, c)
}

private data class Line(val slope: Fraction?, val c: Fraction)