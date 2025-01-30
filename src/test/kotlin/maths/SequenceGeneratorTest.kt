package maths

import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class SequenceGeneratorTest {
    @Test
    fun `Consecutive squares and cubes`() {
        squaresUpTo(100) shouldBe listOf(1, 4, 9, 16, 25, 36, 49, 64, 81, 100)
        cubesUpTo(100) shouldBe listOf(1, 8, 27, 64)
    }

    @Test
    fun `sum of consecutive primes`() {
        isSumOfConsecutive(3, 2, ::primesUpTo)(11 + 13 + 17) shouldBe true
        isSumOfConsecutive(3, 2, ::primesUpTo)(11 + 13 + 15) shouldBe false
        isSumOfConsecutive(7, 3, ::primesUpTo)(947) shouldBe true
    }

    @Test
    fun `product of consecutive primes`() {
        (100L..999L).filter(isProductOfConsecutive(3, 3, ::primesUpTo)).shouldContainExactly(105, 385)
    }

}