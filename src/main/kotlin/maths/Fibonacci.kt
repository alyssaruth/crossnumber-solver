package maths

fun isFibonacci(value: Long) = fibonacciUpTo(value).contains(value)

tailrec fun fibonacciUpTo(limit: Long, soFar: List<Long> = listOf(1, 1)): List<Long> {
    val next = soFar.takeLast(2).sum()
    return if (next > limit) {
        soFar
    } else {
        fibonacciUpTo(limit, soFar + next)
    }
}