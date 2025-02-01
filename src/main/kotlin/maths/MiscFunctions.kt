package maths

fun degreesToFahrenheit(degrees: Long) = 32 + (degrees * 9) / 5

fun String.sorted() = toCharArray().sorted().joinToString("")

fun String.isPalindrome() = reversed() == this

fun <E> List<List<E>>.allCombinations(): List<List<E>> =
    fold(listOf(emptyList())) { listsSoFar, newSolutions ->
        listsSoFar.flatMap { list -> newSolutions.map { x -> list + x } }
    }

/**
 * There are n^2 1x1 squares, (n-1)^2 2x2 squares and so on.
 *
 * The sum of squares from 1..n can also be expressed as n(n+1)(2n + 1)/6
 */
fun squaresOnNByNChessboard(n: Long) = (n * (n + 1) * ((2 * n) + 1)) / 6