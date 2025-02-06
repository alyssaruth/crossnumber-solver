package solver.digitReducer

import solver.ClueId
import solver.Crossnumber
import solver.DigitMap

class ReferentialDigitReducer(
    private val filterFn: (Int, List<Int>) -> Boolean,
    clueId: ClueId,
    squareSelector: MultiSquareSelector,
    otherClueId: ClueId,
    otherSquareSelector: SquareSelector,
    crossnumber: Crossnumber
) : AbstractDigitReducer(clueId, squareSelector, crossnumber) {
    private val otherOptions = currentOptions(otherClueId, otherSquareSelector)

    private fun currentOptions(otherClueId: ClueId, otherSquareSelector: SquareSelector): List<Int> {
        val squares = crossnumber.solutions.getValue(otherClueId).squares
        val square = otherSquareSelector(squares)
        return crossnumber.digitMap.getValue(square)
    }

    override fun apply(digitMap: DigitMap) = digitMap.mapValues { (square, digits) ->
        if (squares.contains(square)) digits.filter { digit -> filterFn(digit, otherOptions) } else digits
    }
}

fun String.digitReference(
    squareSelector: MultiSquareSelector,
    other: String,
    otherSquareSelector: SquareSelector,
    filterFn: (Int, List<Int>) -> Boolean
): DigitReducerConstructor = { crossnumber ->
    ReferentialDigitReducer(
        filterFn,
        ClueId.fromString(this),
        squareSelector,
        ClueId.fromString(other),
        otherSquareSelector,
        crossnumber
    )
}