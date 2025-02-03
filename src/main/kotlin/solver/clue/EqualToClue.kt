package solver.clue

import solver.ClueConstructor

/**
 * Optimised clue for when we know or can directly compute the answer.
 * For clues like:
 *
 *  - The largest prime factor of 733626510400
 *  - The number of consecutive non-prime numbers starting at (and including) 370262
 */
class EqualToClue(val value: Long) : BaseClue() {
    override val filterFunction: (possibles: List<Long>, predicate: (Long) -> Boolean) -> List<Long> =
        { _, _ -> listOf(value) }

    override fun check(value: Long) = value == this.value // unused
}

fun isEqualTo(answer: Long): ClueConstructor = { _ -> EqualToClue(answer) }
