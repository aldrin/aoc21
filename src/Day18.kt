/**
 * A snail fish number.
 */
sealed interface Number

/**
 * A regular snail fish number is just an integer.
 */
class RegularNumber(var value: Int) : Number

/**
 * Should this regular number split?
 */
fun RegularNumber.shouldSplit() = value > 9

/**
 * A pair snail fish number is a pair of two snail fish numbers.
 */
class PairNumber(val left: Number, val right: Number) : Number

/**
 * Is this pair a leaf-pair?
 */
fun PairNumber.isLeaf() = (right is RegularNumber) and (left is RegularNumber)

/**
 * Should this pair number be explode?
 */
fun PairNumber.shouldExplode(level: Int) = level > 4 && isLeaf()

/**
 * In order traversal of a number. We need this to compute the left-nearest and the right-nearest regular numbers
 * when a pair is exploded.
 */
fun Number.inorder(): List<Number> {
    return when (this) {
        is RegularNumber -> listOf(this)
        is PairNumber -> left.inorder().plus(this).plus(right.inorder())
    }
}

/**
 * We look for regular numbers in a given range.
 */
fun List<Number>.regulars(fromIndex: Int, toIndex: Int) = subList(fromIndex, toIndex).filterIsInstance<RegularNumber>()

/**
 * Check if the number has a pair that can be exploded?
 */
fun Number.canExplode(level: Int): Boolean {
    return when (this) {
        is RegularNumber -> false
        is PairNumber -> shouldExplode(level) || left.canExplode(level.inc()) || right.canExplode(level.inc())
    }
}

/**
 * Check if the number has regular numbers that can be split.
 */
fun Number.canSplit(): Boolean {
    return when (this) {
        is RegularNumber -> shouldSplit()
        is PairNumber -> left.canSplit() || right.canSplit()
    }
}

/**
 * Compute the magnitude of the number.
 */
fun Number.magnitude(): Int {
    return when (this) {
        is RegularNumber -> value
        is PairNumber -> (3 * left.magnitude()) + (2 * right.magnitude())
    }
}

/**
 * Explode pair that can explode.
 *
 * @param level The level of nesting we're currently at
 * @param inorder The inorder traversal of the root of the number.
 * @param token Ensures we only do one explosion at a time.
 */
fun Number.explode(level: Int, inorder: List<Number>, token: MutableList<Int>): Number {
    if (token.isEmpty()) return this // We only take one action in a step, if that has been taken, don't do anything.
    return when (this) {
        is RegularNumber -> this
        is PairNumber -> {
            if (level > 4 && left is RegularNumber && right is RegularNumber) {
                token.removeLast() // We've found our action - close the door for others in this step.

                // The left value is added to the closest left regular number.
                val leftIndex = inorder.indexOf(left)
                inorder.regulars(0, leftIndex).lastOrNull()?.let { it.value += left.value }

                // The right value is added to the closes right regular number.
                val rightIndex = inorder.indexOf(right)
                inorder.regulars(rightIndex + 1, inorder.size).firstOrNull()?.let { it.value += right.value }

                // Exploded numbers are replaced by 0
                return RegularNumber(0)
            }

            // We are in a non-leaf node - increase the level and descend into the next level.
            return PairNumber(left.explode(level.inc(), inorder, token), right.explode(level.inc(), inorder, token))
        }
    }
}

/**
 * Split a regular number that is greater than 9.
 */
fun Number.split(token: MutableList<Int>): Number {
    if (token.isEmpty()) return this // Not this time.
    return when (this) {
        // Pair number should pass on the split to their left and right subtrees in order.
        is PairNumber -> PairNumber(left.split(token), right.split(token))
        is RegularNumber -> if (!shouldSplit()) this else { // Regular numbers, split to produce pairs
            token.removeLast()
            val half = value / 2.0
            val left = RegularNumber(kotlin.math.floor(half).toInt())
            val right = RegularNumber(kotlin.math.ceil(half).toInt())
            PairNumber(left, right)
        }
    }
}

/**
 * Add two numbers to produce the sum. The input numbers are modified and should not be reused.
 */
fun add(a: Number, b: Number): Number {

    // Add the numbers
    var sum: Number = PairNumber(a, b)

    // Check if the sum needs to be reduced.
    var canSplit = sum.canSplit()
    var canExplode = sum.canExplode(1)

    // While it can be reduced, keep reducing it.
    while (canSplit || canExplode) {
        // Follow the steps, i.e. explode first, if we can.
        sum = if (canExplode) {
            sum.explode(1, sum.inorder(), mutableListOf(1))
        } else {
            sum.split(mutableListOf(1))
        }
        // Re-evaluate the next step
        canExplode = sum.canExplode(1)
        canSplit = sum.canSplit()
    }

    // Return the final sum
    return sum
}

/**
 * Read a number from the input string.
 */
fun readNumber(input: String): Number {
    val stack = mutableListOf<Number>()
    for (c in input) {
        when (c) {
            '[', ',' -> continue
            ']' -> {
                val right = stack.removeLast()
                val left = stack.removeLast()
                stack.add(PairNumber(left, right))
            }
            else -> stack.add(RegularNumber(c.digitToInt()))
        }
    }
    return stack.removeLast()
}

/**
 * Part 1 is simply a reduce operation.
 */
fun part1(input: List<String>) = input.map { readNumber(it) }.reduce { a, b -> add(a, b) }.magnitude()

/**
 * Part 2 is a pairwise comparison.
 */
fun part2(input: List<String>): Int {
    var maxSum = 0
    for (i in input.indices) {
        for (j in input.indices) {
            if (i == j) continue
            val sum = add(readNumber(input[i]), readNumber(input[j]))
            sum.magnitude().takeIf { it > maxSum }?.let { maxSum = it }
        }
    }
    return maxSum
}

fun main() {
    val day = "Day18"
    val testInput = readInput("${day}_test")
    check(part1(testInput) == 4140) { "Part 1" }
    check(part2(testInput) == 3993) { "Part 2" }
    readOptionalInput(day)?.let { input ->
        println(part1(input)) // 3699
        println(part2(input)) // 4735
    }
}

