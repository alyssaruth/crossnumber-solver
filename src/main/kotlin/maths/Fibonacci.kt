package maths

fun isFibonacci(value: Long) = fibonacciUpTo(value).contains(value)

fun fibonacciUpTo(limit: Long, startDigits: List<Long> = listOf(1, 1)) = numbersUpTo(limit, startDigits) { it.takeLast(2).sum() }