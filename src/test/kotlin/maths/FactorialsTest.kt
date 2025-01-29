package maths

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class FactorialsTest {
    @Test
    fun `Should calculate a factorial modulo some base`() {
        factorialMod(20, 2) shouldBe 0
        factorialMod(20, 3) shouldBe 0
        factorialMod(20, 5) shouldBe 0
        factorialMod(20, 7) shouldBe 0
        factorialMod(20, 11) shouldBe 0
        factorialMod(20, 13) shouldBe 0
        factorialMod(20, 17) shouldBe 0
        factorialMod(20, 19) shouldBe 0
        factorialMod(20, 23) shouldBe 11
        factorialMod(20, 29) shouldBe 26
    }
}