package puzzles

import maths.integerFactorial
import maths.pow
import solver.clue.isEqualTo
import solver.factoryCrossnumber

/**
 * https://chalkdustmagazine.com/regulars/crossnumber/prize-crossnumber-issue-15/
 */
fun main() {
    CROSSNUMBER_15.solve()
}

private const val r = 5
private const val s = 9
private const val t = 2
private const val u = 4
private const val v = 1
private const val w = 3
private const val x = 6
private const val y = 8
private const val z = 7

private val grid = """
    #...#...#...#
    ..#...#...#..
    .#.....#...#.
    ...#.#...#...
    #....#.#....#
    ..#.#...##...
    .#....#....#.
    ...##...#.#..
    #....#.#....#
    ...#...#.#...
    .#...#.....#.
    ..#...#...#..
    #...#...#...#
""".trimIndent()

private val answers = mapOf<String, Number>(
    "1A" to r.pow(3),
    "3A" to (10 + r).pow(2),
    "5A" to (8 + r) * (20 + r),
    "7A" to r.pow(2),
    "8A" to 11.pow(t),
    "10A" to (z * r * s * t),
    "12A" to z.pow(2),
    "14A" to ((60 + u) * (190 + t)),
    "16A" to (2.pow(z) * r - s),
    "18A" to (t * v * w * s * z),
    "20A" to (2520 / r),
    "21A" to (110 * v),
    "23A" to (337 * x),
    "25A" to (337 * w),
    "26A" to (792 / y),
    "27A" to ((r + x).pow(2)),
    "29A" to (13 * s),
    "31A" to (2022 * v),
    "32A" to (677 * w),
    "34A" to (111 * z),
    "36A" to (u + z).pow(2),
    "38A" to w.pow(3),
    "39A" to 1111 * t,
    "41A" to 67 * r * z,
    "43A" to 606 / x,
    "44A" to 2520 / w,
    "45A" to 151 * w,
    "47A" to (25 - w) * (25 + w),
    "49A" to (110 + y) * (110 + w),
    "52A" to x + y,
    "54A" to 14.pow(t) + z,
    "56A" to r.pow(3) * t,
    "57A" to u.pow(t),
    "58A" to 1 + t + t.pow(3) + t.pow(4) + t.pow(5) + t.pow(8) + t.pow(9),
    "59A" to 990 / r,
    "60A" to (20 + y) * (20 - y),

    "1D" to r * 3,
    "2D" to 2.pow(s),
    "3D" to t * (100 + s),
    "4D" to y * z,
    "5D" to 2424 / y,
    "6D" to s * x,
    "7D" to 29 * z,
    "9D" to 1111 * t,
    "11D" to (10 + v) * (330 + v),
    "13D" to 100 * s,
    "14D" to u * s * r,
    "15D" to 133 * y.pow(2),
    "17D" to 11111 * v,
    "19D" to 3.pow(x),
    "22D" to 110 + v,
    "24D" to 2520 / s,
    "26D" to 103 * s,
    "27D" to (w + y).pow(2),
    "28D" to (t + s).pow(2),
    "30D" to 101 * z,
    "31D" to s.pow(t) * z * x * y,
    "33D" to 101 * w,
    "35D" to integerFactorial(x),
    "37D" to z.pow(4),
    "38D" to u.pow(u) - 1,
    "40D" to (50 + t) * (50 + r),
    "41D" to (x * w * s * z * r) / t,
    "42D" to 111 * u,
    "43D" to 99 + t,
    "46D" to 102 * w,
    "48D" to 2.pow(z) - 1,
    "50D" to 2.pow(x) * r + y,
    "51D" to (99 + t) * (y - r),
    "53D" to x * y,
    "55D" to 2.pow(r) - 1,
    "57D" to t.pow(u)
)

private val clueMap = answers.toMap().mapValues { isEqualTo(it.value.toLong()) }

val CROSSNUMBER_15 = factoryCrossnumber(grid, clueMap)