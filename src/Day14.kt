fun main() {
    val day = "Day14"

    fun polymerReaction(input: List<String>, steps: Int = 40): Long {
        val template = input.first()
        val rules = input.drop(2).associate { it.split("->").map { s -> s.trim() }.zipWithNext().first() }

        // We don't need the exact template text that's where I started, and it works for 25 steps or so, but after
        // that the polymer becomes too long to fit in memory. Instead, all we need is the count of pairs and track
        // the character counts and the number of pairs we have in the polymer. The following two counts do just that.
        var pairCounts = template.windowed(2).groupingBy { it }.fold(0L) { count, _ -> count.inc() }
        val charCounts = template.groupingBy { it }.fold(0L) { count, _ -> count.inc() }.toMutableMap()

        // Extract the increment routine to avoid repeating it again.
        val increment = { current: Long?, increment: Long -> current?.plus(increment) ?: increment }

        // Go through the steps, replace pair counts with new ones, based on the rewrite rules.
        repeat(steps) {
            val newPairCounts = mutableMapOf<String, Long>()
            pairCounts.filter { it.key in rules }.forEach { (pair, count) ->
                val match = rules[pair]!!
                val first = "${pair[0]}${match}"
                val second = "${match}${pair[1]}"
                newPairCounts.compute(first) { _, current -> increment(current, count) }
                newPairCounts.compute(second) { _, current -> increment(current, count) }
                charCounts.compute(match[0]) { _, current -> increment(current, count) }
            }
            pairCounts = newPairCounts
        }

        // Return the difference of the most frequent and the least frequent values.
        return charCounts.values.maxOf { it } - charCounts.values.minOf { it }
    }

    fun part1(input: List<String>) = polymerReaction(input, 10)
    fun part2(input: List<String>) = polymerReaction(input)
    val testInput = readInput("${day}_test")
    check(part1(testInput) == 1588L) { "Part 1" }
    check(part2(testInput) == 2188189693529L) { "Part 2" }
    readOptionalInput(day)?.let { input ->
        println(part1(input)) // 2657
        println(part2(input)) // 2188189693529
    }
}