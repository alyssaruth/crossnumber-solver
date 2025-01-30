package maths

import io.kotest.matchers.shouldBe
import java.math.BigInteger
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

    @org.junit.jupiter.api.Test
    fun `Should compute and reverse factorials`() {
        factorial(3) shouldBe 6.toBigInteger()
        factorial(4) shouldBe 24.toBigInteger()
        factorial(5) shouldBe 120.toBigInteger()
        factorial(6) shouldBe 720.toBigInteger()
        factorial(20) shouldBe 2432902008176640000.toBigInteger()
        factorial(25) shouldBe BigInteger("15511210043330985984000000")
        factorial(100) shouldBe BigInteger("93326215443944152681699238856266700490715968264381621468592963895217599993229915608941463976156518286253697920827223758251185210916864000000000000000000000000")

        reverseFactorial(2432902008176640000) shouldBe 20
        reverseFactorial(720) shouldBe 6
        reverseFactorial(120) shouldBe 5
        reverseFactorial(24) shouldBe 4
        reverseFactorial(6) shouldBe 3

        reverseFactorial(7) shouldBe null
    }
}