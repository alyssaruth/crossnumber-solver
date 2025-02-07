package maths

import solver.RAM_THRESHOLD
import java.math.BigInteger
import kotlin.math.ceil
import kotlin.math.floor

fun <E> List<E>.replaceAt(index: Int, newValue: E): List<E> =
    mapIndexed { ix, value -> if (ix == index) newValue else value }

fun degreesToFahrenheit(degrees: Long) = 32 + (degrees * 9) / 5

fun Double.longOrNull() = if (floor(this) == ceil(this)) this.toLong() else null

fun String.sorted() = toCharArray().sorted().joinToString("")

fun String.isPalindrome() = reversed() == this

fun <E> List<Collection<E>>.tryAllCombinations(): List<List<E>>? {
    val size = map { it.size.toBigInteger() }.fold(BigInteger.ONE) { product, n -> product.times(n) }
    if (size > RAM_THRESHOLD.toBigInteger()) {
        return null
    }

    return allCombinations()
}

fun <E> List<Collection<E>>.allCombinations(): List<List<E>> =
    fold(listOf(emptyList())) { listsSoFar, newSolutions ->
        listsSoFar.flatMap { list -> newSolutions.map { x -> list + x } }
    }

fun BigInteger.longOrNull() = try {
    longValueExact()
} catch (e: ArithmeticException) {
    null
}