package solver

abstract class BaseClue {
    abstract fun check(value: Long): Boolean
}

abstract class ContextualClue(protected val crossnumber: Crossnumber) : BaseClue()
