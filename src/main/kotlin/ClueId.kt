package com.github.alyssaruth

enum class Orientation {
    ACROSS,
    DOWN
}

data class ClueId(val number: Int, val orientation: Orientation)
