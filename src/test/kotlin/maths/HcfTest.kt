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

    @Test
    fun `Should calculate the LCM of two numbers quickly`() {
        lcm(54463500, 2005830) shouldBe 63885685500
        lcm(2005830, 54463500) shouldBe 63885685500
    }

    @Test
    fun `Should calculate the LCM of a list of numbers`() {
        lcm(listOf(12, 18, 30)) shouldBe 180
        lcm(listOf(10, 12, 15, 75)) shouldBe 300
        lcm(listOf(3, 5, 7)) shouldBe 3 * 5 * 7
    }
}