import com.github.alyssaruth.ClueId
import com.github.alyssaruth.Orientation
import com.github.alyssaruth.parseGrid
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test


class GridTest {
    @Test
    fun `Should parse and pretty print a grid`() {
        val gridStr = """
            ###
            ..#
            .#.
        """.trimIndent()

        val grid = parseGrid(gridStr)
        grid.prettyString() shouldBe gridStr
    }

    @Test
    fun `Should rotate a grid correctly`() {
        val gridStr = """
            123
            456
            789
        """.trimIndent()

        val expected = """
            741
            852
            963
        """.trimIndent()

        val grid = parseGrid(gridStr)
        grid.rotate().prettyString() shouldBe expected
    }

    @Test
    fun `Should identify a grid without rotational symmetry`() {
        val invalid = """
            .....####....#
            .#........##.#
            .##.###.......
            ........#.##.#
            .######.#....#
            ............#.
            .#.###..#.#...
            ...#.#..###.#.
            .#............
            #....#.######.
            #.##.#........
            .......###.##.
            #.##........#.
            #.....###.....
        """.trimIndent()

        val ex = shouldThrow<IllegalArgumentException> { parseGrid(invalid).validate() }
        ex.message shouldBe "Grid is not rotationally symmetric!"
    }

    @Test
    fun `Should reject a grid that is not square`() {
        val rectangle = """
            ###
            ...
            ...
            ###
        """.trimIndent()

        val ex = shouldThrow<IllegalArgumentException> { parseGrid(rectangle).validate() }
        ex.message shouldBe "Grid is not square, detected dimensions: 3x4"
    }

    @Test
    fun `Should reject a grid containing invalid characters`() {
        val invalid = """
            #1#
            2.2
            #1#
        """.trimIndent()

        val ex = shouldThrow<IllegalArgumentException> { parseGrid(invalid).validate() }
        ex.message shouldBe "Invalid character(s) detected: [1, 2, 2, 1]"
    }

    @Test
    fun `Should validate a correct grid`() {
        shouldNotThrowAny { parseGrid(VALID_GRID).validate() }
    }

    @Test
    fun `Should detect clues correctly`() {
        val clues = parseGrid(VALID_GRID).detectClues()
        val idToLength = clues.associateBy { it.clueId }.mapValues { (_, clue) -> clue.squares.size }

        idToLength.shouldContainExactly(
            mapOf(
                ClueId(1, Orientation.ACROSS) to 5,
                ClueId(5, Orientation.ACROSS) to 5,
                ClueId(8, Orientation.ACROSS) to 8,
                ClueId(10, Orientation.ACROSS) to 7,
                ClueId(11, Orientation.ACROSS) to 8,
                ClueId(12, Orientation.ACROSS) to 4,
                ClueId(15, Orientation.ACROSS) to 12,
                ClueId(19, Orientation.ACROSS) to 2,
                ClueId(20, Orientation.ACROSS) to 3,
                ClueId(21, Orientation.ACROSS) to 3,
                ClueId(23, Orientation.ACROSS) to 2,
                ClueId(24, Orientation.ACROSS) to 12,
                ClueId(26, Orientation.ACROSS) to 4,
                ClueId(27, Orientation.ACROSS) to 8,
                ClueId(29, Orientation.ACROSS) to 7,
                ClueId(31, Orientation.ACROSS) to 8,
                ClueId(34, Orientation.ACROSS) to 5,
                ClueId(35, Orientation.ACROSS) to 5,

                ClueId(1, Orientation.DOWN) to 9,
                ClueId(2, Orientation.DOWN) to 2,
                ClueId(3, Orientation.DOWN) to 4,
                ClueId(4, Orientation.DOWN) to 2,
                ClueId(5, Orientation.DOWN) to 3,
                ClueId(6, Orientation.DOWN) to 7,
                ClueId(7, Orientation.DOWN) to 5,
                ClueId(9, Orientation.DOWN) to 8,
                ClueId(13, Orientation.DOWN) to 2,
                ClueId(14, Orientation.DOWN) to 5,
                ClueId(16, Orientation.DOWN) to 5,
                ClueId(17, Orientation.DOWN) to 8,
                ClueId(18, Orientation.DOWN) to 9,
                ClueId(22, Orientation.DOWN) to 7,
                ClueId(25, Orientation.DOWN) to 2,
                ClueId(26, Orientation.DOWN) to 5,
                ClueId(28, Orientation.DOWN) to 4,
                ClueId(30, Orientation.DOWN) to 3,
                ClueId(32, Orientation.DOWN) to 2,
                ClueId(33, Orientation.DOWN) to 2,
            )
        )
    }
}