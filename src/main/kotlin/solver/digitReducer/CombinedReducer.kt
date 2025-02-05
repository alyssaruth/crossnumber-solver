package solver.digitReducer

import solver.DigitMap

class CombinedReducer(private val reducerOne: AbstractDigitReducer, private val reducerTwo: AbstractDigitReducer) :
    AbstractDigitReducer() {
    override fun apply(digitMap: DigitMap) = reducerOne.apply(reducerTwo.apply(digitMap))
}

operator fun AbstractDigitReducer.plus(other: AbstractDigitReducer) = CombinedReducer(this, other)