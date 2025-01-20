import com.github.alyssaruth.parseGrid
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

private val VALID_GRID = """
            .....###.....#
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

class GridUtilsTest {
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
}