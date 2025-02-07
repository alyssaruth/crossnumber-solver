package solver

sealed interface ISolution {
    val squares: List<Point>
    val clue: ClueConstructor

    fun possibilityCount(digitMap: DigitMap): Long

    fun iterate(clueId: ClueId, crossnumber: Crossnumber, log: Boolean): Crossnumber

    fun status(): String

    fun isSolved(): Boolean
}
