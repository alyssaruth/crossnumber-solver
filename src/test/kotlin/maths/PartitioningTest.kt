package maths

import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import kotlin.test.Test

class PartitioningTest {
    @Test
    fun `Integer partitioning`() {
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

        println(80.distinctIntegerPartitions())
    }
}