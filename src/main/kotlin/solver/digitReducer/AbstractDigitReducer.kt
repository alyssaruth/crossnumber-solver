package solver.digitReducer

import solver.ClueId
import solver.Crossnumber
import solver.DigitMap
import solver.Point

typealias SquareSelector = (List<Point>) -> Point

typealias MultiSquareSelector = (List<Point>) -> List<Point>

typealias DigitReducerConstructor = (crossnumber: Crossnumber) -> AbstractDigitReducer

/**
 * For capturing direct digit implications, which can be applied straight to the digit map
 *
 * E.g. Suppose we know a 16-digit number in the grid is divisible by 100.
 *
 * Then we know its last two digits are zeros - and this is something we can directly update in the digit map *without*
 * needing to loop through all the possibilities.
 *
 * Most crossnumbers don't *require* these, but some do (like Crossnumber 7), and others can get a speed benefit too
 */
abstract class AbstractDigitReducer(
    val clueId: ClueId,
    squareSelector: MultiSquareSelector,
    protected val crossnumber: Crossnumber
) {
    protected val squares = selectSquares(clueId, squareSelector)

    protected fun selectSquares(clueId: ClueId, squareSelector: MultiSquareSelector): List<Point> {
        val clueSquares = crossnumber.solutions.getValue(clueId).squares
        return squareSelector(clueSquares)
    }

    abstract fun apply(digitMap: DigitMap): DigitMap
}