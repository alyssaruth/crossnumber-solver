package puzzles

import maths.digits
import maths.hasDigitSum
import maths.isCoprimeWith
import maths.isCube
import maths.isFibonacci
import maths.isMultipleOf
import maths.isOdd
import maths.isPalindrome
import maths.isPrime
import maths.isSquare
import maths.primeFactors
import solver.clue.calculationWithDualReference
import solver.clue.calculationWithReference
import solver.clue.isAnagramOf
import solver.clue.isEqualTo
import solver.clue.isMultipleOf
import solver.clue.isNotEqualTo
import solver.clue.makeCalculationWithReferences
import solver.clue.plus
import solver.clue.simpleClue
import solver.clue.singleReference
import solver.clueMap
import solver.factoryCrossnumber
import kotlin.math.abs

/**
 * https://chalkdustmagazine.com/regulars/crossnumber/prize-crossnumber-issue-13/
 */
fun main() {
    CROSSNUMBER_13.solve()
}

private val grid = """
    #...#...#...#
    ..#...#...#..
    .###.###.###.
    ..#...#...#..
    #...#...#...#
    ..#...#...#..
    .###.###.###.
    ..#...#...#..
    #...#...#...#
    ..#...#...#..
    .###.###.###.
    ..#...#...#..
    #...#...#...#
""".trimIndent()

