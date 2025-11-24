package solver

enum class Orientation {
    ACROSS,
    DOWN;

    override fun toString() = when (this) {
        ACROSS -> "A"
        DOWN -> "D"
    }

    fun unitVector() = when (this) {
        ACROSS -> Point(1, 0)
        DOWN -> Point(0, 1)
    }
}