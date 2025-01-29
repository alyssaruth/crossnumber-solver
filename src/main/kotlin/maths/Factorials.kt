package maths

fun factorialMod(n: Long, base: Long, currentMultiplier: Long = n - 1): Long {
    if (n == 0L) return 0L
    if (currentMultiplier == 1L) return n

    return factorialMod((n * currentMultiplier) % base, base, currentMultiplier - 1)
}