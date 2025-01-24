package solver

abstract class BaseClue {
    open val onSolve: ((Long) -> Crossnumber)? = null

    abstract fun check(value: Long): Boolean
}

abstract class ContextualClue(protected val crossnumber: Crossnumber) : BaseClue()
