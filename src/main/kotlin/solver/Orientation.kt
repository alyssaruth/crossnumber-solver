package solver

enum class Orientation {
    ACROSS,
    DOWN;

    override fun toString() = when (this) {
        ACROSS -> "A"
        DOWN -> "D"
    }
}