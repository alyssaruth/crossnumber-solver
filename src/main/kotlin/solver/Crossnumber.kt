package solver

import logging.green
import logging.possibleDigitsStr
import logging.red
import logging.timeTakenString
import solver.clue.AsyncEqualToClue
import solver.clue.BaseClue
import kotlin.math.roundToLong

typealias Clue = (candidate: Long) -> Boolean

typealias ClueConstructor = (crossnumber: Crossnumber) -> BaseClue

typealias DigitMap = Map<Point, List<Int>>

data class Crossnumber(
    val originalGrid: Grid,
    val digitMap: DigitMap,
    val solutions: Map<ClueId, ISolution>,
    val loopThreshold: Long = LOOP_THRESHOLD
) {
    fun solve(pass: Int = 1, startTime: Long = System.currentTimeMillis()): Crossnumber {
        printLoopBanner(pass)

        val newCrossnumber = iterateClues(startTime)
        if (newCrossnumber.isSolved()) {
            println("------------------------------------------")
            println(newCrossnumber.substituteKnownDigits().prettyString())
            println("------------------------------------------")
            println("Time elapsed: ${(System.currentTimeMillis() - startTime) / 1000}s")
            return newCrossnumber
        }

        return if (newCrossnumber == this) {
            newCrossnumber.handleLackOfProgress(pass, startTime)
        } else {
            newCrossnumber.copy(loopThreshold = LOOP_THRESHOLD).solve(pass + 1, startTime)
        }
    }

    private fun printLoopBanner(pass: Int) {
        val solved = solutions.values.count(ISolution::isSolved)
        val solvedStr = if (solved < 10) " $solved" else solved.toString()
        println("********************")
        println("* PASS $pass ($solvedStr / ${solutions.size}) *")
        println("********************")
    }

    private fun iterateClues(startTime: Long, log: Boolean = true): Crossnumber {
        // Always try smallest stuff first, because narrowing those down may reduce the search space for bigger stuff
        val prioritisedKeys = solutions.entries.sortedBy { it.value.possibilityCount(digitMap) }.map { it.key }
        return prioritisedKeys.fold(this) { crossnumber, clueId ->
            try {
                val solution = crossnumber.solutions.getValue(clueId)
                crossnumber.iterateSolution(clueId, solution, log)
            } catch (e: Exception) {
                if (log) {
                    println("Caught an exception, aborting.".red())
                    crossnumber.dumpFailureInfo(startTime)
                }

                throw e
            }
        }
    }

    private fun handleLackOfProgress(pass: Int, startTime: Long): Crossnumber {
        if (awaitAsyncWork()) {
            return solve(pass + 1, startTime)
        }

        val reducedByGuessing = ruleOutBadGuesses()
        if (reducedByGuessing != null) {
            return reducedByGuessing.solve(pass + 1, startTime)
        }

        val newLoopThreshold = escalateLoopThreshold()
        if (newLoopThreshold != null) {
            return copy(loopThreshold = newLoopThreshold).solve(pass + 1, startTime)
        }

        println("Made no progress this pass, exiting.".red())
        dumpFailureInfo(startTime)
        return this
    }

    private fun ruleOutBadGuesses(): Crossnumber? {
        val cluesToTry = partialSolutions().filterValues { !it.isSolved() && it.possibilities.size < 50 }

        return cluesToTry.firstNotNullOfOrNull { reduceByContradiction(it.key, it.value) }
    }

    private fun reduceByContradiction(clueId: ClueId, solution: PartialSolution): Crossnumber? {
        val startTime = System.currentTimeMillis()
        println("Attempting to narrow down $clueId via contradiction")
        val possibles = solution.possibilities
        val badPossibles = possibles.filter { possible ->
            val newCrossnumber = replaceSolution(clueId, listOf(possible))
            try {
                newCrossnumber.iterateClues(0L, false)
                false
            } catch (ex: Exception) {
                true
            }
        }

        return if (badPossibles.isNotEmpty()) {
            val newCrossnumber = replaceSolution(clueId, possibles - badPossibles)
            logChanges(this, newCrossnumber, System.currentTimeMillis() - startTime)
            newCrossnumber
        } else {
            null
        }
    }

    private fun escalateLoopThreshold(): Long? {
        val smallestPending = pendingSolutions()
            .filter { it.value.possibilities in (loopThreshold + 1)..MAX_LOOP_THRESHOLD }
            .minByOrNull { it.value.possibilities } ?: return null

        val newThreshold = smallestPending.value.possibilities
        println("Made no progress this pass: kicking up threshold to $newThreshold to crack ${smallestPending.key}")
        return newThreshold
    }

    private fun awaitAsyncWork(): Boolean {
        val pendingAsyncs = solutions.filterValues {
            val clue = it.clue(this)
            clue is AsyncEqualToClue && clue.isPending()
        }

        if (pendingAsyncs.isNotEmpty()) {
            println("Made no progress this pass: some async calculations are still outstanding")
            pendingAsyncs.forEach { (clueId, solution) ->
                val asyncWaitStart = System.currentTimeMillis()
                print("Awaiting $clueId...")
                (solution.clue(this) as AsyncEqualToClue).await()
                println(" done!" + timeTakenString(System.currentTimeMillis() - asyncWaitStart))
            }

            return true
        }

        return false
    }

    private fun dumpFailureInfo(startTime: Long) {
        println("------------------------------------------")
        println(completionString())
        println("------------------------------------------")
        solutions.filterValues { !it.isSolved() }.forEach { (id, soln) ->
            val options =
                if (soln is PartialSolution && soln.possibilities.size < 100) " - ${soln.possibilities}" else ""
            println("$id: ${soln.status()}$options")
        }
        println("------------------------------------------")
        println(substituteKnownDigits().prettyString())
        println("------------------------------------------")
        println("Time elapsed: ${(System.currentTimeMillis() - startTime) / 1000}s")
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

    private fun pendingSolutions(): Map<ClueId, PendingSolution> =
        solutions.filterValues { it is PendingSolution }.mapValues { it.value as PendingSolution }

    private fun partialSolutions(): Map<ClueId, PartialSolution> =
        solutions.filterValues { it is PartialSolution }.mapValues { it.value as PartialSolution }

    private fun isSolved() = solutions.values.all(ISolution::isSolved)

    private fun iterateSolution(id: ClueId, solution: ISolution, log: Boolean): Crossnumber {
        try {
            val startTime = System.currentTimeMillis()
            val newCrossnumber = solution.iterate(id, this)
            if (log) logChanges(this, newCrossnumber, System.currentTimeMillis() - startTime)
            return newCrossnumber
        } catch (ex: Exception) {
            throw Exception("Caught error iterating $id: ${ex.message}", ex)
        }
    }

    private fun logChanges(oldCrossnumber: Crossnumber, newCrossnumber: Crossnumber, timeTaken: Long) {
        oldCrossnumber.solutions.forEach { (clueId, oldSolution) ->
            val newSolution = newCrossnumber.solutions.getValue(clueId)
            if (oldSolution != newSolution) {
                val statusString = "$clueId: ${oldSolution.status()} -> ${newSolution.status()}"
                println(statusString + timeTakenString(timeTaken))
            }
        }
    }

    private fun substituteKnownDigits(): Grid {
        return digitMap.entries.fold(originalGrid) { grid, (pt, digits) ->
            if (digits.size > 1) {
                grid.updateValue(pt, possibleDigitsStr(digits.size))
            } else {
                grid.updateValue(pt, digits.first().toString().green())
            }
        }
    }

    fun replaceSolution(clueId: ClueId, possibilities: List<Long>): Crossnumber {
        val existing = solutions.getValue(clueId)
        return replaceSolution(clueId, PartialSolution(existing.squares, existing.clue, possibilities))
    }

    fun replaceSolution(clueId: ClueId, solution: ISolution): Crossnumber =
        copy(solutions = solutions + (clueId to solution))

    fun solutionsOfLength(length: Int) = solutions.filterValues { it.squares.size == length }

    fun sumAcrossClues(): Long? {
        val acrossSolutions = solutions.filterKeys { it.orientation == Orientation.ACROSS }.values
        if (!acrossSolutions.all(ISolution::isSolved)) {
            return null
        }

        return acrossSolutions.sumOf { (it as PartialSolution).possibilities.first() }
    }
}