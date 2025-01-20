package com.github.alyssaruth

typealias Clue = (candidate: Long) -> Boolean

fun factoryCrossnumber(gridString: String, clues: Map<ClueId, List<Clue>>): Crossnumber {
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
        word.clueId to PendingSolution(word.squares, myClues)
    }

    return Crossnumber(digitMap, pendingSolutions)
}

private fun initialiseDigitMap(solutions: List<Word>): Map<Point, List<Int>> {
    val allPoints = solutions.flatMap { it.squares }.toSet()
    val leadingSpaces = solutions.map { it.squares.first() }.toSet()
    val nonLeadingSpaces = allPoints - leadingSpaces

    return leadingSpaces.associateWith { (1..9).toList() } + nonLeadingSpaces.associateWith { (0..9).toList() }
}

data class Crossnumber(val digitMap: Map<Point, List<Int>>, val solutions: Map<ClueId, ISolution>)