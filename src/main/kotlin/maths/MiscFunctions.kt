package maths

fun degreesToFahrenheit(degrees: Long) = 32 + (degrees * 9) / 5

fun String.sorted() = toCharArray().sorted().joinToString("")

fun String.isPalindrome() = reversed() == this

fun <E> List<List<E>>.allCombinations(): List<List<E>> =
    fold(listOf(emptyList())) { listsSoFar, newSolutions ->
        listsSoFar.flatMap { list -> newSolutions.map { x -> list + x } }
    }