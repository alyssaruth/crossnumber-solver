package com.github.alyssaruth

data class Point(val x: Int, val y: Int)

operator fun Point.plus(other: Point) = Point(x + other.x, y + other.y)

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

    fun validate() {
        if (xMax != yMax) {
            throw IllegalArgumentException("Grid is not square, detected dimensions: ${xMax + 1}x${yMax + 1}")
        }

        val invalids = pointMap.values.filterNot { it == BLACK || it == WHITE }
        if (invalids.isNotEmpty()) {
            throw IllegalArgumentException("Invalid character(s) detected: $invalids")
        }

        if (rotate().rotate().pointMap != pointMap) {
            throw IllegalArgumentException("Grid is not rotationally symmetric!")
        }
    }
}