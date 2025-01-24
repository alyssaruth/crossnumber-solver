package solver

class SingleReferenceClue(
    crossnumber: Crossnumber,
    private val otherClueId: ClueId,
    private val checker: (Long, Long) -> Boolean
) : ContextualClue(crossnumber) {

    override fun check(value: Long) =
        when (val otherSolution = crossnumber.solutions.getValue(otherClueId)) {
            is PendingSolution -> true
            is PartialSolution -> otherSolution.possibilities.any { checker(value, it) }
        }
}

fun simpleReference(clueId: String, checker: (Long, Long) -> Boolean): List<ClueConstructor> =
    listOf { crossnumber -> SingleReferenceClue(crossnumber, ClueId.fromString(clueId), checker) }