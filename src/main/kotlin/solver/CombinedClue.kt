package solver

class CombinedClue(private val clueOne: BaseClue, private val clueTwo: BaseClue) : BaseClue() {
    override fun check(value: Long) = clueOne.check(value) && clueTwo.check(value)
}

operator fun ClueConstructor.plus(other: ClueConstructor): ClueConstructor {
    return other
}