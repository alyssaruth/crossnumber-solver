import com.github.alyssaruth.ClueId
import com.github.alyssaruth.Crossnumber
import com.github.alyssaruth.Orientation
import com.github.alyssaruth.identity
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class CrossnumberTest {
    @Test
    fun `Should validate clue IDs`() {
        val clues = listOf(ClueId(2, Orientation.ACROSS), ClueId(5, Orientation.ACROSS)).map {
            it to listOf(::identity)
        }

        val ex = shouldThrow<IllegalArgumentException> { Crossnumber(VALID_GRID, clues.toMap()).validate() }
        ex.message shouldBe "Invalid clue ID(s): [2A]"
    }
}