import java.util.*

fun main() {
    val day = "Day15"

    /**
     * Part 2 expands the grid based on certain rules.
     */
    fun Grid.expand(times: Int = 5) = Array(size * times) { row ->
        IntArray(size * times) { col ->
            // Cells in expanded grid refer back to the original grid
            val referenceRow = if (row in indices) row else row % size
            val referenceColumn = if (col in indices) col else col % size

            // The values are increased based on how far from reference we are
            val increment = (row / size) + (col / size)
            val value = (this[referenceRow][referenceColumn] + increment)

            // Values greater than 10 need to be adjusted.
            value.takeIf { it < 10 } ?: (value - 9)
        }
    }

    /**
     * The valid neighbors of a cell in the grid.
     */
    fun Grid.neighbors(cell: Cell) = sequenceOf(
        Cell(cell.row, cell.column - 1),            // up
        Cell(cell.row, cell.column + 1),            // down
        Cell(cell.row - 1, cell.column),              // left
        Cell(cell.row + 1, cell.column)               // right
    ).filter { it.row in indices && it.column in indices } // drop those that fall outside

    /**
     * Get the last cell in the grid.
     */
    fun Grid.lastCell(): Cell {
        return Cell(indices.last, this[indices.last].indices.last)
    }

    /**
     * Dijkstra's shortest path to compute the least risky path. We need the priority-queue for part 2 where the input
     * becomes too large the simpler variant.
     */
    fun route(grid: Grid, destination: Cell = grid.lastCell()): Int {
        val risk = mutableMapOf<Cell, Int>()
        val riskTracker = PriorityQueue<Pair<Cell, Int>>(compareBy { it.second })

        // Optimize (not sure if needed) the check to see if the given cell has already been explored.
        val seen = BitSet(grid.size * grid.size)
        val haveSeen = { cell: Cell -> seen[cell.row * grid.size + cell.column] }
        val markSeen = { cell: Cell -> seen.set(cell.row * grid.size + cell.column) }
        val notSeenAll = { seen.cardinality() < grid.size * grid.size }

        // Initialize the search
        val source = Cell(0, 0)
        risk[source] = 0
        riskTracker.add(source to 0)

        // Loop till we haven't seen all the nodes
        while (notSeenAll()) {

            // Remove the least one we have seen so far
            val (current, currentRisk) = riskTracker.remove()
            markSeen(current)

            // Visit all its neighbors
            grid.neighbors(current).filter { !haveSeen(it) }.forEach {
                val leastSoFar = risk[it]
                val viaCurrent = currentRisk + grid[it.row][it.column]
                // Java standard PriorityQueue does not support changing priorities.
                // So we need to handle the update case based on what really happens
                if (leastSoFar == null) { // first time entry. simple case
                    risk[it] = viaCurrent
                    riskTracker.add(it to viaCurrent)
                } else if (leastSoFar > viaCurrent) { // updating the distance
                    risk[it] = viaCurrent
                    riskTracker.remove(it to leastSoFar)
                    riskTracker.add(it to viaCurrent)
                }
            }
        }

        // Return the risk of the destination
        return risk[destination]!!
    }

    fun part1(input: List<String>) = route(readGrid(input))
    fun part2(input: List<String>) = route(readGrid(input).expand())

    val testInput = readInput("${day}_test")
    check(part1(testInput) == 40) { "Part 1" }
    check(part2(testInput) == 315) { "Part 2" }
    readOptionalInput(day)?.let { input ->
        println(part1(input)) // 398
        println(part2(input)) // 2817
    }
}