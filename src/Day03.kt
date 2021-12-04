fun main() {
    /**
     * Process readings and return a list of booleans: true if the most common bit in position is 1, false otherwise.
     */
    fun moreOnesThanZeros(readings: List<String>): List<Boolean> {
        val positions = readings.first().length
        val ones = MutableList(positions) { 0 }
        val zeros = MutableList(positions) { 0 }

        for (reading in readings) {
            var position = 0
            for (bit in reading) {
                when (bit) {
                    '0' -> zeros[position]++
                    '1' -> ones[position]++
                }
                position++
            }
        }
        return ones.zip(zeros).map { it.first >= it.second }
    }

    /**
     * Pick some readings and drop those we don't need.
     */
    fun selectedReadings(readings: List<String>, picks: Set<Int>): List<String> {
        return readings.filterIndexed { i, _ -> picks.contains(i) }.toList()
    }

    /**
     * Calculate the life-support rating. Oxygen rating favors readings with majority bits in position, Carbon dioxide
     * rating favors readings with minority bits in position. It is a bit complicated - per the question itself.
     */
    fun lifeSupportRating(readings: List<String>, favorMajority: Boolean): Int {
        val positions = readings.first().length
        val candidates = readings.indices.toMutableSet()
        for (position in 0 until positions) {
            val moreOnesIn = moreOnesThanZeros(selectedReadings(readings, candidates))
            if (moreOnesIn[position]) {
                if (favorMajority) {
                    candidates.removeIf { readings[it][position] == '0' }
                } else {
                    candidates.removeIf { readings[it][position] == '1' }
                }
            } else {
                if (favorMajority) {
                    candidates.removeIf { readings[it][position] == '1' }
                } else {
                    candidates.removeIf { readings[it][position] == '0' }
                }
            }
            if (candidates.size == 1) {
                break
            }
        }
        return readings[candidates.first()].toInt(2)
    }

    fun part1(input: List<String>): Int {
        var gamma = 0
        var epsilon = 0
        moreOnesThanZeros(input).reversed().forEachIndexed { position, one ->
            val powerOf2 = 1 shl position
            if (one) {
                gamma += powerOf2
            } else {
                epsilon += powerOf2
            }
        }
        return gamma * epsilon
    }

    fun part2(readings: List<String>): Int {
        return lifeSupportRating(readings, true) * lifeSupportRating(readings, false)
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198) { "part1" }
    check(part2(testInput) == 230) { "part2" }

    readOptionalInput("Day03")?.let { input ->
        println(part1(input)) // 3959450
        println(part2(input)) // 7440311
    }
}
