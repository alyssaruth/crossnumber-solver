import solver.ClueId
import solver.Orientation

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