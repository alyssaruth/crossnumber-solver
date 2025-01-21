package puzzles

import com.github.alyssaruth.Clue
import com.github.alyssaruth.ClueId
import com.github.alyssaruth.Orientation
import com.github.alyssaruth.containsDigit
import com.github.alyssaruth.factoryCrossnumber
import com.github.alyssaruth.isEqualTo
import com.github.alyssaruth.isMultipleOf
import com.github.alyssaruth.isPalindrome
import com.github.alyssaruth.isSquare
import com.github.alyssaruth.isTriangleNumber
import com.github.alyssaruth.primeFactors
import kotlinx.datetime.Instant
import org.junit.jupiter.api.Test

/**
 * https://chalkdustmagazine.com/regulars/100-prize-crossnumber-issue-01/
 */
class Crossnumber1 {
    private val grid = """
        #..........####
        #.#.##.###.#...
        #..........#.#.
        ###.#.####.....
        .....#.....#.#.
        .#.#.#..###..#.
        .#.#.##.#..#...
        .#.#.......#.#.
        ...#..#.##.#.#.
        .#..###..#.#.#.
        .#.#.....#.....
        .....####.#.###
        .#.#..........#
        ...#.###.##.#.#
        ####..........#
    """.trimIndent()

    private val clueMap: Map<ClueId, List<Clue>> = mapOf(
        ClueId(1, Orientation.ACROSS) to emptyList(), // TODO - "D4 multiplied by D18"
        ClueId(5, Orientation.ACROSS) to listOf(isMultipleOf(101)),
        ClueId(7, Orientation.ACROSS) to emptyList(), // TODO - "The difference between 10D and 11D"
        ClueId(9, Orientation.ACROSS) to listOf(::isPalindrome, containsDigit(0)),
        ClueId(10, Orientation.ACROSS) to emptyList(), // TODO - "Subtract 24A multiplied by 24A backwards from 100000"
        ClueId(13, Orientation.ACROSS) to emptyList(), // TODO - "Subtract 8D from 35A then multiply by 17A"
        ClueId(15, Orientation.ACROSS) to emptyList(), // TODO - "Multiply this by 13D to get a perfect number"
        ClueId(16, Orientation.ACROSS) to listOf { n: Long -> n.primeFactors().size == 2 },
        ClueId(17, Orientation.ACROSS) to listOf(::isTriangleNumber),
        ClueId(19, Orientation.ACROSS) to emptyList(), // TODO - "A factor of 6D"
        ClueId(
            20,
            Orientation.ACROSS
        ) to emptyList(), // TODO - "30A more than the largest number which cannot be written as the sum of distinct fourth powers"
        ClueId(22, Orientation.ACROSS) to emptyList(), // TODO - "The sum of seven consecutive primes"
        ClueId(
            23,
            Orientation.ACROSS
        ) to emptyList(), // TODO - "When written in roman numerals, this number is an anagram of XILXX"
        ClueId(24, Orientation.ACROSS) to listOf(isEqualTo(733626510400L.primeFactors().max())),
        ClueId(25, Orientation.ACROSS) to listOf(::isSquare),
        ClueId(27, Orientation.ACROSS) to emptyList(), // TODO - "The product of all the digits of 7A"
        ClueId(28, Orientation.ACROSS) to listOf(isMultipleOf(107)),
        ClueId(30, Orientation.ACROSS) to listOf(isEqualTo(Instant.parse("1970-01-02T01:29:41+00:00").epochSeconds)),
    )

    @Test
    fun `Crossnumber #1`() {
        factoryCrossnumber(grid, clueMap).solve()
    }
}