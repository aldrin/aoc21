import java.io.File

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("data", "$name.txt").readLines()

/**
 * Read lines from an optional input file
 */
fun readOptionalInput(name: String) = if (File("data", "$name.txt").exists()) readInput(name) else null
