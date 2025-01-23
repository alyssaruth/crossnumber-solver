package maths

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class RomanNumeralsTest {
    @Test
    fun `Should correctly convert to roman numerals`() {
        toRomanNumerals(1) shouldBe "I"
        toRomanNumerals(2) shouldBe "II"
        toRomanNumerals(3) shouldBe "III"
        toRomanNumerals(4) shouldBe "IV"
        toRomanNumerals(5) shouldBe "V"
        toRomanNumerals(6) shouldBe "VI"
        toRomanNumerals(7) shouldBe "VII"
        toRomanNumerals(8) shouldBe "VIII"
        toRomanNumerals(9) shouldBe "IX"
        toRomanNumerals(10) shouldBe "X"
        toRomanNumerals(39) shouldBe "XXXIX"
        toRomanNumerals(40) shouldBe "XL"
        toRomanNumerals(90) shouldBe "XC"
        toRomanNumerals(160) shouldBe "CLX"
        toRomanNumerals(207) shouldBe "CCVII"
        toRomanNumerals(246) shouldBe "CCXLVI"
        toRomanNumerals(400) shouldBe "CD"
        toRomanNumerals(789) shouldBe "DCCLXXXIX"
        toRomanNumerals(900) shouldBe "CM"
        toRomanNumerals(1009) shouldBe "MIX"
        toRomanNumerals(1066) shouldBe "MLXVI"
        toRomanNumerals(1776) shouldBe "MDCCLXXVI"
        toRomanNumerals(1918) shouldBe "MCMXVIII"
        toRomanNumerals(1944) shouldBe "MCMXLIV"
        toRomanNumerals(2421) shouldBe "MMCDXXI"
        toRomanNumerals(3999) shouldBe "MMMCMXCIX"
    }
}