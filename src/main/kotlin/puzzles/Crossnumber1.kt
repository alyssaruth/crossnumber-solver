package puzzles

import maths.containsDigit
import maths.digits
import maths.hasDigitSum
import maths.hasUniqueDigits
import maths.isEqualTo
import maths.isMultipleOf
import maths.isPalindrome
import maths.isPrime
import maths.isSquare
import maths.isTriangleNumber
import maths.nextPrime
import maths.primeFactors
import solver.ClueId
import solver.Orientation
import kotlinx.datetime.Instant
import maths.isFibonacci
import maths.isPerfect
import maths.product
import maths.reversed
import maths.toRomanNumerals
import solver.ClueConstructor
import solver.factoryCrossnumber
import solver.simpleClues
import solver.simpleReference

/**
 * https://chalkdustmagazine.com/regulars/100-prize-crossnumber-issue-01/
 */
fun main() {
    factoryCrossnumber(grid, clueMap).solve()
}

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

private val clueMap: Map<ClueId, List<ClueConstructor>> = mapOf(
    ClueId(1, Orientation.ACROSS) to emptyList(), // TODO - "D4 multiplied by D18"
    ClueId(5, Orientation.ACROSS) to simpleClues(isMultipleOf(101)),
    ClueId(7, Orientation.ACROSS) to emptyList(), // TODO - "The difference between 10D and 11D"
    ClueId(9, Orientation.ACROSS) to simpleClues(::isPalindrome, containsDigit(0)),
    ClueId(10, Orientation.ACROSS) to emptyList(), // TODO - "Subtract 24A multiplied by 24A backwards from 100000"
    ClueId(13, Orientation.ACROSS) to emptyList(), // TODO - "Subtract 8D from 35A then multiply by 17A"
    ClueId(15, Orientation.ACROSS) to
            simpleReference(
                ClueId(
                    13,
                    Orientation.DOWN
                )
            ) { value, other -> isPerfect(value * other) },
    ClueId(16, Orientation.ACROSS) to simpleClues({ n: Long -> n.primeFactors().size == 2 }),
    ClueId(17, Orientation.ACROSS) to simpleClues(::isTriangleNumber),
    ClueId(19, Orientation.ACROSS) to
            simpleReference(
                ClueId(
                    6,
                    Orientation.DOWN
                )
            ) { value, other -> other % value == 0L },
    ClueId(
        20,
        Orientation.ACROSS
    ) to emptyList(), // TODO - "30A more than the largest number which cannot be written as the sum of distinct fourth powers"
    ClueId(22, Orientation.ACROSS) to emptyList(), // TODO - "The sum of seven consecutive primes"
    ClueId(
        23,
        Orientation.ACROSS
    ) to simpleClues({ toRomanNumerals(it).toCharArray().sorted().joinToString("") == "ILXXX" }),
    ClueId(24, Orientation.ACROSS) to simpleClues(isEqualTo(733626510400L.primeFactors().max())),
    ClueId(25, Orientation.ACROSS) to simpleClues(::isSquare),
    ClueId(27, Orientation.ACROSS) to simpleReference(
        ClueId(
            7,
            Orientation.ACROSS
        )
    ) { value, other -> value == other.digits().product() },
    ClueId(28, Orientation.ACROSS) to simpleClues(isMultipleOf(107)),
    ClueId(30, Orientation.ACROSS) to simpleClues(isEqualTo(Instant.parse("1970-01-02T01:29:41+00:00").epochSeconds)),
    ClueId(
        32,
        Orientation.ACROSS
    ) to emptyList(), // TODO - When written in a base other than 10, this number is 5331005655
    ClueId(35, Orientation.ACROSS) to simpleClues({ value -> value == 1 + (3 * value.reversed()) }),
    ClueId(36, Orientation.ACROSS) to simpleClues(hasUniqueDigits(2)),

    ClueId(1, Orientation.DOWN) to simpleReference(
        ClueId(
            3,
            Orientation.DOWN
        )
    ) { value, other -> value == other - 700 },
    ClueId(2, Orientation.DOWN) to simpleClues(hasDigitSum(16)),
    ClueId(3, Orientation.DOWN) to simpleClues(::isFibonacci),
    ClueId(4, Orientation.DOWN) to emptyList(), // TODO - This is the same as another number in the crossnumber
    ClueId(5, Orientation.DOWN) to simpleClues(::isSquare, hasUniqueDigits(10)),
    ClueId(
        6,
        Orientation.DOWN
    ) to emptyList(), // TODO - This number’s first digit tells you how many 0s are in this number, the second digit how many 1s, the third digit how many 2s, and so on
    ClueId(8, Orientation.DOWN) to simpleReference(ClueId(25, Orientation.ACROSS)) { value, other ->
        value == nextPrime(other)
    },
    ClueId(10, Orientation.DOWN) to simpleClues(::isPrime, { n -> nextPrime(n).toString().length > 10 }),
    ClueId(11, Orientation.DOWN) to simpleClues(isMultipleOf(396533)),
    ClueId(12, Orientation.DOWN) to simpleClues({ 3 * "1$it".toLong() == "${it}1".toLong() }),
    ClueId(13, Orientation.DOWN) to simpleReference(
        ClueId(
            15,
            Orientation.ACROSS
        )
    ) { value, other -> isPerfect(value * other) },
    ClueId(14, Orientation.DOWN) to emptyList(), // TODO - The factorial of 17D divided by the factorial of 16A
    ClueId(17, Orientation.DOWN) to simpleClues(isEqualTo(42)),
    ClueId(18, Orientation.DOWN) to simpleClues(isMultipleOf(5)),
    ClueId(21, Orientation.DOWN) to emptyList(), // TODO - The number of the D clue which has the answer 91199
    ClueId(26, Orientation.DOWN) to simpleClues(isEqualTo(4 + 8 + 6 + 20 + 12)),
    ClueId(27, Orientation.DOWN) to simpleReference(
        ClueId(
            29,
            Orientation.DOWN
        )
    ) { value, other -> value == other + 2 },
    ClueId(29, Orientation.DOWN) to simpleClues({ it.toString().first() == it.toString().last() }),
    ClueId(31, Orientation.DOWN) to simpleReference(
        ClueId(
            24,
            Orientation.ACROSS
        )
    ) { value, other -> value % other == 0L },
    ClueId(33, Orientation.DOWN) to simpleClues(hasUniqueDigits(3), { it.digits().map(Int::toLong).all(::isSquare) }),
    ClueId(34, Orientation.DOWN) to simpleClues(::isSquare)
)