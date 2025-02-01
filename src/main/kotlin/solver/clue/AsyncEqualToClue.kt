package solver.clue

import solver.ClueConstructor
import java.util.UUID

private val asyncWorkResults = mutableMapOf<UUID, Long>()

/**
 * For when there's a computation that we want to kick off in parallel to the rest of the crossnumber.
 * Will do no filtering until the result has been returned
 * If the solve gets stuck before this then any async clue(s) will be awaited.
 *
 * For clues like:
 *
 *  - The number of primes less than 100,000,000
 */
class AsyncEqualToClue(private val id: UUID, private val worker: Thread) : BaseClue() {
    override fun check(value: Long) = if (isPending()) true else value == asyncWorkResults.getValue(id)

    fun isPending() = worker.isAlive

    fun await() = worker.join()
}

fun asyncEquals(work: () -> Long): ClueConstructor {
    val workId = UUID.randomUUID()
    val t = Thread {
        asyncWorkResults[workId] = work()
    }
    t.start()

    return { _ ->
        AsyncEqualToClue(workId, t)
    }
}