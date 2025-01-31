package maths

import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class PascalsTriangleTest {
    @Test
    fun `Should generate Pascals triangle correctly`() {
        val expected = listOf(
            listOf(1),
            listOf(1, 1),
            listOf(1, 2, 1),
            listOf(1, 3, 3, 1),
            listOf(1, 4, 6, 4, 1),
            listOf(1, 5, 10, 10, 5, 1),
            listOf(1, 6, 15, 20, 15, 6, 1),
            listOf(1, 7, 21, 35, 35, 21, 7, 1)
        ).map { it.map(Int::toBigInteger) }

        generateTriangle(7).shouldContainExactly(expected)
    }

    @Test
    fun `Should be able to count how many times a number occurs in Pascals triangle`() {
        countNumberInPascalsTriangle(2) shouldBe 1
        countNumberInPascalsTriangle(3) shouldBe 2
        countNumberInPascalsTriangle(6) shouldBe 3
        countNumberInPascalsTriangle(10) shouldBe 4
        countNumberInPascalsTriangle(120) shouldBe 6
        countNumberInPascalsTriangle(3003) shouldBe 8
    }
}