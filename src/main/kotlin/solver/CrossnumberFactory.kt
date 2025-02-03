package solver

import maths.getViableDigits
import solver.clue.emptyClue

fun factoryCrossnumber(gridString: String, rawClues: Map<String, ClueConstructor>, skipSymmetryCheck: Boolean = false): Crossnumber {
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

    return Crossnumber(grid, digitMap, pendingSolutions)
}

private fun initialiseDigitMap(solutions: List<Word>): DigitMap {
    val allPoints = solutions.flatMap { it.squares }.toSet()
    val leadingSpaces = solutions.map { it.squares.first() }.toSet()
    val nonLeadingSpaces = allPoints - leadingSpaces

    return leadingSpaces.associateWith { getViableDigits(true) } +
            nonLeadingSpaces.associateWith { getViableDigits(false) }
}