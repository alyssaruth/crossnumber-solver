import com.github.alyssaruth.ClueId
import com.github.alyssaruth.Orientation
import com.github.alyssaruth.factoryCrossnumber
import com.github.alyssaruth.identity
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class CrossnumberTest {
    @Test
    fun `Should validate clue IDs`() {
        val clues = listOf(ClueId(2, Orientation.ACROSS), ClueId(5, Orientation.ACROSS)).associateWith {
            listOf(::identity)
        }

        val ex = shouldThrow<IllegalArgumentException> { factoryCrossnumber(VALID_GRID, clues) }
        ex.message shouldBe "Invalid clue ID(s): [2A]"
    }
}