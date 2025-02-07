package maths

import java.math.BigInteger
import kotlin.math.ceil
import kotlin.math.floor

fun <E> List<E>.replaceAt(index: Int, newValue: E): List<E> =
    mapIndexed { ix, value -> if (ix == index) newValue else value }

fun degreesToFahrenheit(degrees: Long) = 32 + (degrees * 9) / 5

fun Double.longOrNull() = if (floor(this) == ceil(this)) this.toLong() else null

fun String.sorted() = toCharArray().sorted().joinToString("")

fun String.isPalindrome() = reversed() == this

fun <E> List<Collection<E>>.allCombinations(): List<List<E>> =
    fold(listOf(emptyList())) { listsSoFar, newSolutions ->
        listsSoFar.flatMap { list -> newSolutions.map { x -> list + x } }
    }

fun BigInteger.longOrNull() = try {
    longValueExact()
} catch (e: ArithmeticException) {
    null
}