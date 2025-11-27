import solver.ClueId
import solver.Crossnumber
import solver.DigitMap
import solver.Orientation
import solver.RAM_THRESHOLD
import solver.parseGrid

val VALID_GRID = """
            .....###.....#
            .#........##.#
            .##.###.......
            ........#.##.#
            .######.#....#
            ............#.
            .#.###..#.#...
            ...#.#..###.#.
            .#............
            #....#.######.
            #.##.#........
            .......###.##.
            #.##........#.
            #.....###.....
        """.trimIndent()

val CLUE_ID = ClueId(1, Orientation.ACROSS)

fun dummyCrossnumber(digitMap: DigitMap): Crossnumber =
    Crossnumber(parseGrid(VALID_GRID), digitMap, emptyMap(), emptyList(), emptyList(), baseLoopThreshold = RAM_THRESHOLD, guessThreshold = 50)