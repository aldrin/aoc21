fun main() {

    /**
     * The input is a graph of big and small caves connected via tunnels.
     */
    class Caves(input: List<String>) {
        val tunnels = mutableMapOf<String, MutableSet<String>>()
        val isSmall = { s: String -> s.all { it.isLowerCase() } }

        init {
            val addEdge = { a: String, b: String -> tunnels.computeIfAbsent(a) { mutableSetOf() }.add(b) }
            for (line in input) {
                val (from, to) = line.split("-").zipWithNext().first()
                addEdge(from, to)
                if (!(from == "start" || to == "end")) {
                    addEdge(to, from)
                }
            }
        }

        /**
         * Find all possible paths to the end.
         *
         * @param current The path we've taken so far.
         * @param from The current cave (node) in our exploration.
         * @param part1 A flag to differentiate between the two parts of the problem.
         */
        fun findPaths(
            current: List<String> = listOf(),
            from: String = "start",
            part1: Boolean = false
        ): Set<List<String>> {
            val pathNow = current + listOf(from)

            if (from == "end") {
                return setOf(pathNow)
            }

            // In part 2, we allow one (and only one) small cave to be repeated on the path.
            val repeatedOneAlready = pathNow.filter(isSmall).groupingBy { it }.eachCount().values.any { it > 1 }

            // Collect paths via all adjacent tunnels.
            val paths = mutableSetOf<List<String>>()
            for (cave in tunnels[from]!!) {
                if (pathNow.contains(cave) && isSmall(cave)) {
                    if (part1 || repeatedOneAlready) {
                        continue
                    }
                }
                paths.addAll(findPaths(pathNow, cave, part1))
            }
            return paths
        }
    }

    val day = "Day12"
    fun part1(input: List<String>) = Caves(input).findPaths(part1 = true).size
    fun part2(input: List<String>) = Caves(input).findPaths().size
    val testInput = readInput("${day}_test")
    check(part1(testInput) == 10) { "Part 1" }
    check(part2(testInput) == 36) { "Part 2" }
    readOptionalInput(day)?.let { input ->
        println(part1(input)) // 5252
        println(part2(input)) // 147784
    }
}