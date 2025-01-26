package solver

sealed interface ISolution {
    val squares: List<Point>
    val clue: ClueConstructor

    fun possibilityCount(digitMap: Map<Point, List<Int>>): Long

    fun iterate(clueId: ClueId, crossnumber: Crossnumber): Crossnumber

    fun status(): String

    fun isSolved(): Boolean
}
