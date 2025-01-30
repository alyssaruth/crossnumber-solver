package maths

import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class PartitioningTest {
    @Test
    fun `Integer partitioning - distinct`() {
        10.distinctIntegerPartitions().shouldContainExactlyInAnyOrder(
            listOf(1, 2, 3, 4),
            listOf(1, 2, 7),
            listOf(1, 3, 6),
            listOf(1, 4, 5),
            listOf(1, 9),
            listOf(2, 3, 5),
            listOf(2, 8),
            listOf(3, 7),
            listOf(4, 6)
        )
    }

    @Test
    fun `Integer partitioning - not distinct`() {
        val distinct = 10.distinctIntegerPartitions()
        val nonDistinct = listOf(
            listOf(5, 5),
            listOf(1, 1, 8),
            listOf(2, 2, 6),
            listOf(2, 4, 4),
            listOf(3, 3, 4),
            listOf(1, 1, 1, 7),
            listOf(1, 1, 2, 6),
            listOf(1, 1, 3, 5),
            listOf(1, 1, 4, 4),
            listOf(1, 2, 2, 5),
            listOf(1, 3, 3, 3),
            listOf(2, 2, 2, 4),
            listOf(2, 2, 3, 3),
            listOf(1, 1, 1, 1, 6),
            listOf(1, 1, 1, 2, 5),
            listOf(1, 1, 1, 3, 4),
            listOf(1, 1, 2, 2, 4),
            listOf(1, 1, 2, 3, 3),
            listOf(1, 2, 2, 2, 3),
            listOf(2, 2, 2, 2, 2),
            listOf(1, 1, 1, 1, 1, 5),
            listOf(1, 1, 1, 1, 2, 4),
            listOf(1, 1, 1, 1, 3, 3),
            listOf(1, 1, 1, 2, 2, 3),
            listOf(1, 1, 2, 2, 2, 2),
            listOf(1, 1, 1, 1, 1, 1, 4),
            listOf(1, 1, 1, 1, 1, 2, 3),
            listOf(1, 1, 1, 1, 2, 2, 2),
            listOf(1, 1, 1, 1, 1, 1, 1, 3),
            listOf(1, 1, 1, 1, 1, 1, 2, 2),
            listOf(1, 1, 1, 1, 1, 1, 1, 1, 2),
            listOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 1)
        )

        10.integerPartitions().shouldContainExactlyInAnyOrder(distinct + nonDistinct)
    }

    @Test
    fun `Should be able to calculate partitions of a desired length`() {
        9.integerPartitions(ofLength = 2).shouldContainExactlyInAnyOrder(
            listOf(1, 8),
            listOf(2, 7),
            listOf(3, 6),
            listOf(4, 5)
        )

        10.integerPartitions(ofLength = 2).shouldContainExactlyInAnyOrder(
            listOf(1, 9),
            listOf(2, 8),
            listOf(3, 7),
            listOf(4, 6),
            listOf(5, 5)
        )

        10.integerPartitions(ofLength = 3).shouldContainExactlyInAnyOrder(
            listOf(1, 1, 8),
            listOf(1, 2, 7),
            listOf(1, 3, 6),
            listOf(1, 4, 5),
            listOf(2, 2, 6),
            listOf(2, 3, 5),
            listOf(2, 4, 4),
            listOf(3, 3, 4)
        )
    }

    @Test
    fun `Should efficiently calculate a partition of a specified length`() {
        50000.integerPartitions(ofLength = 2).size shouldBe 25000
        50001.integerPartitions(ofLength = 2).size shouldBe 25000
    }
}