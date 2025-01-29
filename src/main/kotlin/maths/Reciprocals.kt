package maths

fun reciprocalSum(values: List<Int>): Double {
    val lcm = lcm(values)
    val numerators = values.map { lcm / it }
    return numerators.sum().toDouble() / lcm
}