package solver

class DualReferenceClue(
    crossnumber: Crossnumber,
    private val clueA: ClueId,
    private val clueB: ClueId,
    private val combiner: (Long, Long) -> Long
) :
    ContextualClue(crossnumber) {
    private val potentialSolutions = computePotentialSolutions()

    override fun check(value: Long) = potentialSolutions?.contains(value) ?: true

    private fun computePotentialSolutions(): Set<Long>? {
        val aValues = lookupAnswers(clueA) ?: return null
        val bValues = lookupAnswers(clueB) ?: return null

        if (aValues.size * bValues.size > RAM_THRESHOLD) {
            return null
        }

        return aValues.flatMap { a -> bValues.map { b -> combiner(a, b) } }.toSet()
    }
}

fun dualReference(clueA: String, clueB: String, combiner: (Long, Long) -> Long): ClueConstructor =
    { crossnumber -> DualReferenceClue(crossnumber, ClueId.fromString(clueA), ClueId.fromString(clueB), combiner) }