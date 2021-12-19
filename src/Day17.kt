import java.lang.Integer.max
import kotlin.math.absoluteValue

fun main() {
    val day = "Day17"

    /**
     * Position of the probe.
     */
    data class Position(val x: Int, val y: Int)

    /**
     * Velocity of the probe.
     */
    data class Velocity(val x: Int, val y: Int)

    /**
     * The target.
     */
    data class Target(val xRange: IntRange, val yRange: IntRange)

    /**
     * Read the target from the input.
     */
    fun readTarget(input: List<String>): Target {
        val splits = input.first().split("=", ",").map { it.trim() }
        val xRange = splits[1].split("..").map { it.toInt() }.zipWithNext().map { it.first..it.second }
        val yRange = splits[3].split("..").map { it.toInt() }.zipWithNext().map { it.first..it.second }
        return Target(xRange.first(), yRange.first())
    }

    /**
     * Check if the position hits the target
     */
    fun Position.hit(target: Target) = x in target.xRange && y in target.yRange

    /**
     * Step forward with the given velocity.
     */
    fun Position.step(velocity: Velocity) = Position(x + velocity.x, y + velocity.y)

    /**
     * Adjust velocity after taking a step.
     */
    fun Velocity.step() = Velocity(x = if (x == 0) 0 else if (x > 0) x - 1 else x + 1, y = y - 1)

    /**
     * Test if the position is hopeless, i.e. has no hope of hitting the target now.
     */
    fun Position.isHopeless(target: Target, velocity: Velocity) = velocity.y < 0 && y < target.yRange.first

    /**
     * Find maximum heights reached for initial velocities.
     */
    fun maxHeight(target: Target): Map<Velocity, Int> {
        val heights = mutableMapOf<Velocity, Int>()

        // Not sure if there's some projectile motion "closed form" that helps here, but given my limited Physics
        // we're going to do a brute force search on all likely values for the velocities.
        for (xVelocity in 0..target.xRange.last) {
            for (yVelocity in target.yRange.first..target.yRange.first.absoluteValue) {
                val initialVelocity = Velocity(xVelocity, yVelocity)

                var maxHeight = 0
                var position = Position(0, 0)
                var velocity = initialVelocity

                // Keep going till we hit the target or reach a hopeless position
                while (!position.hit(target) && !position.isHopeless(target, velocity)) {
                    maxHeight = max(position.y, maxHeight)
                    position = position.step(velocity)
                    velocity = velocity.step()
                }

                // If we did hit the target, record the max height reached
                if (position.hit(target)) {
                    heights[initialVelocity] = maxHeight
                }
            }
        }

        return heights
    }

    fun part1(input: List<String>) = maxHeight(readTarget(input)).maxOf { it.value }
    fun part2(input: List<String>) = maxHeight(readTarget(input)).size
    val testInput = readInput("${day}_test")
    check(part1(testInput) == 45) { "Part 1" }
    check(part2(testInput) == 112) { "Part 2" }
    readOptionalInput(day)?.let { input ->
        println(part1(input)) // 23005
        println(part2(input)) // 2040
    }
}