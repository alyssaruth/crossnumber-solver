package puzzles

import kotlinx.datetime.Instant
import maths.canBeWrittenInSomeBaseAs
import maths.containsDigit
import maths.digitProduct
import maths.digits
import maths.digitsSameExceptOne
import maths.hasDigitSum
import maths.hasUniqueDigits
import maths.isFibonacci
import maths.isMultipleOf
import maths.isPalindrome
import maths.isPerfect
import maths.isPrime
import maths.isSquare
import maths.isSumOfConsecutive
import maths.isTriangleNumber
import maths.longDigits
import maths.nextPrime
import maths.primeFactors
import maths.primesUpTo
import maths.reversed
import maths.sorted
import maths.toRomanNumerals
import solver.ClueConstructor
import solver.ClueId
import solver.Crossnumber
import solver.Orientation
import solver.RAM_THRESHOLD
import solver.clue.ContextualClue
import solver.clue.calculationWithReference
import solver.clue.emptyClue
import solver.clue.equalToNumberOfClueWithAnswer
import solver.clue.equalsSomeOther
import solver.clue.isDifferenceBetween
import solver.clue.isEqualTo
import solver.clue.isFactorOf
import solver.clue.isMultipleOf
import solver.clue.isProductOf
import solver.clue.simpleClue
import solver.clue.singleReference
import solver.clue.tripleReference
import solver.clueMap
import solver.digitReducer.DigitReducerConstructor
import solver.digitReducer.allDigits
import solver.digitReducer.simpleReducer
import solver.factoryCrossnumber

/**
 * https://chalkdustmagazine.com/regulars/100-prize-crossnumber-issue-01/
 */
fun main() {
    CROSSNUMBER_1.solve()
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

private val digitReducers: List<DigitReducerConstructor> = listOf(
    "6D".simpleReducer(allDigits()) { it < 9 }
)

private val clueMap: Map<String, ClueConstructor> = clueMap(
    *"1A".isProductOf("4D", "18D"),
    "5A" to simpleClue(isMultipleOf(101)),
    *"7A".isDifferenceBetween("10D", "11D"),
    "9A" to simpleClue { value -> isPalindrome(value) && containsDigit(0)(value) },
    *"10A".singleReference("24A") { other -> 100000 - (other * other.reversed()) },
    "13A" to tripleReference("35A", "8D", "17A") { a35, d8, a17 -> (a35 - d8) * a17 },
    *"15A".calculationWithReference("13D") { value, other -> isPerfect(value * other) },
    "16A" to simpleClue { n: Long -> n.primeFactors().size == 2 },
    "17A" to simpleClue(::isTriangleNumber),
    *"19A".isFactorOf("6D"),
    *"20A".singleReference("30A") { it + 5134240 },
    "22A" to simpleClue(isSumOfConsecutive(7, digits = 3, ::primesUpTo)),
    "23A" to simpleClue { toRomanNumerals(it).sorted() == "ILXXX" },
    "24A" to isEqualTo(733626510400L.primeFactors().max()),
    "25A" to simpleClue(::isSquare),
    *"27A".singleReference("7A", ::digitProduct),
    "28A" to simpleClue(isMultipleOf(107)),
    "30A" to isEqualTo(Instant.parse("1970-01-02T01:29:41+00:00").epochSeconds),
    "32A" to canBeWrittenInSomeBaseAs(5331005655, 10),
    "35A" to simpleClue { value -> value == 1 + (3 * value.reversed()) },
    "36A" to digitsSameExceptOne(10),

    *"1D".singleReference("3D") { it - 700 },
    "2D" to simpleClue(hasDigitSum(16)),
    "3D" to simpleClue(::isFibonacci),
    "4D".equalsSomeOther(),
    "5D" to simpleClue { value -> isSquare(value) && hasUniqueDigits(10)(value) },
    "6D" to simpleClue(::sixDown),
    *"8D".singleReference("25A") { nextPrime(it) },
    "10D" to simpleClue { n -> n > 9990000000 && isPrime(n) && nextPrime(n).toString().length > 10 },
    "11D" to simpleClue(isMultipleOf(396533)),
    "12D" to simpleClue { 3 * "1$it".toLong() == "${it}1".toLong() },
    "13D" to emptyClue(), // Covered by 15A
    "14D" to ::FourteenDown,
    "17D" to isEqualTo(42),
    "18D" to simpleClue(isMultipleOf(5)),
    "21D" to equalToNumberOfClueWithAnswer(Orientation.DOWN, 91199),
    "26D" to isEqualTo(4 + 8 + 6 + 20 + 12),
    *"27D".singleReference("29D") { it + 2 },
    "29D" to simpleClue { it.toString().first() == it.toString().last() },
    *"31D".isMultipleOf("24A"),
    "33D" to simpleClue { hasUniqueDigits(3)(it) && it.longDigits().all { it > 0 && isSquare(it) } },
    "34D" to simpleClue(::isSquare)
)

val CROSSNUMBER_1 = factoryCrossnumber(grid, clueMap, digitReducers)

/**
 * This number’s first digit tells you how many 0s are in this number, the second digit how many 1s, the third digit how many 2s, and so on
 */
private fun sixDown(value: Long): Boolean {
    val digits = value.digits()
    return digits.indices.all { i -> digits.count { it == i } == digits[i] }
}

/**
 * The factorial of 17D divided by the factorial of 16A
 */
class FourteenDown(crossnumber: Crossnumber) : ContextualClue(crossnumber) {
    private val d17s = lookupAnswers(ClueId(17, Orientation.DOWN))
    private val a16s = lookupAnswers(ClueId(16, Orientation.ACROSS))

    override fun check(value: Long): Boolean {
        if (d17s == null || a16s == null) {
            return true
        }

        val size = d17s.size * a16s.size
        if (size > RAM_THRESHOLD) {
            return true
        }

        val possibles =
            d17s.flatMap { d17 ->
                a16s.mapNotNull { a16 ->
                    if (d17 <= a16) null else ((a16 + 1)..d17).fold(1L, Long::times)
                }
            }.toSet()

        return possibles.contains(value)
    }
}
