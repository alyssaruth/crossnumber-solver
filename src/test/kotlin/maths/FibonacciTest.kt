package maths

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class FibonacciTest {
    @Test
    fun `Matrix multiplication should work`() {
        val identity = Matrix2d(1, 0, 0, 1)
        identity * identity shouldBe identity

        Matrix2d(1, 1, 1, 0) * Matrix2d(1, 1, 1, 0) shouldBe Matrix2d(2, 1, 1, 1)
        Matrix2d(1, 2, 3, 4) * Matrix2d(5, 6, 7, 8) shouldBe Matrix2d(19, 22, 43, 50)
    }

    @Test
    fun `Should compute nth power of a matrix`() {
        val fib = Matrix2d(1, 1, 1, 0)

        fib.pow(0) shouldBe Matrix2d(1, 0, 0, 1)
        fib.pow(1) shouldBe fib
        fib.pow(2) shouldBe Matrix2d(2, 1, 1, 1)
        fib.pow(3) shouldBe Matrix2d(3, 2, 2, 1)
        fib.pow(4) shouldBe Matrix2d(5, 3, 3, 2)
        fib.pow(10) shouldBe Matrix2d(89, 55, 55, 34)
        fib.pow(50) shouldBe Matrix2d(20365011074, 12586269025, 12586269025, 7778742049)
    }

    @Test
    fun `Should compute nth fibonacci number`() {
        nthFibonacci(1) shouldBe 1
        nthFibonacci(2) shouldBe 1
        nthFibonacci(3) shouldBe 2
        nthFibonacci(4) shouldBe 3
        nthFibonacci(5) shouldBe 5

        nthFibonacci(80) shouldBe 23416728348467685
    }

    @Test
    fun `Should correctly and efficiently test if a number is a fibonacci number`() {
        isFibonacci(1) shouldBe true
        isFibonacci(2) shouldBe true
        isFibonacci(3) shouldBe true
        isFibonacci(5) shouldBe true
        isFibonacci(8) shouldBe true
        isFibonacci(2880067194370816120) shouldBe true

        isFibonacci(4) shouldBe false
        isFibonacci(6) shouldBe false
        isFibonacci(2880067194370816119) shouldBe false
        isFibonacci(2880067194370816121) shouldBe false
    }
}