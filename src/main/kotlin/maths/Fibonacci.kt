package maths

fun isFibonacci(value: Long) = fibonacciUpTo(value).contains(value)

fun fibonacciUpTo(limit: Long) = numbersUpTo(limit, listOf(1, 1)) { it.takeLast(2).sum() }