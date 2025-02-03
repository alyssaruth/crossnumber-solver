package solver.clue

import maths.geometricMean
import maths.longOrNull
import maths.wholeDiv
import solver.ClueConstructor
import kotlin.math.abs

/**
 * X is multiple of Y <=> Y is factor of X
 */
fun String.isMultipleOf(other: String) = arrayOf(
    this to isMultipleOfRef(other),
    other to isFactorOfRef(this)
)

fun String.isFactorOf(other: String) = arrayOf(
    this to isFactorOfRef(other),
    other to isMultipleOfRef(this)
)

/**
 * a = b * c => b = a/c and c = a/b
 */
fun String.isProductOf(a: String, b: String) = arrayOf(
    this to dualReference(a, b, Long::times),
    a to dualReference(this, b, ::wholeDiv),
    b to dualReference(this, a, ::wholeDiv)
)

/**
 * a = b + c + d =>  b = a - c - d  and  c = a - b - d  and  d = a - b - c
 */
fun String.isSumOf(vararg others: String): Array<Pair<String, ClueConstructor>> {
    val reciprocals = others.map { other ->
        val othersNotMe = others.toList() - other
        other to multiReference(this, *othersNotMe.toTypedArray()) { it.first() - it.drop(1).sum() }
    }

    return arrayOf(
        this to multiReference(*others) { it.sum() },
    ) + reciprocals
}

/**
 * X = (Y + Z)/2  =>  Y = 2X - Z  and  Z = 2X - Y
 */
fun String.isMeanOf(vararg others: String): Array<Pair<String, ClueConstructor>> {
    val reciprocals = others.map { other ->
        val othersNotMe = others.toList() - other
        other to multiReference(this, *othersNotMe.toTypedArray()) { (others.size * it.first()) - it.drop(1).sum() }
    }
    return arrayOf(
        this to multiReference(*others) { l -> l.average().longOrNull() },
    ) + reciprocals
}


/**
 * X = sqrt(Y * Z) => Z = X^2 / Y and Y = X^2 / Z
 */
fun String.isGeometricMeanOf(a: String, b: String): Array<Pair<String, ClueConstructor>> =
    arrayOf(
        this to multiReference(a, b, combiner = ::geometricMean),
        a to dualReference(this, b) { x, y -> (x * x) / y },
        b to dualReference(this, a) { x, y -> (x * x) / y },
    )

/**
 * X = |Y-Z|/2  =>  Y = 2X - Z or 2X + Z, whichever is positive. And same for X.
 */
fun String.isHalfTheDifferenceBetween(a: String, b: String): Array<Pair<String, ClueConstructor>> =
    arrayOf(
        this to dualReference(a, b) { x, y -> abs(x - y) / 2 },
        a to dualReference(this, b) { x, y -> listOf(y - (2 * x), y + (2 * x)).first { it > 0 } },
        b to dualReference(this, a) { x, y -> listOf(y - (2 * x), y + (2 * x)).first { it > 0 } },
    )

fun String.isDifferenceBetween(a: String, b: String): Array<Pair<String, ClueConstructor>> =
    arrayOf(
        this to dualReference(a, b) { x, y -> abs(x - y) },
        // a to dualReference(this, b) { x, y -> listOf(y - x, y + x).first { it > 0 } },
        // b to dualReference(this, a) { x, y -> listOf(y - x, y + x).first { it > 0 } },
    )

/**
 * X = f(Y) => f(Y) = X
 */
fun String.singleReference(other: String, mapper: (Long) -> Long): Array<Pair<String, ClueConstructor>> =
    arrayOf(
        this to makeSingleReference(other, mapper),
        other to transformedEqualsRef(this, mapper)
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