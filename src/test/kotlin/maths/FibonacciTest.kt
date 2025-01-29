package maths

import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class FibonacciTest {
    @Test
    fun `Should compute fibonacci numbers up to specified limit`() {
        fibonacciUpTo(5).shouldContainExactly(1, 1, 2, 3, 5)
        fibonacciUpTo(20).shouldContainExactly(1, 1, 2, 3, 5, 8, 13)
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