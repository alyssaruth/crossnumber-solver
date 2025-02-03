package maths

import solver.Point
import java.math.BigInteger

/**
 * 2 ^ (n-k) * nCk
 *
 * Lovely explanation here: https://www.math.brown.edu/tbanchof/Beyond3d/chapter4/section07.html
 */
fun facesOfAHypercube(faceDimension: Int, cubeDimension: Int) =
    2.pow(cubeDimension - faceDimension) * cubeDimension.choose(faceDimension)

/**
 * https://oeis.org/A003401
 *
 * n-gon is constructible iff n is the product of a power of 2 and any number of distinct Fermat primes
 */
fun nGonIsConstructible(n: Long): Boolean {
    val primeFactors = n.primeFactors().filterNot { it == 2L }
    return n > 2 &&
            primeFactors.distinct().size == primeFactors.size &&
            primeFactors.all(::isFermatPrime)
}

/**
 * A000125
 */
fun nthCakeNumber(n: Int) = (n.pow(3) + (5 * n) + 6) / 6

/**
 * OEIS: A000127
 *
 * (n^4 - 6n^3 + 23n^2 - 18n^24 + 24) / 24
 *
 * But where's the fun in a boring quartic?
 * It's _also_ equivalent to the sum of the first five terms in the (N-1)th row of Pascal's triangle.
 */
fun maximumRegionsByJoiningPointsOnACircle(n: Int): Long {
    val pascalsRow = generateTriangle(n).dropLast(1).last()
    return pascalsRow.subList(0, minOf(5, pascalsRow.size)).map(BigInteger::longValueExact).sum()
}
//fun maximumRegionsByJoiningPointsOnACircle(n: Int): Long =
//    (n.pow(4) - (6 * n.pow(3)) + (23 * n.pow(2)) - (18 * n) + 24) / 24

/**
 * The number of straight lines that go through at least two points of a NxN grid of points
 *
 * OEIS: A018808
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