package solver.digitReducer

import solver.DigitMap

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
abstract class AbstractDigitReducer {
    abstract fun apply(digitMap: DigitMap): DigitMap
}