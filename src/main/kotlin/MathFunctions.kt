package com.github.alyssaruth

import kotlin.math.sqrt

fun identity(unused: Long): Boolean = true

fun isPrime(value: Long): Boolean {
    if (value == 1L) return false
    if (value == 2L) return true
    if (value % 2 == 0L) return false

    val range = 3..(sqrt(value.toDouble()).toLong() + 1) step 2
    return range.all { value % it != 0L }
}