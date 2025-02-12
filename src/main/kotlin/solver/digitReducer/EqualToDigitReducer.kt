package solver.digitReducer

import solver.ClueId
import solver.Crossnumber
import solver.DigitMap

class EqualToDigitReducer(clueA: ClueId, clueB: ClueId, crossnumber: Crossnumber) :
    AbstractDigitReducer(clueA, allDigits(), crossnumber) {
    private val otherSquares = selectSquares(clueB, allDigits())

    override fun apply(digitMap: DigitMap) = digitMap.mapValues { (square, digits) ->
        val ix = maxOf(squares.indexOf(square), otherSquares.indexOf(square))
        if (ix > 0) {
            val newOptions = digitMap.getValue(squares[ix]).intersect(digitMap.getValue(otherSquares[ix]))
            newOptions.toList().sorted()
        } else {
            digits
        }
    }
}

fun String.allDigitsEqualTo(other: String): DigitReducerConstructor =
    { crossnumber -> EqualToDigitReducer(ClueId.fromString(this), ClueId.fromString(other), crossnumber) }