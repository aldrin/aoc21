fun main() {
    fun part1(input: List<String>): Int {
        var x = 0
        var y = 0
        for (step in input) {
            val next = step.split(" ")
            val magnitude = next[1].toInt()
            when (next[0]) {
                "forward" -> x += magnitude
                "up" -> y -= magnitude
                "down" -> y += magnitude
            }
        }
        return x * y
    }

    fun part2(input: List<String>): Int {
        var x = 0
        var y = 0
        var aim = 0
        for (step in input) {
            val next = step.split(" ")
            val magnitude = next[1].toInt()
            when (next[0]) {
                "forward" -> {
                    x += magnitude
                    y += aim * magnitude
                }
                "up" -> aim -= magnitude
                "down" -> aim += magnitude
            }
        }
        return x * y
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    readOptionalInput("Day02")?.let { input ->
        println(part1(input))
        println(part2(input))
    }
}
