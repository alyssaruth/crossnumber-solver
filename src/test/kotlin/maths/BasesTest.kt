package maths

import dummyCrossnumber
import io.kotest.matchers.collections.shouldContainExactly
import org.junit.jupiter.api.Test

class BasesTest {
    @Test
    fun `Should find all candidates that can be written as N in some other base`() {
        val clue = canBeWrittenInSomeBaseAs(256, 3)
        clue(dummyCrossnumber(emptyMap())).knownPossibilities()!!.shouldContainExactly(
            139, // Base 7
            174, // Base 8
            213, // Base 9
            303, // Base 11
            354, // Base 12
            409, // Base 13
            468, // Base 14
            531, // Base 15
            598, // Base 16
            669, // Base 17
            744, // Base 18
            823, // Base 19
            906, // Base 20
            993 // Base 21
        )
    }
}