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
    fun `Should be able to get middle N digits of a number`() {
        middleNDigits(2, 12344321) shouldBe 44
        middleNDigits(4, 12344321) shouldBe 3443
        middleNDigits(8, 12344321) shouldBe 12344321

        middleNDigits(1, 123454321) shouldBe 5
        middleNDigits(3, 123454321) shouldBe 454
        middleNDigits(9, 123454321) shouldBe 123454321
    }

    @Test
    fun `Should be able to compute digit sums and products`() {
        digitSum(159) shouldBe 15
        digitProduct(2413) shouldBe 24
    }

    @Test
    fun `Should identify numbers with all digits the same except one`() {
        digitsAllSameExceptOne(1111112) shouldBe true
        digitsAllSameExceptOne(373333) shouldBe true

        digitsAllSameExceptOne(1111) shouldBe false
        digitsAllSameExceptOne(12121) shouldBe false
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