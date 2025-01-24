package solver

class SimpleClue(private val checker: Clue) : BaseClue() {
    override fun check(value: Long) = checker(value)
}

fun emptyClue(): ClueConstructor = simpleClue { true }

fun simpleClue(clue: Clue): ClueConstructor = { _ -> SimpleClue(clue) }