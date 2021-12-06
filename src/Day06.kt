fun main() {

    fun simulate(input: List<String>, days: Int = 256): Long {
        // Initialize population map and count fishes with a particular timer.
        val population = mutableMapOf<Int, Long>()
        (0..8).forEach { population[it] = 0 }
        input.first().split(",").map { it.toInt() }.forEach {
            population.compute(it) { _, count -> (count ?: 0) + 1 }
        }
        // Simulate population changes for each day
        for (day in days downTo 1) {
            // Retain zero population before updating it, it goes to two places
            val zero = population[0]!!

            // Other timers just shift up
            (0..7).forEach { population[it] = population[it + 1]!! }

            // Zeros go to 6 and 8
            population[6] = population[6]!! + zero
            population[8] = zero
        }
        return population.values.sum()
    }

    fun part1(input: List<String>): Int {
        return simulate(input, 80).toInt()
    }

    fun part2(input: List<String>): Long {
        return simulate(input, 256)
    }

    val testInput = readInput("Day06_test")
    check(part1(testInput) == 5934) { "part 1" }
    check(part2(testInput) == 26984457539L) { "part 2" }

    readOptionalInput("Day06")?.let { input ->
        println(part1(input)) // 350917
        println(part2(input)) // 1592918715629
    }
}
