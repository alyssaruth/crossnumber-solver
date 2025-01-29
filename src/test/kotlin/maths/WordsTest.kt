package maths

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class WordsTest {
    @Test
    fun `Should describe a number in words`() {
        inWords(1) shouldBe "one"
        inWords(2) shouldBe "two"
        inWords(3) shouldBe "three"
        inWords(4) shouldBe "four"
        inWords(5) shouldBe "five"
        inWords(6) shouldBe "six"
        inWords(7) shouldBe "seven"
        inWords(8) shouldBe "eight"
        inWords(9) shouldBe "nine"
        inWords(10) shouldBe "ten"
        inWords(17) shouldBe "seventeen"

        inWords(25) shouldBe "twenty five"
        inWords(47) shouldBe "forty seven"

        inWords(100) shouldBe "one hundred"
        inWords(101) shouldBe "one hundred and one"
        inWords(213) shouldBe "two hundred and thirteen"
        inWords(997) shouldBe "nine hundred and ninety seven"

        inWords(1000) shouldBe "one thousand"
        inWords(1025) shouldBe "one thousand and twenty five"
        inWords(1111) shouldBe "one thousand one hundred and eleven"
        inWords(14073) shouldBe "fourteen thousand and seventy three"

        inWords(258140) shouldBe "two hundred and fifty eight thousand one hundred and forty"
    }
}