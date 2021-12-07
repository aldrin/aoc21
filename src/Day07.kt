import kotlin.math.abs

fun main() {

    fun part1(input: List<String>): Int {
        val positions = readNumbers(input.first())
        val median = positions.sorted()[positions.size / 2]
        val frequencies = positions.groupingBy { it }.eachCount()
        return frequencies.map { (k, v) -> abs(k - median) * v }.sum()
    }

    fun part2(input: List<String>): Int {
        val positions = readNumbers(input.first())
        val frequencies = positions.groupingBy { it }.eachCount()
        val costs = mutableMapOf<Int, Int>()
        var mid = positions.average().toInt()
        val sumN = { n: Int -> n * (n + 1) / 2 }
        val cost = { n: Int -> costs.computeIfAbsent(n) { frequencies.map { (k, v) -> sumN(abs(k - it)) * v }.sum() } }
        while (cost(mid) > cost(mid - 1)) {
            mid -= 1
        }
        while (cost(mid) > cost(mid + 1)) {
            mid += 1
        }
        return cost(mid)
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 37) { "part 1" }
    check(part2(testInput) == 168) { "part 2" }
    readOptionalInput("Day07")?.let { input ->
        println(part1(input)) // 326132
        println(part2(input)) // 88612508
    }
}
