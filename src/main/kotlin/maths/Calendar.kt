package maths

import kotlinx.datetime.Instant

/**
 * Using "Rata Die" method, see https://en.wikipedia.org/wiki/Determination_of_the_day_of_the_week#Rata_Die
 */
fun dayOfWeek(dateString: String): Int {
    val parsed = Instant.parse("${dateString}T00:00:00+00:00")
    val comparison = Instant.parse("0001-01-01T00:00:00+00:00")
    val duration = parsed.minus(comparison).inWholeDays + 1
    val mod7 = duration.mod(7)

    // Java's Calendar constants are 1 more than the ISO standard ones
    return mod7 + 1
}