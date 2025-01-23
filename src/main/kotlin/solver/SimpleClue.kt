package solver

class SimpleClue(private val checker: Clue) : BaseClue() {
    override fun check(value: Long) = checker(value)
}

fun wrapSimpleClue(clue: Clue): ClueConstructor = { _ -> SimpleClue(clue) }

fun simpleClues(vararg clues: Clue): List<ClueConstructor> = clues.map(::wrapSimpleClue)