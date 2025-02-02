package solver.clue

import maths.geometricMean
import solver.ClueConstructor

/**
 * X is multiple of Y => Y is factor of X
 */
fun String.isMultipleOf(other: String) = listOf(
    this to isMultipleOfRef(other),
    other to isFactorOfRef(this)
)

/**
 * X = sqrt(Y * Z) => Z = X^2 / Y and Y = X^2 / Z
 */
fun String.isGeometricMeanOf(a: String, b: String): List<Pair<String, ClueConstructor>> =
    listOf(
        this to multiReference(a, b, combiner = ::geometricMean),
        a to dualReference(this, b) { x, y -> (x * x) / y },
        b to dualReference(this, a) { x, y -> (x * x) / y },
    )


/**
 * Collect a list of clues into a single map, combining clues if the same id comes up
 */
fun List<Pair<String, ClueConstructor>>.collectClues(): Map<String, ClueConstructor> =
    fold(emptyMap()) { mapSoFar, (clueId, clue) ->
        val existing = mapSoFar[clueId]
        if (existing == null) {
            mapSoFar + (clueId to clue)
        } else {
            mapSoFar + (clueId to (existing + clue))
        }
    }