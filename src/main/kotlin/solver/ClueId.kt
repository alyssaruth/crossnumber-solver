package solver

data class ClueId(val number: Int, val orientation: Orientation) {
    override fun toString() = "${number}$orientation"

    companion object {
        fun fromString(clueStr: String): ClueId {
            val orientationStr = clueStr.last().toString()
            val orientation = when (orientationStr) {
                "A" -> Orientation.ACROSS
                "D" -> Orientation.DOWN
                else -> throw IllegalArgumentException("Invalid clue string: $clueStr")
            }

            val number = clueStr.replace(orientationStr, "").toInt()
            return ClueId(number, orientation)
        }
    }
}
