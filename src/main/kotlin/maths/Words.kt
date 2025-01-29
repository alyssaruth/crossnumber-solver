package maths

val VOWELS = setOf('a', 'e', 'i', 'o', 'u')

fun String.vowels() = filter(VOWELS::contains)

fun inWords(number: Long) = constructWords(number)

private fun constructWords(number: Long, descriptionSoFar: String = ""): String {
    return if (number == 0L) {
        descriptionSoFar
    } else if (number < 10) {
        append(descriptionSoFar, digitToWord(number.toInt()))
    } else if (number < 20) {
        append(descriptionSoFar, teensDesc(number.toInt()))
    } else if (number < 100) {
        val tens = number / 10
        constructWords(number - (10 * tens), append(descriptionSoFar, tensDesc(tens.toInt())))
    } else if (number < 1000) {
        val hundreds = number / 100
        val remainder = number - (hundreds * 100)
        val appended = append(descriptionSoFar, "${digitToWord(hundreds.toInt())} hundred")
        constructWords(remainder, appendAnd(appended, remainder))
    } else if (number < 1_000_000) {
        val thousands = number / 1000
        val remainder = number - (thousands * 1000)
        val thousandsDesc = constructWords(thousands)
        val appended = append(descriptionSoFar, "$thousandsDesc thousand")
        constructWords(number - (thousands * 1000), appendAnd(appended, remainder))
    } else {
        throw Exception("Unable to describe number $number")
    }
}

private fun appendAnd(currentDescription: String, remainder: Long): String =
    if (remainder in (1..100)) "$currentDescription and" else currentDescription

private fun append(currentDescription: String, next: String, sep: String = " ") =
    if (currentDescription.isNotEmpty()) "$currentDescription$sep$next" else next

private fun digitToWord(digit: Int) = when (digit) {
    1 -> "one"
    2 -> "two"
    3 -> "three"
    4 -> "four"
    5 -> "five"
    6 -> "six"
    7 -> "seven"
    8 -> "eight"
    9 -> "nine"
    else -> throw Exception("Invalid digit $digit")
}

private fun teensDesc(number: Int) = when (number) {
    10 -> "ten"
    11 -> "eleven"
    12 -> "twelve"
    13 -> "thirteen"
    14 -> "fourteen"
    15 -> "fifteen"
    16 -> "sixteen"
    17 -> "seventeen"
    18 -> "eighteen"
    19 -> "nineteen"
    else -> throw Exception("Invalid number $number")
}

private fun tensDesc(digit: Int) = when (digit) {
    2 -> "twenty"
    3 -> "thirty"
    4 -> "forty"
    5 -> "fifty"
    6 -> "sixty"
    7 -> "seventy"
    8 -> "eighty"
    9 -> "ninety"
    else -> throw Exception("Invalid digit $digit")
}