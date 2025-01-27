package maths

tailrec fun inPence(n: Int, coinsSoFar: List<Int> = emptyList()): List<Int> =
    if (n == 0) {
        coinsSoFar
    } else if (n >= 50) {
        inPence(n - 50, coinsSoFar + 50)
    } else if (n >= 20) {
        inPence(n - 20, coinsSoFar + 20)
    } else if (n >= 10) {
        inPence(n - 10, coinsSoFar + 10)
    } else if (n >= 5) {
        inPence(n - 5, coinsSoFar + 5)
    } else if (n >= 2) {
        inPence(n - 2, coinsSoFar + 2)
    } else {
        inPence(n - 1, coinsSoFar + 1)
    }