import java.util.function.Predicate

fun main() {
    val day = "Day08"

    /**
     * A single signal entry.
     */
    data class Entry(val signals: List<String>, val output: List<String>)

    /**
     * Read the input into a list of entries.
     */
    fun readInput(input: List<String>): List<Entry> {
        return input.flatMap { l ->
            l.split(" ", "|").filter { it.isNotBlank() }.windowed(10, 10, partialWindows = true).windowed(2, 2)
                .map { Entry(it[0], it[1]) }
        }.toList()
    }

    fun part1(input: List<String>): Int {
        return readInput(input).flatMap { it.output }.count { it.length in setOf(2, 4, 3, 7) }
    }

    fun part2(input: List<String>): Int {
        var sum = 0
        val segments = listOf("abcefg", "cf", "acdeg", "acdfg", "bcdf", "abdfg", "abdefg", "acf", "abcdefg", "abcdfg")
        val toSet = { x: String? -> x!!.toCharArray().sorted().toSet() }
        val pick = { e: Entry, p: Predicate<String> -> toSet(e.signals.find { p.test(it) }) }
        val rewire = mutableMapOf<Char, Char>()

        for (entry in readInput(input)) {
            // Identify numbers, from the number of segments
            val one = pick(entry) { it.length == 2 }
            val four = pick(entry) { it.length == 4 }
            val eight = pick(entry) { it.length == 7 }
            val seven = pick(entry) { it.length == 3 }

            // 3 has 5 segments. So do 2 and 5 but 3 is the only one that totally eclipses one.
            val three = pick(entry) { it.length == 5 && toSet(it).intersect(one) == one }

            // 9 has 6 segments. So do 0 and 6 but 9 is the only one that totally eclipses four.
            val nine = pick(entry) { it.length == 6 && toSet(it).intersect(four) == four }

            // Decode signals (this is best understood with pen and paper).
            val a = seven.subtract(one)
            val e = eight.subtract(nine)
            val two = pick(entry) { it.length == 5 && toSet(it).intersect(e).isNotEmpty() }
            val g = nine.subtract(seven.union(four))
            val d = three.subtract(seven.union(g))
            val b = four.subtract(one.union(d))
            val c = two.subtract(a).subtract(d).subtract(e).subtract(g)
            val f = one.subtract(c)

            // Record what we learned.
            rewire[a.first()] = 'a'
            rewire[b.first()] = 'b'
            rewire[c.first()] = 'c'
            rewire[d.first()] = 'd'
            rewire[e.first()] = 'e'
            rewire[f.first()] = 'f'
            rewire[g.first()] = 'g'

            // Rewire output, convert to number and add it up.
            val rewired = entry.output.map { toSet(it).map { c -> rewire[c]!! }.sortedBy { c -> c }.joinToString("") }
            sum += rewired.joinToString("") { segments.indexOf(it).toString() }.toInt()
        }
        return sum
    }

    val testInput = readInput("${day}_test")

    check(part1(testInput) == 26) { "Part 1" }
    check(part2(testInput) == 61229) { "Part 2" }

    readOptionalInput(day)?.let { input ->
        println(part1(input)) // 493
        println(part2(input)) // 1010460
    }
}