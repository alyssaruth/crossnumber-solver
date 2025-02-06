package solver.digitReducer

fun allDigits(): MultiSquareSelector = { it }

fun firstNDigits(n: Int): MultiSquareSelector = { it.take(n) }

fun lastDigit(): SquareSelector = { it.last() }