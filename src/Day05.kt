fun main() {
    /**
     * We could use Pair here, but "x" and "y" read better than "first" and "second".
     */
    data class Point(val x: Int, val y: Int)

    /**
     * A Hydrothermal Vent.
     */
    class HydrothermalVent(input: String) {
        /**
         * A 2 element list that represents a line. The "first" point on the line is the one with smaller x-coordinate.
         * This convention helps simplify the diagonal point enumeration.
         */
        val points = input.splitToSequence(" ", "-", ",", ">").filter { it.isNotBlank() }.map { it.trim().toInt() }
            .windowed(2, 2).map { Point(it[0], it[1]) }.sortedBy { it.x }.toList()

        /**
         * Enumerate points on this line. This can be "simplified" to fewer lines, but that'd hurt readability.
         */
        fun enumeratePoints(ignoreDiagonal: Boolean = false): Set<Point> {
            val vertical = (points[0].x == points[1].x)
            val horizontal = (points[0].y == points[1].y)

            if (vertical) {
                val x = points[0].x
                val y1 = points[0].y
                val y2 = points[1].y
                val ySteps = if (y1 > y2) (y1 downTo y2) else (y1..y2)
                return ySteps.map { Point(x, it) }.toSet()
            }

            if (horizontal) {
                val y = points[0].y
                val x1 = points[0].x
                val x2 = points[1].x
                val xSteps = if (x1 > x2) (x1 downTo x2) else (x1..x2)
                return xSteps.map { Point(it, y) }.toSet()
            }

            if (!ignoreDiagonal) { // For part 1 we ignore diagonal lines
                val xSteps = (points[0].x..points[1].x)
                val ySteps = if (points[0].y > points[1].y) {
                    (points[0].y downTo points[1].y)
                } else {
                    (points[0].y..points[1].y)
                }
                return xSteps.zip(ySteps).map { Point(it.first, it.second) }.toSet()
            }

            return emptySet()
        }
    }

    /**
     * Count how many points are on more than 1 lines.
     */
    fun overlappingPoints(ventPoints: List<Set<Point>>): Int {
        val thermalPoints = mutableMapOf<Point, Int>()
        ventPoints.forEach { points ->
            points.forEach { point ->
                thermalPoints.compute(point) { _, count -> (count ?: 0) + 1 }
            }
        }
        return thermalPoints.values.count { it > 1 }
    }

    fun part1(input: List<String>): Int {
        return overlappingPoints(input.map { HydrothermalVent(it).enumeratePoints(true) })
    }

    fun part2(input: List<String>): Int {
        return overlappingPoints(input.map { HydrothermalVent(it).enumeratePoints() })
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == 5) { "part 1" }
    check(part2(testInput) == 12) { "part 2" }
    readOptionalInput("Day05")?.let { input ->
        println(part1(input)) // 5774
        println(part2(input)) // 18423
    }
}
