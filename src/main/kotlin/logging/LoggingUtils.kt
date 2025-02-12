package logging

import solver.Crossnumber
import solver.ISolution
import solver.PartialSolution
import solver.PendingSolution
import kotlin.math.roundToLong

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

fun possibleDigitsStr(digits: List<Int>) =
    if (digits.size == 1) digits.first().toString().green() else unknownDigitsStr(digits.size)

private fun unknownDigitsStr(possibilities: Int) = when (possibilities) {
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

fun Crossnumber.printLoopBanner(pass: Int) {
    val solved = solutions.values.count(ISolution::isSolved)
    val solvedStr = if (solved < 10) " $solved" else solved.toString()
    val extraStar = if (solved < 10) "" else "*"
    println("********************$extraStar")
    println("* PASS $pass ($solvedStr / ${solutions.size}) *")
    println("********************$extraStar")
}

fun Crossnumber.dumpFailureInfo() {
    println("------------------------------------------")
    println(completionString())
    println("------------------------------------------")
    solutions.filterValues { !it.isSolved() }.toList().sortedBy { it.first }.forEach { (id, soln) ->
        val options =
            if (soln is PartialSolution && soln.possibilities.size < 100) " - ${soln.possibilities}" else ""
        println("$id: ${soln.status()}$options")
    }
    println("------------------------------------------")
    println(substituteKnownDigits().prettyString())
    println("------------------------------------------")
    println("Time elapsed: ${(System.currentTimeMillis() - creationTime) / 1000}s")
}

fun Crossnumber.completionString(): String {
    val solved = solutions.values.filter(ISolution::isSolved).size
    val partial = solutions.values.filter { it is PartialSolution && !it.isSolved() }.size
    val pending = solutions.values.filterIsInstance<PendingSolution>().size
    return """
            Solved: ${progressLine(solved)}
            Partial: ${progressLine(partial)}
            Pending: ${progressLine(pending)}
        """.trimIndent()
}

private fun Crossnumber.progressLine(solutionCount: Int): String {
    val percent = (1000 * solutionCount.toDouble() / solutions.size.toDouble()).roundToLong().toDouble() / 10
    return "$solutionCount / ${solutions.size} ($percent%)"
}