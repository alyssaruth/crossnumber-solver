package solver.digitReducer

import solver.ClueId
import solver.Crossnumber
import solver.DigitMap

class SimpleDigitReducer(
    private val filterFn: (Int) -> Boolean,
    clueId: ClueId,
    squareSelector: MultiSquareSelector,
    crossnumber: Crossnumber
) : AbstractDigitReducer(clueId, squareSelector, crossnumber) {
    override fun apply(digitMap: DigitMap) = digitMap.mapValues { (square, digits) ->
        if (squares.contains(square)) digits.filter(filterFn) else digits
    }
}

fun String.simpleReducer(squareSelector: MultiSquareSelector, filterFn: (Int) -> Boolean): DigitReducerConstructor =
    { crossnumber ->
        val clueId = ClueId.fromString(this)
        SimpleDigitReducer(filterFn, clueId, squareSelector, crossnumber)
    }