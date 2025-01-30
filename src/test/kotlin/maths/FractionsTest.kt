package maths

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class FractionsTest {
    @Test
    fun `Normalising fractions`() {
        Fraction(3, 6).normalise() shouldBe Fraction(1, 2)
        Fraction(15, 51).normalise() shouldBe Fraction(5, 17)

        Fraction(0, 5).normalise() shouldBe Fraction(0, 1)
    }

    @Test
    fun `Subtracting fractions`() {
        Fraction(5, 2) - 1.toFraction() shouldBe Fraction(3, 2)
        Fraction(5, 6) - Fraction(1, 4) shouldBe Fraction(7, 12)
    }
}