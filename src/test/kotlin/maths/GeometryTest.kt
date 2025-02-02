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
    fun `Should compute the max regions that can be formed by joining N points on a circle with straight lines`() {
        (1..10).map(::maximumRegionsByJoiningPointsOnACircle).shouldContainExactly(
            1, 2, 4, 8, 16, 31, 57, 99, 163, 256
        )
    }

    @Test
    fun `Should compute the number of straight lines passing through at least two points in an NxN grid`() {
        (2..10).map(::countStraightLinesThroughGrid).shouldContainExactly(
            6, 20, 62, 140, 306, 536, 938, 1492, 2306
        )
    }

    @Test
    fun `Should count k-faces of an n-dimensional hypercube`() {
        facesOfAHypercube(0, 3) shouldBe 8  // vertices
        facesOfAHypercube(1, 3) shouldBe 12 // edges
        facesOfAHypercube(2, 3) shouldBe 6
        facesOfAHypercube(3, 3) shouldBe 1

        facesOfAHypercube(0, 4) shouldBe 16
        facesOfAHypercube(1, 4) shouldBe 32
        facesOfAHypercube(2, 4) shouldBe 24
        facesOfAHypercube(3, 4) shouldBe 8
        facesOfAHypercube(4, 4) shouldBe 1
    }
}