import logging.red
import puzzles.CROSSNUMBER_1
import puzzles.CROSSNUMBER_10
import puzzles.CROSSNUMBER_11
import puzzles.CROSSNUMBER_12
import puzzles.CROSSNUMBER_13
import puzzles.CROSSNUMBER_17
import puzzles.CROSSNUMBER_18
import puzzles.CROSSNUMBER_2
import puzzles.CROSSNUMBER_3
import puzzles.CROSSNUMBER_4
import puzzles.CROSSNUMBER_5
import puzzles.CROSSNUMBER_6
import puzzles.CROSSNUMBER_7
import puzzles.CROSSNUMBER_8
import puzzles.CROSSNUMBER_9
import puzzles.solveCrossnumber14
import solver.Crossnumber
import kotlin.system.exitProcess

private val puzzles = mapOf<String, () -> Crossnumber>(
    "1" to CROSSNUMBER_1::solve,
    "2" to CROSSNUMBER_2::solve,
    "3" to CROSSNUMBER_3::solve,
    "4" to CROSSNUMBER_4::solve,
    "5" to CROSSNUMBER_5::solve,
    "6" to CROSSNUMBER_6::solve,
    "7" to CROSSNUMBER_7::solve,
    "8" to CROSSNUMBER_8::solve,
    "9" to CROSSNUMBER_9::solve,
    "10" to CROSSNUMBER_10::solve,
    "11" to CROSSNUMBER_11::solve,
    "12" to CROSSNUMBER_12::solve,
    "13" to CROSSNUMBER_13::solve,
    "14" to ::solveCrossnumber14,
    "17" to CROSSNUMBER_17::solve,
    "18" to CROSSNUMBER_18::solve,
)

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("No puzzle number supplied. Usage: ./gradlew solve -Puzzle=1".red())
        exitProcess(1)
    }

    val puzzleNumber = args.first()
    val puzzle = puzzles[puzzleNumber]
    if (puzzle == null) {
        println("No puzzle found for input $puzzleNumber. Valid inputs: ${puzzles.keys}")
        exitProcess(1)
    }

    puzzle()
    exitProcess(0)
}