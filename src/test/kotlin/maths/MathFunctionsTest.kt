package maths

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class MathFunctionsTest {
    @Test
    fun `Should correctly (and performantly) check prime numbers`() {
        isPrime(2) shouldBe true
        isPrime(3) shouldBe true
        isPrime(5) shouldBe true
        isPrime(7) shouldBe true
        isPrime(11) shouldBe true
        isPrime(2772403811878331) shouldBe true

        isPrime(1) shouldBe false
        isPrime(4) shouldBe false
        isPrime(9) shouldBe false
        isPrime(2772403811878333) shouldBe false
    }

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
    fun `Should correctly prime factorise`() {
        19L.primeFactors() shouldBe listOf(19)
        1024L.primeFactors() shouldBe listOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2)
        78204L.primeFactors() shouldBe listOf(2, 2, 3, 7, 7, 7, 19)

        42125857312073.primeFactors() shouldBe listOf(6328831, 6656183)
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
}