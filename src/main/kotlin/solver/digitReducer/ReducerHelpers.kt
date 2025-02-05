package solver.digitReducer

import solver.RawReducer

fun allDigits(filterFn: (Int) -> Boolean) = RawReducer({ it }, filterFn)