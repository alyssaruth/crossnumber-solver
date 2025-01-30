package maths

import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class GeometryTest {
    @Test
    fun `Should test for constructible polygons`() {
        (1L..100).filter(::nGonIsConstructible).shouldContainExactly(
            3, 4, 5, 6, 8, 10, 12, 15, 16, 17, 20, 24, 30, 32, 34, 40, 48, 51, 60, 64, 68, 80, 85, 96
        )
    }

    @Test
    fun `Should compute the number of straight lines passing through at least two points in an NxN grid`() {
        countStraightLinesThroughGrid(2) shouldBe 6
        countStraightLinesThroughGrid(3) shouldBe 20
        countStraightLinesThroughGrid(10) shouldBe 2306
    }
}