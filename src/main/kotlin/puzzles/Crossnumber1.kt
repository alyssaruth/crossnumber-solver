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

private val clueMap: Map<String, List<ClueConstructor>> = mapOf(
    "1A" to emptyList(), // TODO - "D4 multiplied by D18"
    "5A" to simpleClues(isMultipleOf(101)),
    "7A" to emptyList(), // TODO - "The difference between 10D and 11D"
    "9A" to simpleClues(::isPalindrome, containsDigit(0)),
    "10A" to emptyList(), // TODO - "Subtract 24A multiplied by 24A backwards from 100000"
    "13A" to emptyList(), // TODO - "Subtract 8D from 35A then multiply by 17A"
    "15A" to simpleReference("13D") { value, other -> isPerfect(value * other) },
    "16A" to simpleClues({ n: Long -> n.primeFactors().size == 2 }),
    "17A" to simpleClues(::isTriangleNumber),
    "19A" to simpleReference("6D") { value, other -> other % value == 0L },
    "20A" to emptyList(), // TODO - "30A more than the largest number which cannot be written as the sum of distinct fourth powers"
    "22A" to emptyList(), // TODO - "The sum of seven consecutive primes"
    "23A" to simpleClues({ toRomanNumerals(it).toCharArray().sorted().joinToString("") == "ILXXX" }),
    "24A" to simpleClues(isEqualTo(733626510400L.primeFactors().max())),
    "25A" to simpleClues(::isSquare),
    "27A" to simpleReference("7A") { value, other -> value == other.digits().product() },
    "28A" to simpleClues(isMultipleOf(107)),
    "30A" to simpleClues(isEqualTo(Instant.parse("1970-01-02T01:29:41+00:00").epochSeconds)),
    "32A" to emptyList(), // TODO - When written in a base other than 10, this number is 5331005655
    "35A" to simpleClues({ value -> value == 1 + (3 * value.reversed()) }),
    "36A" to simpleClues(hasUniqueDigits(2)),

    "1D" to simpleReference("3D") { value, other -> value == other - 700 },
    "2D" to simpleClues(hasDigitSum(16)),
    "3D" to simpleClues(::isFibonacci),
    "4D" to emptyList(), // TODO - This is the same as another number in the crossnumber
    "5D" to simpleClues(::isSquare, hasUniqueDigits(10)),
    "6D" to emptyList(), // TODO - This number’s first digit tells you how many 0s are in this number, the second digit how many 1s, the third digit how many 2s, and so on
    "8D" to simpleReference("25A") { value, other -> value == nextPrime(other) },
    "10D" to simpleClues(::isPrime, { n -> nextPrime(n).toString().length > 10 }),
    "11D" to simpleClues(isMultipleOf(396533)),
    "12D" to simpleClues({ 3 * "1$it".toLong() == "${it}1".toLong() }),
    "13D" to simpleReference("15A") { value, other -> isPerfect(value * other) },
    "14D" to emptyList(), // TODO - The factorial of 17D divided by the factorial of 16A
    "17D" to simpleClues(isEqualTo(42)),
    "18D" to simpleClues(isMultipleOf(5)),
    "21D" to emptyList(), // TODO - The number of the D clue which has the answer 91199
    "26D" to simpleClues(isEqualTo(4 + 8 + 6 + 20 + 12)),
    "27D" to simpleReference("29D") { value, other -> value == other + 2 },
    "29D" to simpleClues({ it.toString().first() == it.toString().last() }),
    "31D" to simpleReference("24A") { value, other -> value % other == 0L },
    "33D" to simpleClues(hasUniqueDigits(3), { it.digits().map(Int::toLong).all(::isSquare) }),
    "34D" to simpleClues(::isSquare)
)