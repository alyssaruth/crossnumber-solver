package solver

import kotlin.math.roundToLong

typealias Clue = (candidate: Long) -> Boolean

typealias ClueConstructor = (crossnumber: Crossnumber) -> BaseClue

fun factoryCrossnumber(gridString: String, clues: Map<ClueId, List<ClueConstructor>>): Crossnumber {
    val grid = parseGrid(gridString)
    grid.validate()

    val detectedWords = grid.detectWords()
    val validIds = detectedWords.map { it.clueId }

    val invalidIds = clues.keys - validIds
    if (invalidIds.isNotEmpty()) {
        throw IllegalArgumentException("Invalid clue ID(s): $invalidIds")
    }

    val digitMap = initialiseDigitMap(detectedWords)
    val pendingSolutions = detectedWords.associate { word ->
        val myClues = clues.getOrDefault(word.clueId, emptyList())
        word.clueId to PendingSolution(word.squares, myClues, digitMap)
    }

    return Crossnumber(grid, digitMap, pendingSolutions)
}

private fun initialiseDigitMap(solutions: List<Word>): Map<Point, List<Int>> {
    val allPoints = solutions.flatMap { it.squares }.toSet()
    val leadingSpaces = solutions.map { it.squares.first() }.toSet()
    val nonLeadingSpaces = allPoints - leadingSpaces

    return leadingSpaces.associateWith { (1..9).toList() } + nonLeadingSpaces.associateWith { (0..9).toList() }
}

data class Crossnumber(
    val originalGrid: Grid,
    val digitMap: Map<Point, List<Int>>,
    val solutions: Map<ClueId, ISolution>
) {
    fun solve(pass: Int = 1): Crossnumber {
        println("************")
        println("*  PASS $pass  *")
        println("************")

        val newCrossnumber = solutions.entries.fold(this) { crossnumber, solution ->
            crossnumber.iterateSolution(solution)
        }

        if (newCrossnumber.isSolved()) {
            return newCrossnumber
        }

        if (newCrossnumber == this) {
            println("Made no progress on latest pass, exiting.")
            println("------------------------------------------")
            println(newCrossnumber.completionString())
            println("------------------------------------------")
            println(newCrossnumber.substituteKnownDigits().prettyString())
            return newCrossnumber
        }

        return newCrossnumber.solve(pass + 1)
    }

    private fun completionString(): String {
        val solved = solutions.values.filter(ISolution::isSolved).size
        val partial = solutions.values.filter { it is PartialSolution && !it.isSolved() }.size
        val pending = solutions.values.filterIsInstance<PendingSolution>().size
        return """
            Solved: ${progressLine(solved)}
            Partial: ${progressLine(partial)}
            Pending: ${progressLine(pending)}
        """.trimIndent()
    }

    private fun progressLine(solutionCount: Int): String {
        val percent = (1000 * solutionCount.toDouble() / solutions.size.toDouble()).roundToLong().toDouble() / 10
        return "$solutionCount / ${solutions.size} ($percent%)"
    }


    private fun isSolved() = solutions.values.all(ISolution::isSolved)

    private fun iterateSolution(solution: Map.Entry<ClueId, ISolution>): Crossnumber {
        if (solution.value.isSolved()) {
            return this
        }

        try {
            val (newSolution, newDigitMap) = solution.value.iterate(this)
            if (solution.value != newSolution) {
                println("${solution.key}: ${solution.value.status()} -> ${newSolution.status()}")
            }
            return Crossnumber(originalGrid, newDigitMap, solutions + (solution.key to newSolution))
        } catch (ex: Exception) {
            throw Exception("Caught error iterating ${solution.key}: ${ex.message}", ex)
        }
    }

    private fun substituteKnownDigits(): Grid {
        return digitMap.entries.fold(originalGrid) { grid, (pt, digits) ->
            if (digits.size > 1) {
                grid
            } else {
                grid.updateValue(pt, digits.first().toString())
            }
        }
    }
}