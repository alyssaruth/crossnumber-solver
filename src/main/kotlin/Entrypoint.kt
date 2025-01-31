import logging.red
import puzzles.CROSSNUMBER_1
import puzzles.CROSSNUMBER_2
import puzzles.CROSSNUMBER_3
import puzzles.CROSSNUMBER_4
import kotlin.system.exitProcess

private val puzzles = mapOf(
    "1" to CROSSNUMBER_1,
    "2" to CROSSNUMBER_2,
    "3" to CROSSNUMBER_3,
    "4" to CROSSNUMBER_4,
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

    puzzle.solve()
    exitProcess(0)
}