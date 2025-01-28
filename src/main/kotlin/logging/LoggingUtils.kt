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