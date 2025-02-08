package solver.clue

import maths.areCoprime
import maths.geometricMean
import maths.hcf
import maths.lcm
import maths.longOrNull
import maths.wholeDiv
import solver.ClueConstructor
import kotlin.math.abs

/**
 * X is multiple of Y <=> Y is factor of X
 */
fun String.isMultipleOf(otherClue: String) =
    calculationWithReference(otherClue) { value, other -> maths.isMultipleOf(other)(value) }

fun String.isFactorOf(other: String) = other.isMultipleOf(this)

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
 * X = |Y-Z|/2  =>  Y = Z - 2X or 2X + Z, depending on whether Y > Z or Y < Z. And same for Z.
 */
fun String.isHalfTheDifferenceBetween(a: String, b: String): Array<Pair<String, ClueConstructor>> =
    arrayOf(
        this to dualReference(a, b) { x, y -> abs(x - y) / 2 },
        a to tripleReference(this, a, b) { diff, x, y -> if (x > y) y + (2 * diff) else y - (2 * diff) },
        b to tripleReference(this, b, a) { diff, x, y -> if (x > y) y + (2 * diff) else y - (2 * diff) },
    )

/**
 * X is HCF(a, b) =>  a is multiple of X and b is multiple of X
 */
fun String.isHcfOf(a: String, b: String): Array<Pair<String, ClueConstructor>> =
    arrayOf(
        this to dualReference(a, b, ::hcf),
        a to makeCalculationWithReference(this) { value, other -> maths.isMultipleOf(other)(value) },
        b to makeCalculationWithReference(this) { value, other -> maths.isMultipleOf(other)(value) },
    )

/**
 * X is LCM(a, b) => a is a factor of x, b is a factor of x
 */
fun String.isLcmOf(a: String, b: String): Array<Pair<String, ClueConstructor>> =
    arrayOf(
        this to dualReference(a, b, ::lcm),
        a to makeCalculationWithReference(this) { value, other -> maths.isMultipleOf(value)(other) },
        b to makeCalculationWithReference(this) { value, other -> maths.isMultipleOf(value)(other) },
    )

fun String.isDifferenceBetween(a: String, b: String): Array<Pair<String, ClueConstructor>> =
    arrayOf(
        this to dualReference(a, b) { x, y -> abs(x - y) },
        a to tripleReference(this, a, b) { diff, x, y -> if (x > y) y + diff else y - diff },
        b to tripleReference(this, b, a) { diff, x, y -> if (x > y) y + diff else y - diff },
    )

fun String.isEqualTo(otherClue: String) = this.calculationWithReference(otherClue, Long::equals)

fun String.isNotEqualTo(otherClue: String) = this.calculationWithReference(otherClue) { value, other -> value != other }

fun String.isGreaterThan(otherClue: String) = this.calculationWithReference(otherClue) { value, other -> value > other }

fun String.isLessThan(other: String) = other.isGreaterThan(this)

/**
 * X = f(Y) => f(Y) = X
 */
fun String.singleReference(other: String, mapper: (Long) -> Long?): Array<Pair<String, ClueConstructor>> =
    arrayOf(
        this to makeSingleReference(other, mapper),
        other to transformedEqualsRef(this, mapper)
    )

fun String.singleReferenceFlattened(other: String, mapper: (Long) -> List<Long>): Array<Pair<String, ClueConstructor>> =
    arrayOf(
        this to makeSingleReferenceFlattened(other, mapper),
        other to transformedEqualsRefFlattened(this, mapper)
    )

fun String.transformedEquals(other: String, mapper: (Long) -> Long) = other.singleReference(this, mapper)

fun String.isCoprimeWith(otherClue: String) = calculationWithReference(otherClue, ::areCoprime)

fun String.calculationWithReference(
    otherClue: String,
    checker: (Long, Long) -> Boolean
): Array<Pair<String, ClueConstructor>> =
    arrayOf(
        this to makeCalculationWithReference(otherClue, checker),
        otherClue to makeCalculationWithReference(this) { value, other -> checker(other, value) },
    )

/**
 * Only sound if the checker doesn't care about the order of the clues!
 */
fun String.calculationWithReferences(
    vararg otherClues: String,
    checker: (List<Long>) -> Boolean
): Array<Pair<String, ClueConstructor>> {
    val reciprocals = otherClues.map { other ->
        val othersNotMe = otherClues.toList() - other
        other to makeCalculationWithReferences(this, *othersNotMe.toTypedArray(), checker = checker)
    }.toTypedArray()

    return reciprocals + arrayOf(
        this to makeCalculationWithReferences(*otherClues, checker = checker)
    )
}
