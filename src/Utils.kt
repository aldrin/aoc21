import java.io.File

typealias Grid = Array<IntArray>

/**
 * A cell in a grid.
 */
data class Cell(val row: Int, val column: Int)

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("data", "$name.txt").readLines()

/**
 * Read lines from an optional input file
 */
fun readOptionalInput(name: String) = if (File("data", "$name.txt").exists()) readInput(name) else null

/**
 * Read numbers from the given line
 */
fun readNumbers(line: String, delimit: String = ","): List<Int> = line.split(delimit).map { it.trim().toInt() }

/**
 * Prints a grid of numbers.
 */
fun Grid.print() {
    println()
    for (r in this.indices) {
        for (c in this[r].indices) {
            print("${this[r][c]} ")
        }
        println()
    }
    println()
}

/**
 * Read a grid of numbers
 */
fun readGrid(input: List<String>): Grid {
    val grid = Array(input.size) { IntArray(input[0].length) { 0 } }
    for (r in input.indices) {
        for (c in input[r].indices) {
            grid[r][c] = input[r][c].digitToInt()
        }
    }
    return grid
}