package maths

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class PowersTest {
    @Test
    fun `Should calculate modulo powers correctly`() {
        modPow(
            66.toBigInteger(),
            119315717514045L.toBigInteger(),
            119315717514047L.toBigInteger()
        ) shouldBe 1807813901728L.toBigInteger()
    }

    @Test
    fun `Should generate the powers of a number of a desired length`() {
        generatePowers(1)(2) shouldBe listOf(2, 4, 8)
        generatePowers(2)(2) shouldBe listOf(16, 32, 64)
        generatePowers(3)(2) shouldBe listOf(128, 256, 512)

        generatePowers(4)(256) shouldBe emptyList()
        generatePowers(5)(256) shouldBe listOf(65536)
    }
}