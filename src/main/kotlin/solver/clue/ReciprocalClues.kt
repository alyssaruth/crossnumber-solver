package solver.clue

import maths.geometricMean
import solver.ClueConstructor

/**
 * X is multiple of Y => Y is factor of X
 */
fun String.isMultipleOf(other: String) = arrayOf(
    this to isMultipleOfRef(other),
    other to isFactorOfRef(this)
)

/**
 * a = b * c => b = a/c and c = a/b
 */
fun String.isProductOf(a: String, b: String) = listOf(
    this to dualReference(a, b, Long::times),
    a to dualReference(this, b, Long::div),
    b to dualReference(this, a, Long::div)
)

/**
 * X = sqrt(Y * Z) => Z = X^2 / Y and Y = X^2 / Z
 */
fun String.isGeometricMeanOf(a: String, b: String): Array<Pair<String, ClueConstructor>> =
    arrayOf(
        this to multiReference(a, b, combiner = ::geometricMean),
        a to dualReference(this, b) { x, y -> (x * x) / y },
        b to dualReference(this, a) { x, y -> (x * x) / y },
    )


fun clueMap(vararg clues: Pair<String, ClueConstructor>): Map<String, ClueConstructor> =
    clues.fold(emptyMap()) { mapSoFar, (clueId, clue) ->
        val existing = mapSoFar[clueId]
        if (existing == null) {
            mapSoFar + (clueId to clue)
        } else {
            mapSoFar + (clueId to (existing + clue))
        }
    }