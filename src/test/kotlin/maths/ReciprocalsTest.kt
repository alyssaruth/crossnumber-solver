package maths

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class ReciprocalsTest {
    @Test
    fun `Should be able to sum reciprocals`() {
        reciprocalSum(listOf(2, 2)) shouldBe 1
        reciprocalSum(listOf(3, 3, 3)) shouldBe 1
        reciprocalSum(listOf(2, 4)) shouldBe 0.75
    }
}