fun main() {
    val day = "Day11"

    /**
     * Read energy levels.
     */
    fun readGrid(input: List<String>): Grid {
        val energy = Array(input.size) { IntArray(input[0].length) { 0 } }
        for (r in input.indices) {
            for (c in input[r].indices) {
                energy[r][c] = input[r][c].digitToInt()
            }
        }
        return energy
    }

    /**
     * Bump energy levels, all over the grid.
     */
    fun Grid.bumpAll() {
        for (r in this.indices) {
            for (c in this[r].indices) {
                this[r][c]++
            }
        }
    }

    /**
     * Bump energy levels, around a particular cell.
     */
    fun Grid.bumpAround(cell: Cell) {
        val rows = (cell.row - 1..cell.row + 1).filter { it in this.indices }
        val cols = (cell.column - 1..cell.column + 1).filter { it in this[cell.row].indices }
        rows.forEach { r -> cols.forEach { c -> this[r][c]++ } }
    }

    /**
     * Collect cells that will flash next.
     */
    fun Grid.willFlash(): Set<Cell> {
        return this.indices.flatMap { row ->
            this[row].indices.mapNotNull { col ->
                Cell(row, col).takeIf { this[row][col] > 9 }
            }
        }.toSet()
    }

    /**
     * Take one step and return the number of octopuses that flashed.
     */
    fun Grid.step(): Int {
        bumpAll()
        var candidates = willFlash()
        val flashed = mutableSetOf<Cell>()
        while (candidates.isNotEmpty()) {
            candidates.forEach { bumpAround(it) }
            flashed.addAll(candidates)
            candidates = willFlash().subtract(flashed)
        }
        flashed.forEach { (r, c) -> this[r][c] = 0 }
        return flashed.size
    }

    fun part1(input: List<String>, steps: Int = 100): Int {
        val grid = readGrid(input)
        return (steps downTo 1).sumOf { grid.step() }
    }

    fun part2(input: List<String>): Int {
        var step = 1
        val grid = readGrid(input)
        val target = grid.size * grid[0].size
        while (grid.step() != target) {
            step++
        }
        return step
    }

    val testInput = readInput("${day}_test")
    check(part1(testInput) == 1656) { "Part 1" }
    check(part2(testInput) == 195) { "Part 2" }
    readOptionalInput(day)?.let { input ->
        println(part1(input)) // 1785
        println(part2(input)) // 354
    }
}