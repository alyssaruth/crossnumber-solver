package solver

sealed interface ISolution {
    val squares: List<Point>
    val clue: ClueConstructor

    fun iterate(clueId: ClueId, crossnumber: Crossnumber): Crossnumber

    fun status(): String

    fun isSolved(): Boolean
}
