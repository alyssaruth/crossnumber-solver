package com.github.alyssaruth

enum class Orientation {
    ACROSS,
    DOWN;

    override fun toString() = when (this) {
        ACROSS -> "A"
        DOWN -> "D"
    }
}

data class ClueId(val number: Int, val orientation: Orientation) {
    override fun toString() = "${number}$orientation"
}
