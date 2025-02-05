package solver.digitReducer

import solver.DigitMap
import solver.Point

class DigitReducer(private val squares: Set<Point>, private val filterFn: (Int) -> Boolean) : AbstractDigitReducer() {
    override fun apply(digitMap: DigitMap) = digitMap.mapValues { (square, digits) ->
        if (squares.contains(square)) digits.filter(filterFn) else digits
    }
}
