package solver

import maths.getViableDigits
import solver.clue.emptyClue
import solver.clue.plus
import solver.digitReducer.DigitReducerConstructor

fun clueMap(vararg clues: Pair<String, ClueConstructor>): Map<String, ClueConstructor> =
    clues.fold(emptyMap()) { mapSoFar, (clueId, clue) ->
        val existing = mapSoFar[clueId]
        if (existing == null) {
            mapSoFar + (clueId to clue)
        } else {
            mapSoFar + (clueId to (existing + clue))
        }
    }

fun factoryCrossnumber(
    gridString: String,
    rawClues: Map<String, ClueConstructor>,
    digitReducers: List<DigitReducerConstructor> = emptyList(),
    globalClues: List<GlobalClue> = emptyList(),
    skipSymmetryCheck: Boolean = false,
    guessThreshold: Int = 25,
    baseLoopThreshold: Long = RAM_THRESHOLD,
): Crossnumber {
    val clues = rawClues.mapKeys { (clueStr, _) -> ClueId.fromString(clueStr) }
    val grid = parseGrid(gridString)
    grid.validate(skipSymmetryCheck)

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

    return Crossnumber(grid, digitMap, pendingSolutions, digitReducers, globalClues, guessThreshold = guessThreshold, baseLoopThreshold = baseLoopThreshold)
}

private fun initialiseDigitMap(solutions: List<Word>): DigitMap {
    val allPoints = solutions.flatMap { it.squares }.toSet()
    val leadingSpaces = solutions.map { it.squares.first() }.toSet()
    val nonLeadingSpaces = allPoints - leadingSpaces

    return leadingSpaces.associateWith { getViableDigits(true) } +
            nonLeadingSpaces.associateWith { getViableDigits(false) }
}