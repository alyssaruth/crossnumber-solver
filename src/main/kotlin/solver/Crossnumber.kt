package solver

import kotlin.math.roundToLong

typealias Clue = (candidate: Long) -> Boolean

typealias ClueConstructor = (crossnumber: Crossnumber) -> BaseClue

fun factoryCrossnumber(gridString: String, rawClues: Map<String, ClueConstructor>): Crossnumber {
    val clues = rawClues.mapKeys { (clueStr, _) -> ClueId.fromString(clueStr) }
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
        val myClues = clues.getOrDefault(word.clueId, emptyClue())
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
    val solutions: Map<ClueId, ISolution>,
    val loopThreshold: Long = LOOP_THRESHOLD
) {
    fun solve(pass: Int = 1, startTime: Long = System.currentTimeMillis()): Crossnumber {
        val solved = solutions.values.count(ISolution::isSolved)
        val solvedStr = if (solved < 10) " $solved" else solved.toString()
        println("********************")
        println("* PASS $pass ($solvedStr / ${solutions.size}) *")
        println("********************")

        // Always try smallest stuff first, because narrowing those down may reduce the search space for bigger stuff
        val prioritisedKeys = solutions.entries.sortedBy { it.value.possibilityCount(digitMap) }.map { it.key }
        val newCrossnumber = prioritisedKeys.fold(this) { crossnumber, clueId ->
            val solution = crossnumber.solutions.getValue(clueId)
            crossnumber.iterateSolution(clueId, solution)
        }

        if (newCrossnumber.isSolved()) {
            println("------------------------------------------")
            println(newCrossnumber.substituteKnownDigits().prettyString())
            println("------------------------------------------")
            println("Time elapsed: ${(System.currentTimeMillis() - startTime) / 1000}s")
            return newCrossnumber
        }

        if (newCrossnumber == this) {
            if (newCrossnumber.loopThreshold == LOOP_THRESHOLD) {
                println("Made no progress on latest pass, kicking up loop threshold.")
                return newCrossnumber.copy(loopThreshold = EXTREME_LOOP_THRESHOLD).solve(pass + 1, startTime)
            }

            println("Made no progress on latest pass, exiting.")
            println("------------------------------------------")
            println(newCrossnumber.completionString())
            println("------------------------------------------")
            newCrossnumber.solutions.filterValues { !it.isSolved() }.forEach { id, soln ->
                val options =
                    if (soln is PartialSolution && soln.possibilities.size < 100) " - ${soln.possibilities}" else ""
                println("$id: ${soln.status()}$options")
            }
            println("------------------------------------------")
            println(newCrossnumber.substituteKnownDigits().prettyString())
            println("------------------------------------------")
            println("Time elapsed: ${(System.currentTimeMillis() - startTime) / 1000}s")
            return newCrossnumber
        }

        return newCrossnumber.solve(pass + 1, startTime)
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

    private fun iterateSolution(id: ClueId, solution: ISolution): Crossnumber {
        try {
            val newCrossnumber = solution.iterate(id, this)
            logChanges(this, newCrossnumber)
            return newCrossnumber
        } catch (ex: Exception) {
            throw Exception("Caught error iterating $id: ${ex.message}", ex)
        }
    }

    private fun logChanges(oldCrossnumber: Crossnumber, newCrossnumber: Crossnumber) {
        oldCrossnumber.solutions.forEach { clueId, oldSolution ->
            val newSolution = newCrossnumber.solutions.getValue(clueId)
            if (oldSolution != newSolution) {
                println("$clueId: ${oldSolution.status()} -> ${newSolution.status()}")
            }
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

    fun replaceSolution(clueId: ClueId, solution: ISolution): Crossnumber =
        copy(solutions = solutions + (clueId to solution))
}