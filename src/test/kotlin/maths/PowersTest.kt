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
}