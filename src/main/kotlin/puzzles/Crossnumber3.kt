package puzzles

import maths.canBePermutedSuchThat
import maths.canBeWrittenInSomeBaseAs
import maths.countTwinPrimesUpTo
import maths.digitCounts
import maths.digitSum
import maths.digits
import maths.digitsAreStrictlyIncreasing
import maths.distinctDivisors
import maths.distinctIntegerPartitions
import maths.factorialMod
import maths.fibonacciUpTo
import maths.hasWholeNthRoot
import maths.hcf
import maths.inWords
import maths.isFibonacci
import maths.isMultipleOf
import maths.isOdd
import maths.isPalindrome
import maths.isPowerOf
import maths.isPrime
import maths.isProductOfConsecutive
import maths.isSquare
import maths.isSumOfConsecutive
import maths.nthRoot
import maths.primesUpTo
import maths.reciprocalSum
import maths.sorted
import maths.toBinary
import maths.vowels
import solver.ClueConstructor
import solver.clue.asyncEquals
import solver.clue.dualReference
import solver.clue.emptyClue
import solver.clue.isEqualTo
import solver.clue.isGreaterThan
import solver.clue.isHalfTheDifferenceBetween
import solver.clue.isMultipleOf
import solver.clue.isProductOf
import solver.clue.isSumOf
import solver.clue.largest
import solver.clue.plus
import solver.clue.simpleClue
import solver.clue.simplyNot
import solver.clue.singleReference
import solver.clue.smallest
import solver.clueMap
import solver.factoryCrossnumber
import java.math.BigInteger

/**
 * https://chalkdustmagazine.com/regulars/crossnumber/prize-crossnumber-issue-03/
 */
fun main() {
    CROSSNUMBER_3.solve()
}

private val grid = """
    .......#.......
    .#.#.#...#####.
    .#.#.##........
    .#....#...#.##.
    .#.#.#...#.....
    .#..###.###.##.
    .....#...#.##..
    #......#......#
    ..##.#...#.....
    .##.###.###..#.
    .....#...#.#.#.
    .##.#...#....#.
    ........##.#.#.
    .#####...#.#.#.
    .......#.......
""".trimIndent()

private val clueMap: Map<String, ClueConstructor> = clueMap(
    "1A" to simpleClue(isMultipleOf(999)),
    *"5A".isHalfTheDifferenceBetween("45A", "1A"),
    *"7A".isSumOf("13A", "43D"),
    *"9A".isMultipleOf("41A"),
    *"12A".isProductOf("4D", "43D"),
    "13A" to emptyClue(),
    "14A" to emptyClue(),
    "15A" to simpleClue { inWords(it).vowels().sorted() == "aeiou" },
    *"16A".singleReference("8D", ::digitSum),
    "18A" to simpleClue { it.digits().sorted() == listOf(0, 2, 4, 6, 8) },
    "21A" to simpleClue { hcf(it, 756) == 1L && !isPrime(it) },
    *"24A".singleReference("29D", ::digitSum),
    "25A" to simpleClue(isProductOfConsecutive(4, digits = 6, ::fibonacciUpTo)),
    *"26A".isProductOf("14A", "21D"),
    "29A" to largest(simplyNot { BigInteger.TWO.pow(it.toInt()).digits().contains(0) }),
    "30A" to isEqualTo(418), // I am a teapot
    "32A" to simpleClue(::isPrime) + simpleClue(isSumOfConsecutive(25, digits = 5, ::primesUpTo)),
    "35A" to isEqualTo(11), // The number of different nets of a cube (with reflections and rotations being considered as the same net)
    "36A" to simpleClue(::isFibonacci),
    "37A" to canBeWrittenInSomeBaseAs(256, 3),
    "39A" to isEqualTo(789), // Why is 6 afraid of 7?
    "40A" to isEqualTo(8902), // The number of ways to play the first 3 moves (2 white moves, 1 black move) in a game of chess
    "41A" to simpleClue(isMultipleOf(719)),
    "42A" to simpleClue { isPrime(it) && isPrime(it + 2) },
    *"44A".singleReference("29D") { it / 2 },
    *"45A".isSumOf("6D", "8D", "31D", "37D", "43D"),

    "1D" to simpleClue(isPowerOf(2)),
    "2D" to simpleClue(::isPalindrome),
    "3D" to simpleClue { hasWholeNthRoot(3)(it) && nthRoot(it, 3) == distinctDivisors(it).size.toLong() },
    "4D" to simpleClue(isOdd),
    "5D" to simpleClue(::isSquare),
    "6D" to dualReference("5D", "27D") { d5, d27 -> d5 * (d27 - 1) },
    "8D" to simpleClue(isOdd),
    "10D" to simpleClue(isMultipleOf(7)),
    "11D" to asyncEquals { countTwinPrimesUpTo(1_000_000).toLong() },
    *"17D".singleReference("26A") { distinctDivisors(it).size.toLong() },
    *"19D".isGreaterThan("30A"),
    "20D" to simpleClue { it.digitsAreStrictlyIncreasing() && it.digits().all { digit -> isPrime(digit.toLong()) } },
    *"21D".isSumOf("14A", "37A"),
    "22D" to simpleClue(isMultipleOf(27)),
    "23D" to simpleClue { factorialMod(it - 1, it * it) == (it * it) - 1 },
    "24D" to smallest(simplyNot(canBePermutedSuchThat(::isPrime))),
    "27D" to dualReference("5D", "6D") { d5, d6 -> (d6 / d5) + 1 },
    *"28D".isMultipleOf("34D"),
    "29D" to simpleClue(isMultipleOf(7)),
    "31D" to simpleClue(::isPrime) + simpleClue { it.digitCounts().values.toList() == listOf(2, 2, 2) },
    *"33D".singleReference("6D") { it - 1000006 },
    "34D" to smallest(simpleClue { !it.toBinary().isPalindrome() && (it * it).toBinary().isPalindrome() }),
    "37D" to simpleClue { (it * it).digitCounts().keys.sorted() == listOf(1, 2, 3, 4) },
    "38D" to simpleClue(hasWholeNthRoot(3)),
    "39D" to asyncEquals {
        (10..99).filter {
            it.distinctIntegerPartitions().none { partition -> reciprocalSum(partition) == 1.0 }
        }.max().toLong()
    },
    "43D" to smallest(simpleClue { it == 2L * it.digitSum() })
)

val CROSSNUMBER_3 = factoryCrossnumber(grid, clueMap)