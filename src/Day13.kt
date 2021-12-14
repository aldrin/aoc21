fun main() {
    val day = "Day13"

    /**
     * A single fold.
     */
    data class Fold(val along: Char, val at: Int)

    /**
     * A point.
     */
    data class Point(val x: Int, val y: Int) {
        fun reflection(fold: Fold): Point? {
            return when (fold.along) {
                'x' -> Point(fold.at - (x - fold.at), y).takeIf { x > fold.at }
                else -> Point(x, fold.at - (y - fold.at)).takeIf { y > fold.at }
            }
        }
    }

    /**
     * Read the input into a set of points and folds.
     */
    fun read(input: List<String>): Pair<Set<Point>, List<Fold>> {
        var readingPoints = true
        val folds = mutableListOf<Fold>()
        val points = mutableSetOf<Point>()
        for (line in input) {
            if (line.isEmpty()) {
                readingPoints = false
                continue
            }
            if (readingPoints) {
                readNumbers(line).zipWithNext().first().let { (x, y) -> points.add(Point(x, y)) }
            } else {
                line.splitToSequence(" ", "=").drop(2).zipWithNext().first()
                    .let { (on, at) -> folds.add(Fold(on[0], at.toInt())) }
            }
        }
        return Pair(points, folds)
    }

    /**
     * The solver. The folds are actually a fold operation.
     */
    fun fold(startingPoints: Set<Point>, folds: List<Fold>): Set<Point> {
        return folds.fold(startingPoints) { points, fold -> points.map { it.reflection(fold) ?: it }.toSet() }
    }

    /**
     * Fold once and return the number of points.
     */
    fun part1(input: List<String>): Int {
        return read(input).let { (points, folds) -> fold(points, folds.take(1)).size }
    }

    /**
     * Same as part 1, except we don't stop after 1 fold.
     */
    fun part2(input: List<String>) {
        val folded = read(input).let { (points, folds) -> fold(points, folds) }

        // Print it out for the visual read of the answer
        // for my input it was - FJAHJGAH
        for (y in (0..folded.maxOf { it.y })) {
            for (x in 0..folded.maxOf { it.x }) {
                if (Point(x, y) in folded) {
                    print('#')
                } else {
                    print(' ')
                }
            }
            println()
        }
    }

    val testInput = readInput("${day}_test")
    check(part1(testInput) == 17) { "Part 1" }
    readOptionalInput(day)?.let { input ->
        println(part1(input))
        part2(input)
    }
}