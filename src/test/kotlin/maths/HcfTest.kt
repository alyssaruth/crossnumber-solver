package maths

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class HcfTest {
    @Test
    fun `Should compute the HCF of two numbers`() {
        hcf(54463500, 2005830) shouldBe 1710
        hcf(5, 7) shouldBe 1
        hcf(11, 11) shouldBe 11
    }
}