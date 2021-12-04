import java.util.*

fun main() {
    /**
     * Collect all the winning rows.
     */
    val rows = (0..20 step 5).map { (it..it + 4).toSet() }.toSet()

    /**
     * Collect all winning columns.
     */
    val columns = (0..4).map { (it..20 + it step 5).toSet() }.toSet()

    /**
     * Collect all winners.
     */
    val winners = rows.union(columns)

    /**
     * Play Bingo with the given board and the given sequence of numbers called out. Return a pair where the first
     * number is the "draw" when this board wins and the second is the score calculated as per the problem definition.
     */
    fun bingo(board: List<Int>, draws: List<Int>): Pair<Int, Int> {
        val haveStamped = BitSet(25)
        for ((draw, number) in draws.withIndex()) {
            val haveIt = board.indexOf(number)
            if (haveIt in 0..25) haveStamped.set(haveIt)
            if (haveStamped.cardinality() >= 5) {
                for (winner in winners) {
                    if (winner.all { haveStamped[it] }) {
                        val unmarked = board.filterIndexed { i, _ -> !haveStamped[i] }.sum()
                        return Pair(draw, unmarked * number)
                    }
                }
            }
        }
        return Pair(-1, -1)
    }

    /**
     * Read the input, play all the boards and return the scores.
     */
    fun play(input: List<String>): List<Pair<Int, Int>> {
        val draws = input.first().split(",").map { it.toInt() }.toList()
        val boardScores = mutableListOf<Pair<Int, Int>>()
        val board = mutableListOf<Int>()
        for (line in input.drop(1)) {
            if (line.isEmpty() && board.isNotEmpty()) {
                boardScores.add(bingo(board, draws))
                board.clear()
            } else {
                line.split(" ").filter { it.isNotBlank() }.forEach { board.add(it.toInt()) }
            }
        }
        boardScores.add(bingo(board, draws))
        return boardScores
    }

    fun part1(input: List<String>): Int {
        return play(input).minByOrNull { it.first }?.second!!
    }

    fun part2(input: List<String>): Int {
        return play(input).maxByOrNull { it.first }?.second!!
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 4512) { "part1" }
    check(part2(testInput) == 1924) { "part2" }

    readOptionalInput("Day04")?.let { input ->
        println(part1(input)) // 58374
        println(part2(input)) // 11377
    }
}
