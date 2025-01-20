package com.github.alyssaruth

typealias Clue = (possibles: List<Int>) -> List<Int>

data class Crossnumber(val gridString: String, val clues: Map<ClueId, List<Clue>>) {
    private val grid = parseGrid(gridString)

    fun validate() {
        grid.validate()
    }
}