package maths

fun degreesToFahrenheit(degrees: Long) = 32 + (degrees * 9) / 5

fun String.sorted() = toCharArray().sorted().joinToString("")

fun String.isPalindrome() = reversed() == this