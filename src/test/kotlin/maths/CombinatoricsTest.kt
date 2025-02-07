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

    @Test
    fun `Squares on nxn chessboard`() {
        squaresOnNByNChessboard(1) shouldBe 1
        squaresOnNByNChessboard(2) shouldBe 5
        squaresOnNByNChessboard(3) shouldBe 14
        squaresOnNByNChessboard(8) shouldBe 204
    }
}