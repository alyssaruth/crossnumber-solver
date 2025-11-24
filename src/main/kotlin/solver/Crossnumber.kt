package solver

import logging.dumpFailureInfo
import logging.orange
import logging.possibleDigitsStr
import logging.printLoopBanner
import logging.red
import logging.timeTakenString
import solver.clue.AsyncEqualToClue
import solver.clue.BaseClue
import solver.digitReducer.DigitReducerConstructor

typealias Clue = (candidate: Long) -> Boolean

typealias ClueConstructor = (crossnumber: Crossnumber) -> BaseClue

typealias GlobalClue = (crossnumber: Crossnumber) -> Boolean

typealias DigitMap = Map<Point, List<Int>>

data class Crossnumber(
    val originalGrid: Grid,
    val digitMap: DigitMap,
    val solutions: Map<ClueId, ISolution>,
    val digitReducers: List<DigitReducerConstructor>,
    val globalClues: List<GlobalClue>,
    val loopThreshold: Long = LOOP_THRESHOLD,
    val exploringGuess: Boolean = false,
    val creationTime: Long = System.currentTimeMillis(),
    val guessThreshold: Int,
) {
    fun solve(): Crossnumber = solve(1)

    fun solve(pass: Int = 1, log: Boolean = true): Crossnumber {
        if (log) printLoopBanner(pass)

        val reduced = applyDigitReducers(log)

        val newCrossnumber = reduced.iterateAllClues(log)
        if (newCrossnumber.isSolved()) {
            if (log) {
                println("------------------------------------------")
                println(newCrossnumber.substituteKnownDigits().prettyString())
                println("------------------------------------------")
                println("Time elapsed: ${(System.currentTimeMillis() - creationTime) / 1000}s")
            }
            return newCrossnumber
        }

        return if (newCrossnumber == this) {
            newCrossnumber.handleLackOfProgress(pass, log)
        } else {
            newCrossnumber.copy(loopThreshold = LOOP_THRESHOLD).solve(pass + 1, log)
        }
    }

    private fun applyDigitReducers(log: Boolean): Crossnumber {
        val newDigitMap = digitReducers.fold(digitMap) { currentMap, mkReducer ->
            val reducer = mkReducer(this)
            val newMap = reducer.apply(currentMap)

            val oldSize = currentMap.values.sumOf { it.size }
            val newSize = newMap.values.sumOf { it.size }
            if (newSize < oldSize) {
                if (log) println("${reducer.clueId}: Reduced digits by ${oldSize - newSize}")
            }

            newMap
        }

        return copy(digitMap = newDigitMap)
    }

    private fun iterateAllClues(
        log: Boolean = true,
    ): Crossnumber {
        val prioritised = prioritiseSolutions(solutions.entries.toList())
        val updatedCrossnumber = iterateClues(log, this, prioritised)

        if (!globalClues.all { it(updatedCrossnumber) }) {
            throw Exception("Global rule violated!")
        }

        return updatedCrossnumber
    }

    private tailrec fun iterateClues(
        log: Boolean = true,
        currentCrossnumber: Crossnumber = this,
        remainingClues: List<Map.Entry<ClueId, ISolution>>,
    ): Crossnumber {
        if (remainingClues.isEmpty()) {
            return currentCrossnumber
        } else {
            val nextSolution = remainingClues.first()
            val newCrossnumber = currentCrossnumber.iterateSolution(nextSolution.key, log)
            val newClues = newCrossnumber.prioritiseSolutions(remainingClues.drop(1))
            return iterateClues(log, newCrossnumber, newClues)
        }
    }

    private fun prioritiseSolutions(solutions: List<Map.Entry<ClueId, ISolution>>) = solutions.sortedBy {
        it.value.possibilityCount(digitMap)
    }

    private fun handleLackOfProgress(pass: Int, log: Boolean): Crossnumber {
        if (exploringGuess) {
            return this
        }

        if (awaitAsyncWork()) {
            return solve(pass + 1)
        }

        val reducedByGuessing = ruleOutBadGuesses(log)
        if (reducedByGuessing != null) {
            return reducedByGuessing.solve(pass + 1, log)
        }

        val newLoopThreshold = escalateLoopThreshold()
        if (newLoopThreshold != null) {
            return copy(loopThreshold = newLoopThreshold).solve(pass + 1)
        }

        if (log) println("Made no progress this pass, exiting.".red())
        if (log) dumpFailureInfo()
        return this
    }

    private fun ruleOutBadGuesses(log: Boolean): Crossnumber? {
        val cluesToTry = partialSolutions().filterValues { !it.isSolved() && it.possibilities.size < guessThreshold }
        val sortedPairs = cluesToTry.entries.sortedBy { it.value.possibilities.size }

        val startTime = System.currentTimeMillis()
        return sortedPairs.firstNotNullOfOrNull { reduceByContradiction(it.key, it.value, startTime, log) }
    }

    private fun reduceByContradiction(
        clueId: ClueId,
        solution: PartialSolution,
        startTime: Long,
        log: Boolean
    ): Crossnumber? {
        val possibles = solution.possibilities
        val badPossibles = possibles.filter { possible ->
            val newCrossnumber = copy(exploringGuess = true).replaceSolution(clueId, listOf(possible))
            try {
                newCrossnumber.solve(1, false)
                false
            } catch (ex: Exception) {
                true
            }
        }

        return if (badPossibles.isNotEmpty()) {
            val newCrossnumber =
                replaceSolution(clueId, possibles - badPossibles)
            if (log) logChanges(clueId, this, newCrossnumber, startTime, " (by contradiction)".orange())
            newCrossnumber.iterateSolution(clueId, false)
        } else {
            null
        }
    }

    private fun escalateLoopThreshold(): Long? {
        val smallestPending = pendingSolutions()
            .filter {
                it.value.possibilities in (loopThreshold + 1)..MAX_LOOP_THRESHOLD
                        && it.value.possibilities < it.value.longestAttempted
            }
            .minByOrNull { it.value.possibilities } ?: return null

        val newThreshold = smallestPending.value.possibilities
        val digitStr = smallestPending.value.squares.joinToString("") { possibleDigitsStr(digitMap.getValue(it)) }
        println("Made no progress this pass - kicking up threshold to $newThreshold to crack ${smallestPending.key}: $digitStr")
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

    private fun pendingSolutions(): Map<ClueId, PendingSolution> =
        solutions.filterValues { it is PendingSolution }.mapValues { it.value as PendingSolution }

    fun partialSolutions(): Map<ClueId, PartialSolution> =
        solutions.filterValues { it is PartialSolution }.mapValues { it.value as PartialSolution }

    fun isSolved() = solutions.values.all(ISolution::isSolved)

    private fun iterateSolution(id: ClueId, log: Boolean): Crossnumber {
        try {
            val startTime = System.currentTimeMillis()
            val newCrossnumber = solutions.getValue(id).iterate(id, this)
            if (log) logChanges(id, this, newCrossnumber, startTime)
            return newCrossnumber
        } catch (ex: Exception) {
            if (log) {
                println("Caught an exception, aborting.".red())
                dumpFailureInfo()
            }

            throw Exception("Caught error iterating $id: ${ex.message}", ex)
        }
    }

    private fun logChanges(
        id: ClueId,
        oldCrossnumber: Crossnumber,
        newCrossnumber: Crossnumber,
        startTime: Long,
        suffix: String = ""
    ) {
        val timeTaken = System.currentTimeMillis() - startTime
        oldCrossnumber.solutions.forEach { (clueId, oldSolution) ->
            val newSolution = newCrossnumber.solutions.getValue(clueId)
            if (oldSolution != newSolution) {
                val statusString = "$clueId: ${oldSolution.status()} -> ${newSolution.status()}$suffix"
                println(statusString + timeTakenString(timeTaken))
            }
        }

        if (oldCrossnumber.solutions == newCrossnumber.solutions && timeTaken > 1000) {
            val solution = newCrossnumber.solutions.getValue(id)
            println("$id: ${solution.status()} -> ${"unchanged".orange()}${timeTakenString(timeTaken)}")
        }
    }

    fun substituteKnownDigits(): Grid {
        return digitMap.entries.fold(originalGrid) { grid, (pt, digits) ->
            grid.updateValue(pt, possibleDigitsStr(digits))
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

    fun digitsFromRow(rowNumber: Int): List<Int>? {
        val squares = digitMap.filter { it.key.y == rowNumber }.values
        if (squares.any { it.size > 1 }) {
            return null
        }

        return squares.map { it.first() }
    }
}