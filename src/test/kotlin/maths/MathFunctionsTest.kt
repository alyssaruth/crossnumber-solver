package maths

import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class MathFunctionsTest {
    @Test
    fun `Should correctly (and performantly) identify triangle numbers`() {
        isTriangleNumber(1) shouldBe true
        isTriangleNumber(3) shouldBe true
        isTriangleNumber(6) shouldBe true
        isTriangleNumber(10) shouldBe true
        isTriangleNumber(15) shouldBe true
        isTriangleNumber(125000250000) shouldBe true // 500,000th

        isTriangleNumber(2) shouldBe false
        isTriangleNumber(9) shouldBe false
        isTriangleNumber(11) shouldBe false
        isTriangleNumber(125000249999) shouldBe false
        isTriangleNumber(125000250001) shouldBe false
    }

    @Test
    fun `Should correctly test for divisibility`() {
        isMultipleOf(1)(12) shouldBe true
        isMultipleOf(2)(12) shouldBe true
        isMultipleOf(3)(12) shouldBe true
        isMultipleOf(4)(12) shouldBe true
        isMultipleOf(5)(12) shouldBe false
        isMultipleOf(6)(12) shouldBe true
        isMultipleOf(7)(12) shouldBe false
        isMultipleOf(8)(12) shouldBe false
        isMultipleOf(9)(12) shouldBe false
        isMultipleOf(10)(12) shouldBe false
        isMultipleOf(11)(12) shouldBe false
        isMultipleOf(12)(12) shouldBe true
        isMultipleOf(24)(12) shouldBe false
    }

    @Test
    fun `Should correctly identify palindromes`() {
        isPalindrome(123454321) shouldBe true
        isPalindrome(1001) shouldBe true
        isPalindrome(10101) shouldBe true

        isPalindrome(12222) shouldBe false
        isPalindrome(51225) shouldBe false
        isPalindrome(1000) shouldBe false
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
    fun `Should test for square numbers`() {
        isSquare(1) shouldBe true
        isSquare(4) shouldBe true
        isSquare(9) shouldBe true
        isSquare(16) shouldBe true
        isSquare(25) shouldBe true
        isSquare(44304772129489) shouldBe true

        isSquare(24) shouldBe false
        isSquare(26) shouldBe false
        isSquare(44304772129488) shouldBe false
        isSquare(44304772129490) shouldBe false
    }

    @Test
    fun `Should get all divisors of a number`() {
        distinctDivisors(6).shouldContainExactlyInAnyOrder(1, 2, 3, 6)
        distinctDivisors(12).shouldContainExactlyInAnyOrder(1, 2, 3, 4, 6, 12)
        distinctDivisors(25).shouldContainExactlyInAnyOrder(1, 5, 25)
        distinctDivisors(137438691328).shouldContainExactlyInAnyOrder(
            1,
            2,
            4,
            8,
            16,
            32,
            64,
            128,
            256,
            512,
            1024,
            2048,
            4096,
            8192,
            16384,
            32768,
            65536,
            131072,
            262144,
            524287,
            1048574,
            2097148,
            4194296,
            8388592,
            16777184,
            33554368,
            67108736,
            134217472,
            268434944,
            536869888,
            1073739776,
            2147479552,
            4294959104,
            8589918208,
            17179836416,
            34359672832,
            68719345664,
            137438691328
        )
    }

    @Test
    fun `Should test for perfect numbers`() {
        isPerfect(6) shouldBe true
        isPerfect(28) shouldBe true
        isPerfect(496) shouldBe true
        isPerfect(8128) shouldBe true
        isPerfect(137438691328) shouldBe true
        isPerfect(2305843008139952128) shouldBe true

        isPerfect(5) shouldBe false
        isPerfect(7) shouldBe false
        isPerfect(128) shouldBe false // "Almost-perfect"
        isPerfect(2305843008139952127) shouldBe false
        isPerfect(2305843008139952129) shouldBe false
    }

    @Test
    fun `Should compute and reverse factorials`() {
        factorial(3) shouldBe 6
        factorial(4) shouldBe 24
        factorial(5) shouldBe 120
        factorial(6) shouldBe 720
        factorial(20) shouldBe 2432902008176640000

        reverseFactorial(2432902008176640000) shouldBe 20
        reverseFactorial(720) shouldBe 6
        reverseFactorial(120) shouldBe 5
        reverseFactorial(24) shouldBe 4
        reverseFactorial(6) shouldBe 3

        reverseFactorial(7) shouldBe null
    }

    @Test
    fun `Should test being a power of a number`() {
        isPowerOf(3)(3) shouldBe true
        isPowerOf(3)(9) shouldBe true
        isPowerOf(3)(27) shouldBe true
        isPowerOf(3)(27) shouldBe true
        isPowerOf(3)(1594323) shouldBe true
    }
}