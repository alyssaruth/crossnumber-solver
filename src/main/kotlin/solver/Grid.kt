package solver

data class Point(val x: Int, val y: Int)

operator fun Point.plus(other: Point) = Point(x + other.x, y + other.y)
operator fun Point.minus(other: Point) = Point(x - other.x, y - other.y)

const val BLACK = "#"
const val WHITE = "."

fun parseGrid(gridStr: String): Grid {
    val pairs = gridStr.lines().flatMapIndexed { y, line ->
        line.toCharArray().mapIndexed { x, value ->
            Point(x, y) to value.toString()
        }
    }

    return Grid(mapOf(*pairs.toTypedArray()))
}

class Grid(private val pointMap: Map<Point, String>) {
    private val xMax = pointMap.keys.maxOf { it.x }
    private val yMax = pointMap.keys.maxOf { it.y }

    fun rotate() = pointMap.mapKeys { (pt, _) -> Point(xMax - pt.y, pt.x) }.let(::Grid)

    fun prettyString() = (0..yMax).joinToString("\n") { y ->
        (0..xMax).joinToString("") { x -> pointMap.getValue(Point(x, y)).toString() }
    }

    fun validate(skipSymmetry: Boolean = false) {
        if (xMax != yMax) {
            throw IllegalArgumentException("Grid is not square, detected dimensions: ${xMax + 1}x${yMax + 1}")
        }

        val invalids = pointMap.values.filterNot { it == BLACK || it == WHITE }
        if (invalids.isNotEmpty()) {
            throw IllegalArgumentException("Invalid character(s) detected: $invalids")
        }

        if (!skipSymmetry && rotate().rotate().pointMap != pointMap) {
            throw IllegalArgumentException("Grid is not rotationally symmetric!")
        }
    }

    fun detectWords(): List<Word> {
        val pts = pointMap.keys.sortedWith(compareBy<Point> { it.y }.thenBy { it.x })

        return pts.fold(emptyList()) { cluesSoFar, pt ->
            val nextClueNumber = 1 + (cluesSoFar.maxOfOrNull { it.clueId.number } ?: 0)

            val across = getWord(nextClueNumber, pt, Orientation.ACROSS)
            val down = getWord(nextClueNumber, pt, Orientation.DOWN)

            cluesSoFar + listOfNotNull(across, down)
        }
    }

    private fun getWord(number: Int, startPt: Point, orientation: Orientation): Word? {
        if (lookupPoint(startPt) == BLACK) {
            return null
        }

        // Edge case for a single isolated white square (e.g. Crossnumber 22)
        val neighbours = Orientation.entries.flatMap { listOf(startPt + it.unitVector(), startPt - it.unitVector()) }
        if (neighbours.all { lookupPoint(it) == BLACK}) {
            return Word(ClueId(number, orientation), listOf(startPt))
        }

        if (lookupPoint(startPt - orientation.unitVector()) == WHITE) {
            // White square to our left/above
            return null
        }

        if (lookupPoint(startPt + orientation.unitVector()) == BLACK) {
            // Black square or edge to our right/below
            return null
        }

        val allPts = mutableListOf(startPt)
        var nextPt = startPt + orientation.unitVector()
        while (lookupPoint(nextPt) == WHITE) {
            allPts.add(nextPt)
            nextPt += orientation.unitVector()
        }

        return Word(ClueId(number, orientation), allPts.toList())
    }

    private fun lookupPoint(pt: Point) = pointMap.getOrDefault(pt, BLACK)

    fun updateValue(pt: Point, value: String) = Grid(pointMap + (pt to value))
}