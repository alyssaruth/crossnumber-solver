package solver.clue

import solver.Clue
import solver.ClueConstructor

/**
 * For standalone clues like:
 *
 *  - A prime number
 *  - A multiple of 101
 *  - The product of two primes
 */
class SimpleClue(private val checker: Clue) : BaseClue() {
    override fun check(value: Long) = checker(value)

    override fun totalCombinations(solutionCombos: Long) = solutionCombos
}

fun emptyClue(): ClueConstructor = simpleClue { true }

fun simpleClue(clue: Clue): ClueConstructor = { _ -> SimpleClue(clue) }