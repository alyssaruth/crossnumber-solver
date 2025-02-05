package solver

import maths.getViableDigits
import solver.clue.emptyClue
import solver.clue.plus
import solver.digitReducer.AbstractDigitReducer
import solver.digitReducer.DigitReducer
import solver.digitReducer.plus

typealias SquareSelector = (List<Point>) -> List<Point>

data class RawReducer(val selector: SquareSelector, val filterFn: (Int) -> Boolean)

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
    rawReducers: List<Pair<String, RawReducer>> = emptyList(),
    skipSymmetryCheck: Boolean = false
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

    val digitReducers = collectReducers(rawReducers, detectedWords)
    return Crossnumber(grid, digitMap, pendingSolutions, digitReducers)
}

private fun collectReducers(
    rawReducers: List<Pair<String, RawReducer>>,
    detectedWords: List<Word>
): Map<ClueId, AbstractDigitReducer> =
    rawReducers.fold(emptyMap()) { mapSoFar, (clueStr, rawReducer) ->
        val clueId = ClueId.fromString(clueStr)
        val word = detectedWords.firstOrNull { it.clueId == clueId }
            ?: throw IllegalArgumentException("Invalid clue ID for reducer: $clueId")
        val squares = rawReducer.selector(word.squares).toSet()
        val newReducer = DigitReducer(squares, rawReducer.filterFn)

        val existing = mapSoFar[clueId]
        if (existing == null) {
            mapSoFar + (clueId to newReducer)
        } else {
            mapSoFar + (clueId to (existing + newReducer))
        }
    }

private fun initialiseDigitMap(solutions: List<Word>): DigitMap {
    val allPoints = solutions.flatMap { it.squares }.toSet()
    val leadingSpaces = solutions.map { it.squares.first() }.toSet()
    val nonLeadingSpaces = allPoints - leadingSpaces

    return leadingSpaces.associateWith { getViableDigits(true) } +
            nonLeadingSpaces.associateWith { getViableDigits(false) }
}