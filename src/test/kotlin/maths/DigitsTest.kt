package maths

import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.math.BigInteger

class DigitsTest {
    @Test
    fun `Should be able to get the digits of a BigInteger`() {
        BigInteger.TWO.pow(80).digits()
            .shouldContainExactly(1, 2, 0, 8, 9, 2, 5, 8, 1, 9, 6, 1, 4, 6, 2, 9, 1, 7, 4, 7, 0, 6, 1, 7, 6)
    }

    @Test
    fun `Should be able to take prefix or suffix of a number`() {
        1234567L.firstNDigits(1) shouldBe 1
        1234567L.firstNDigits(2) shouldBe 12

        1234567L.lastNDigits(3) shouldBe 567
    }

    @Test
    fun `Should report whether a number contains specified digit`() {
        containsDigit(0)(1044) shouldBe true
        containsDigit(1)(1044) shouldBe true
        containsDigit(4)(1044) shouldBe true
        containsDigit(2)(1044) shouldBe false
        containsDigit(3)(1044) shouldBe false
        containsDigit(5)(1044) shouldBe false
    }

    @Test
    fun `Should find all permutations from changing one digit`() {
        permuteDigits(10).shouldContainExactlyInAnyOrder(
            11,
            12,
            13,
            14,
            15,
            16,
            17,
            18,
            19,
            20,
            30,
            40,
            50,
            60,
            70,
            80,
            90
        )

        permuteDigits(100).size shouldBe 9 + 9 + 8
        permuteDigits(9876).size shouldBe (3 * 9) + 8
        permuteDigits(77777777777777).size shouldBe (13 * 9) + 8
    }
}