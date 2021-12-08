fun main() {
    val day = "Day08"
    fun part1(input: List<String>) = 0
    fun part2(input: List<String>) = 0
    val testInput = readInput("${day}_test")
    check(part1(testInput) == 0)
    check(part2(testInput) == 0)
    readOptionalInput(day)?.let { input ->
        println(part1(input))
        println(part2(input))
    }
}