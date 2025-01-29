package logging

fun String.green() = coloured(32)
fun String.orange() = coloured(33)
fun String.red() = coloured(31)

private fun String.coloured(ansi: Int) = "\u001B[${ansi}m$this\u001B[0m"

fun timeTakenString(timeTaken: Long): String {
    if (timeTaken < 1000) {
        return ""
    }

    val timeInSeconds = timeTaken / 1000
    val timeStr = " [${timeInSeconds}s]"
    return if (timeTaken < 5000) timeStr.orange() else timeStr.red()
}

fun possibleDigitsStr(possibilities: Int) = when (possibilities) {
    2 -> "²"
    3 -> "³"
    4 -> "⁴"
    5 -> "⁵"
    6 -> "⁶"
    7 -> "⁷"
    8 -> "⁸"
    9 -> "⁹"
    else -> "⁺"
}.red()