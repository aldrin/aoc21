fun main() {
    val day = "Day10"
    val openingChars = mapOf('(' to ')', '{' to '}', '[' to ']', '<' to '>')
    val autoCompleteCharScores = mapOf(')' to 1, ']' to 2, '}' to 3, '>' to 4)
    val illegalCharScores = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)

    /**
     * Validate the input chunk and return the illegal character seen, if any.
     */
    fun validate(chunks: String): Char? {
        val stack = mutableListOf<Char>()
        for (nextChar in chunks) {
            when (nextChar) {
                in openingChars.keys -> stack.add(nextChar)
                in autoCompleteCharScores.keys -> {
                    val lastOpen = openingChars[stack.removeLast()]
                    if (lastOpen != nextChar) {
                        return nextChar
                    }
                }
            }
        }
        return null
    }

    /**
     * Get characters that complete the chunks.
     */
    fun autoComplete(chunks: String): List<Char> {
        val stack = mutableListOf<Char>()
        for (nextChar in chunks) {
            when (nextChar) {
                in openingChars.keys -> stack.add(nextChar)
                in autoCompleteCharScores.keys -> stack.removeLast()
            }
        }
        return stack.reversed().map { openingChars[it]!! }
    }

    fun part1(input: List<String>) = input.map { validate(it) }.mapNotNull { illegalCharScores[it] }.sum()

    fun part2(input: List<String>): Long {
        val scores = input.filter { validate(it) == null }.map { autoComplete(it) }
            .map { it.fold(0L) { score, next -> score * 5 + (autoCompleteCharScores[next] ?: 0) } }.sorted()
        return scores[scores.size / 2]
    }

    val testInput = readInput("${day}_test")
    check(part1(testInput) == 26397) { "Part 1" }
    check(part2(testInput) == 288957L) { "Part 2" }
    readOptionalInput(day)?.let { input ->
        println(part1(input)) // 318099
        println(part2(input)) // 2389738699
    }
}