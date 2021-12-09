// 2 type aliases to save some typing
typealias Point = Pair<Int, Int>
typealias Grid = Array<IntArray>

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
     * Find points adjacent to the given point on the grid.
     */
    fun findAdjacentPoints(point: Point, grid: Grid): Map<Point, Int> {
        return point.let { (r, c) ->
            sequenceOf(Pair(r - 1, c), Pair(r, c - 1), Pair(r, c + 1), Pair(r + 1, c))
                .filter { it.first in grid.indices && it.second in grid[0].indices }
                .map { (row, col) -> Pair(Pair(row, col), grid[row][col]) }
                .toMap()
        }
    }

    /**
     * Find the low points on the grid.
     */
    fun findLowPoints(grid: Grid): Map<Point, Int> {
        val lowPoints = mutableMapOf<Point, Int>()
        for (row in grid.indices) {
            for (col in grid[row].indices) {
                val height = grid[row][col]
                if (findAdjacentPoints(Pair(row, col), grid).values.all { it > height }) {
                    lowPoints[Pair(row, col)] = height
                }
            }
        }
        return lowPoints
    }

    /**
     * Find the size of basins for the low-points
     */
    fun findBasins(grid: Grid): Map<Point, Int> {
        val basin = mutableMapOf<Point, Int>()
        for (point in findLowPoints(grid).keys) {
            val queue = mutableListOf(point)
            val explored = mutableSetOf<Point>()
            while (queue.isNotEmpty()) { // BFS
                val next = queue.removeFirst()
                if (explored.add(next)) {
                    queue.addAll(findAdjacentPoints(next, grid).filterValues { it != 9 }.keys)
                }
            }
            basin[point] = explored.size
        }
        return basin
    }

    fun part1(input: List<String>): Int {
        return findLowPoints(readGrid(input)).values.sumOf { it + 1 }
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