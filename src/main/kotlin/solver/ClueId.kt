package solver

data class ClueId(val number: Int, val orientation: Orientation) {
    override fun toString() = "${number}$orientation"
}
