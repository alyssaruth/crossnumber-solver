package maths

/**
 * To avoid stupid floating-point / double imprecision problems
 */
data class Fraction(val numerator: Long, val denominator: Long)

fun Int.toFraction() = Fraction(this.toLong(), 1)

fun Fraction.normalise(): Fraction {
    if (numerator == 0L) {
        return Fraction(0, 1)
    }

    val hcf = hcf(numerator, denominator).toInt()
    return Fraction(numerator / hcf, denominator / hcf)
}

operator fun Fraction.times(other: Fraction) = Fraction(numerator * other.numerator, denominator * other.denominator)

operator fun Fraction.minus(other: Fraction): Fraction {
    val lcm = lcm(denominator, other.denominator)
    val aMultiplier = lcm / denominator
    val bMultiplier = lcm / other.denominator

    val newNumerator = (aMultiplier * numerator) - (bMultiplier * other.numerator)
    return Fraction(newNumerator, lcm).normalise()
}