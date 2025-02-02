package maths

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class CombinatoricsTest {
    @Test
    fun `Can calculate n choose k`() {
        10.choose(5) shouldBe 252
        10.choose(4) shouldBe 210

        50.choose(10) shouldBe 10272278170
    }
}