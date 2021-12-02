fun main() {
    fun part1(input: List<String>) = input.map { it.toInt() }.zipWithNext().count { it.second > it.first }

    fun part2(input: List<String>) = (2 until input.size).map { input.slice(it - 2..it) }
        .map { window -> window.stream().mapToInt { it.toInt() }.sum() }.zipWithNext().count { it.second > it.first }

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    readOptionalInput("Day01")?.let { input ->
        println(part1(input))
        println(part2(input))
    }
}
