package puzzles

import maths.areAnagrams
import maths.bigPow
import maths.digitCounts
import maths.digitSum
import maths.digits
import maths.digitsAllTheSame
import maths.hasDigitProduct
import maths.isMultipleOf
import maths.isOdd
import maths.isPalindrome
import maths.isPowerOf
import maths.isPrime
import maths.isSquare
import maths.sqrtWhole
import maths.sumOfCubesOfDigits
import maths.sumOfNthPowerOfDigits
import solver.clue.calculationWithReference
import solver.clue.emptyClue
import solver.clue.equalsSomeOther
import solver.clue.isEqualTo
import solver.clue.isFactorOf
import solver.clue.isMultipleOf
import solver.clue.isLessThan
import solver.clue.isNotEqualTo
import solver.clue.plus
import solver.clue.simpleClue
import solver.clue.singleReference
import solver.clueMap
import solver.factoryCrossnumber

/**
 * https://chalkdustmagazine.com/regulars/crossnumber/prize-crossnumber-issue-09/
 */
fun main() {
    CROSSNUMBER_9.solve()
}

private val grid = """
    ...............
    .##.###.#..#.#.
    .##.#.#...##.#.
    ..#.#.#....#.#.
    ...............
    .##.###.#....#.
    #........#.....
    .#.#...#...#.#.
    .....#........#
    .#....#.###.##.
    ...............
    .#.#....#.#.#..
    .#.##...#.#.##.
    .#.#..#.###.##.
    ...............
""".trimIndent()

private val clueMap = clueMap(
    "1A" to digitsAllTheSame(15),
    *"8A".isFactorOf("9D"),
    "10A" to simpleClue(::isPalindrome),
    "12A" to simpleClue(::isPrime),
    "14A" to simpleClue(hasDigitProduct(70)),
    "16A" to digitsAllTheSame(15),
    "18A" to simpleClue { it == sumOfNthPowerOfDigits(4)(it) },
    "19A".equalsSomeOther(),
    "25A" to emptyClue(), // TODO - An anagram of a palindrome
    "27A" to simpleClue { it == sumOfCubesOfDigits(it) },
    "28A" to simpleClue { it == sumOfCubesOfDigits(it) },
    *"30A".singleReference("12A") { 2 * it.bigPow(4).longValueExact() },
    "32A" to simpleClue(isMultipleOf(3)),
    "35A" to simpleClue { it == sumOfNthPowerOfDigits(4)(it) },
    "38A" to digitsAllTheSame(15),
    "42A" to simpleClue { it == sumOfNthPowerOfDigits(4)(it) },
    *"43A".isNotEqualTo("41D"),
    "44A" to simpleClue { it == sumOfCubesOfDigits(it) },
    "45A" to simpleClue(isPowerOf(2)) + simpleClue { isMultipleOf(9)(it - 1) },
    "46A" to digitsAllTheSame(15),

    *"1D".isMultipleOf("12A"),
    *"2D".calculationWithReference("3D", ::areAnagrams),
    "3D" to simpleClue(isMultipleOf(3)) + simpleClue { it.digits()[1] == 4 },
    "4D" to emptyClue(), // TODO - An anagram of a palindrome
    *"5D".isFactorOf("10A"),
    "6D" to simpleClue(isMultipleOf(3)),
    "7D" to simpleClue { it.digits().windowed(2).all { (a, b) -> b == a + 1 } },
    *"9D".isEqualTo("10A"),
    *"11D".isMultipleOf("8A"),
    *"13D".singleReference("1D", ::digitSum),
    "15D" to emptyClue(), // TODO - Each digit of this number is either one more than or one third of the previous digit
    "17D" to simpleClue(isMultipleOf(5)),
    *"20D".isMultipleOf("2D"),
    "21D" to simpleClue { !isMultipleOf(3)(it) },
    "22D" to simpleClue(::isSquare),
    *"23D".singleReference("30A") { sqrtWhole(it - 1) }, // TODO - not quite right, should sort this sqrt stuff out
    *"24D".isEqualTo("23D"),
    "26D" to emptyClue(), // TODO - A five-digit number that appears in this crossnumber followed by a three-digit number that appears in this crossnumber
    *"29D".isLessThan("43A"),
    "31D" to simpleClue(::isPrime), // TODO - A prime number that is equal to the average of the previous and next prime
    "33D" to simpleClue { it.digits().windowed(2).all { (a, b) -> b == a + 1 || b == a + 3 } },
    "34D" to simpleClue { it.digits().windowed(2).all { (a, b) -> b == a - 1 || b == a * 3 } },
    "36D" to simpleClue { it.digitCounts().values.any { count -> isOdd(count.toLong()) } },
    "37D" to emptyClue(), // TODO - A three-digit number that appears in this crossnumber followed by a different three-digit number that appears in this crossnumber
    "39D" to simpleClue { it == sumOfCubesOfDigits(it) },
    "40D" to emptyClue(),  // TODO - An anagram of a palindrome
    *"41D".isFactorOf("43A"),
    *"45D".singleReference("6D", ::digitSum)
)

val CROSSNUMBER_9 = factoryCrossnumber(grid, clueMap)