package maths

import solver.Clue


typealias Generator = (Long) -> List<Long>

fun isProductOfConsecutive(chainSize: Int, digits: Int, generator: Generator): Clue {
    val max = 10.pow(digits)
    val candidates = generator(max).windowed(chainSize).map { it.fold(1L, Long::times) }

    return { candidates.contains(it) }
}

fun isSumOfConsecutive(chainSize: Int, digits: Int, generator: Generator): Clue {
    val max = 10.pow(digits)
    val candidates = generator(max).windowed(chainSize).map { it.sum() }

    return { candidates.contains(it) }
}

fun squaresUpTo(limit: Long) = numbersUpTo(limit, listOf(1L)) {
    val n = it.size.toLong() + 1
    n * n
}

fun cubesUpTo(limit: Long) = numbersUpTo(limit, listOf(1L)) {
    val n = it.size.toLong() + 1
    n * n * n
}

tailrec fun numbersUpTo(
    limit: Long,
    numbersSoFar: List<Long>,
    nextNumber: (List<Long>) -> Long
): List<Long> {
    val next = nextNumber(numbersSoFar)
    return if (next > limit) {
        numbersSoFar
    } else {
        numbersUpTo(limit, numbersSoFar + next, nextNumber)
    }
}