private val clueMap = clueMap(
    "1A" to (simpleClue(isMultipleOf(13)) + simpleClue(::isSquare)),
    *"1D".calculationWithDualReference("31A", "41A") { d1, a31, a41 -> (d1 == a31) or (d1 == a41) },
    *"1D".calculationWithReference("7A") { d1, a7 -> isPrime(d1) xor isOdd(a7) },
    *"2D".calculationWithDualReference("3D", "4D") { d2, d3, d4 -> (d2 == d3) or (d2 == d4) },
    *"3A".calculationWithReference("3D") { x, y -> hasDigitSum(27)(x) || hasDigitSum(27)(y) },
    *"4D".calculationWithReference("9D") { x, y -> isCube(x) xor isCube(y) },
    *"4D".calculationWithReference("11D") { x, y -> isSquare(x) xor isSquare(y) },
    *"5A".calculationWithDualReference("1A", "3A") { a5, a1, a3 -> (a5 == a1) xor (a5 == a3) },
    "5D" to makeCalculationWithReferences("2D", "6D", "1D") { (d5, d2, d6, d1) -> (d5 == d2) xor (d6 == d1) },
    *"7A".calculationWithDualReference("54A", "12A") { a7, a54, a12 -> (a7 == a54) xor (a7 == a12) },
    *"8A".calculationWithDualReference("3A", "10A") { a8, a3, a10 -> (a8 == a3) xor (a8 == a10) },
    *"10A".calculationWithDualReference("3A", "4D") { a10, a3, d4 ->
        isMultipleOf(a10)(a3) || isMultipleOf(d4)(a3)
    },
    *"12A".calculationWithDualReference("54A", "48A") { a12, a54, a48 -> (a12 == a54) xor (a12 == a48) },
    "13D" to simpleClue(hasDigitSum(17)),
    "7D" to simpleClue(hasDigitSum(17)),
    "14A" to simpleClue {
        !isPrime(it) && it.primeFactors().none { factor -> isPrime(factor) && (10..99).contains(factor) }
    },
    *"15D".calculationWithReference("16A") { d15, a16 -> d15 + a16 == 742L && abs(d15 - a16) == 38L },
    *"25A".calculationWithDualReference("16A", "17D") { a25, a16, d17 -> (a25 == a16) xor (a25 == d17) },
    *"16D".calculationWithReference("21A") { d16, a21 -> isMultipleOf(100)(d16) xor isMultipleOf(100)(a21) },
    *"16D".calculationWithReference("21A") { d16, a21 -> hasDigitSum(5)(d16) xor hasDigitSum(5)(a21) },
    "17D" to makeCalculationWithReferences("15D", "1D", "48A") { (d17, d15, d1, a48) ->
        !isCoprimeWith(d17)(d15) == (d17 == d1 * a48)
    },
    "18A" to simpleClue(::isPalindrome) + simpleClue(hasDigitSum(2)),
    "18D" to simpleClue(::isPalindrome) + simpleClue(hasDigitSum(2)),
    *"19D".calculationWithReference("27A") { d19, a17 -> isPrime(d19) xor isPrime(a17) },
    "20A" to simpleClue {
        !isPrime(it) && it.primeFactors().none { factor -> isPrime(factor) && (10..99).contains(factor) }
    },
    "20D" to simpleClue(::isPalindrome) + simpleClue(hasDigitSum(16)),
    *"22A".calculationWithDualReference("1D", "48A") { a22, d1, a48 ->
        isMultipleOf(5)(a22) == (a22 == d1 * a48)
    },
    *"23A".singleReference("27A") { it * 6 },
    *"28D".isEqualTo("19D"),
    "24A" to simpleClue(isMultipleOf(8)),
    *"45D".isMultipleOf("24A"),
    *"24D".calculationWithReference("49D") { x, y -> hasDigitSum(5)(x) == hasDigitSum(5)(y) },
    *"29A".calculationWithDualReference("37A", "30D") { a29, a37, d30 -> (a29 == a37) xor isPalindrome(d30) },
    *"29A".calculationWithReference("37A") { a29, a37 -> isPrime(a29) xor isPrime(a37) },
    *"32D".isMultipleOf("41A"),
    *"41D".isMultipleOf("41A"),
    *"35D".isEqualTo("39A"),
    *"35D".isEqualTo("35A"),
    "37D" to simpleClue { isPrime(it) xor hasDigitSum(5)(it) },
    "37D" to simpleClue { it.digits().first() != 1 || it.digits().last() != 1 },
    *"37D".calculationWithReference("30D") { d37, d30 -> hasDigitSum(7)(d37) xor hasDigitSum(7)(d30) },
    *"38A".isMultipleOf("10A"),
    *"39A".isMultipleOf("10A"),
    "40A" to simpleClue(isMultipleOf(100)),
    "45D" to simpleClue(isMultipleOf(100)),
    *"40A".calculationWithReference("45D") { x, y -> !isMultipleOf(x)(y) && !isMultipleOf(y)(x) },
    *"40A".calculationWithReference("45D") { x, y -> isSquare(x) == isSquare(y) },
    *"42A".calculationWithReference("39A") { x, y -> isMultipleOf(9)(x) == isMultipleOf(9)(y) },
    *"42A".isAnagramOf("33A"),
    *"34D".isAnagramOf("33D"),
    *"43D".isMultipleOf("10A"),
    *"26D".isMultipleOf("10A"),
    *"5A".calculationWithDualReference("44A", "36D") { a5, a44, d36 -> (a5 == a44) xor (a5 == d36) },
    *"46A".calculationWithDualReference("40A", "45D") { a46, a40, d45 ->
        isMultipleOf(a46)(a40) xor isMultipleOf(a46)(d45)
    },
    *"47D".calculationWithDualReference("40A", "45D") { d47, a40, d45 ->
        isMultipleOf(d47)(a40) xor isMultipleOf(d47)(d45)
    },
    "48A" to simpleClue(::isCube) + simpleClue(::isSquare),
    "50A" to simpleClue(::isCube) + simpleClue(::isSquare),
    *"49D".calculationWithReference("50D") { x, y -> isMultipleOf(5)(x) xor isMultipleOf(5)(y) },
    *"52A".singleReference("55A") { it + 2 },
    "52A" to simpleClue(hasDigitSum(3)),
    "52D" to simpleClue(::isFibonacci),
    "53D" to simpleClue(::isFibonacci),
    *"54A".calculationWithDualReference("7A", "12A") { a54, a7, a12 -> (a54 == a7) xor (a54 == a12) },
    *"54D".calculationWithDualReference("52D", "53D") { d54, d52, d53 -> isFibonacci(d54) xor (d54 == d52 + d53) },
    *"54D".isNotEqualTo("52D"),
    *"54D".isNotEqualTo("53D"),
    "56A" to simpleClue { isSquare(it) xor isCube(it) },
    "57A" to simpleClue { isSquare(it) xor (it > 200) }
)

val CROSSNUMBER_13 = factoryCrossnumber(grid, clueMap)