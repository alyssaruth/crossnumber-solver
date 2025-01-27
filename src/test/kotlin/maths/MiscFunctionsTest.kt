package maths

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class MiscFunctionsTest {
    @Test
    fun `Degrees to fahrenheit`() {
        degreesToFahrenheit(0) shouldBe 32
        degreesToFahrenheit(50) shouldBe 122
        degreesToFahrenheit(100) shouldBe 212
    }
}