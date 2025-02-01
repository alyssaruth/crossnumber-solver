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

    @Test
    fun `Squares on nxn chessboard`() {
        squaresOnNByNChessboard(1) shouldBe 1
        squaresOnNByNChessboard(2) shouldBe 5
        squaresOnNByNChessboard(3) shouldBe 14
        squaresOnNByNChessboard(8) shouldBe 204
    }
}