package solver.digitReducer

fun allDigits(): MultiSquareSelector = { it }

fun firstNDigits(n: Int): MultiSquareSelector = { it.take(n) }

fun lastDigit(): SquareSelector = { it.last() }

fun digitIndices(vararg indices: Int): MultiSquareSelector =
    { it.filterIndexed { index, _ -> indices.contains(index) } }