fun main() {
    val day = "Day09"

    /**
     * Read the grid from the input strings.
     */
    fun readGrid(input: List<String>): Grid {
        val grid = Array(input.size) { IntArray(input.first().length) }
        for (r in input.indices) {
            for (c in 0 until input[r].length) {
                grid[r][c] = input[r][c].digitToInt()
            }
        }
        return grid
    }

    /**
     * Find Cells adjacent to the given Cell on the grid.
     */
    fun findAdjacentCells(Cell: Cell, grid: Grid): Map<Cell, Int> {
        return Cell.let { (r, c) ->
            sequenceOf(Cell(r - 1, c), Cell(r, c - 1), Cell(r, c + 1), Cell(r + 1, c))
                .filter { it.row in grid.indices && it.column in grid[0].indices }
                .map { (row, col) -> Pair(Cell(row, col), grid[row][col]) }
                .toMap()
        }
    }

    /**
     * Find the low Cells on the grid.
     */
    fun findLowCells(grid: Grid): Map<Cell, Int> {
        val lowCells = mutableMapOf<Cell, Int>()
        for (row in grid.indices) {
            for (col in grid[row].indices) {
                val height = grid[row][col]
                if (findAdjacentCells(Cell(row, col), grid).values.all { it > height }) {
                    lowCells[Cell(row, col)] = height
                }
            }
        }
        return lowCells
    }

    /**
     * Find the size of basins for the low-Cells
     */
    fun findBasins(grid: Grid): Map<Cell, Int> {
        val basin = mutableMapOf<Cell, Int>()
        for (Cell in findLowCells(grid).keys) {
            val queue = mutableListOf(Cell)
            val explored = mutableSetOf<Cell>()
            while (queue.isNotEmpty()) { // BFS
                val next = queue.removeFirst()
                if (explored.add(next)) {
                    queue.addAll(findAdjacentCells(next, grid).filterValues { it != 9 }.keys)
                }
            }
            basin[Cell] = explored.size
        }
        return basin
    }

    fun part1(input: List<String>): Int {
        return findLowCells(readGrid(input)).values.sumOf { it + 1 }
    }

    fun part2(input: List<String>): Int {
        return findBasins(readGrid(input)).values.sortedDescending().take(3).reduce { x, y -> x * y }
    }

    val testInput = readInput("${day}_test")
    check(part1(testInput) == 15) { "Part 1" }
    check(part2(testInput) == 1134) { "Part 2" }
    readOptionalInput(day)?.let { input ->
        println(part1(input)) // 524
        println(part2(input)) // 1235430
    }
}