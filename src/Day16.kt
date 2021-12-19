fun main() {
    val day = "Day16"

    /**
     * Packet header.
     */
    data class Header(val version: Long, val type: Long)

    /**
     * Packet, base type.
     */
    open class Packet(open val header: Header)

    /**
     * A literal packet holds a single value.
     */
    data class LiteralPacket(override val header: Header, val value: Long) : Packet(header)

    /**
     * An operator packet is nested structure that contains other packets (that can be operator packets themselves).
     */
    data class OperatorPacket(override val header: Header, val subPackets: List<Packet>) : Packet(header)

    /**
     * BITS Transmission.
     */
    class Transmission(input: String) {
        // Convert to bit sequence.
        val bits = input.map { it.digitToInt(16).toString(2).padStart(4, '0') }.joinToString("")

        // Maintain a read pointer to track how far we've read
        var pointer = 0

        // Read next n bits from the bit stream and advance the pointer accordingly.
        fun read(nBits: Int): String {
            val b = bits.substring(pointer, pointer + nBits)
            pointer += nBits
            return b
        }

        // (Shorthand) Read next n bits as a number
        fun readNumber(n: Int) = read(n).toLong(2)

        // Decode the transmission and extract the packet.
        fun decode(): Packet {
            val header = Header(readNumber(3), readNumber(3))
            return when (header.type) {
                4L -> decodeLiteral(header)
                else -> decodeOperator(header)
            }
        }

        // Decode a literal packet.
        fun decodeLiteral(header: Header): Packet {
            var literal = ""
            while (read(1) == "1") {
                literal += read(4)
            }
            literal += read(4)
            return LiteralPacket(header, literal.toLong(2))
        }

        // Decode an operator packet (this is recursive)
        fun decodeOperator(header: Header): Packet {
            val subPackets = mutableListOf<Packet>()
            when (read(1)) {
                "0" -> {
                    val length = readNumber(15)
                    val until = pointer + length
                    while (pointer < until) {
                        subPackets.add(decode())
                    }
                }
                else -> {
                    val count = readNumber(11)
                    while (subPackets.size < count) {
                        subPackets.add(decode())
                    }
                }
            }
            return OperatorPacket(header, subPackets)
        }
    }

    /**
     * Part 1 requires a sum of all the versions.
     */
    fun Packet.versionSum(): Long {
        return when (this) {
            is OperatorPacket -> header.version + subPackets.sumOf { it.versionSum() }
            else -> header.version
        }
    }

    /**
     * Part 2 requires an evaluation of the packet.
     */
    fun Packet.evaluate(): Long {
        return when (this) {
            is LiteralPacket -> value
            is OperatorPacket ->
                when (header.type) {
                    0L -> subPackets.sumOf { it.evaluate() }
                    1L -> subPackets.fold(1L) { v, p -> v * p.evaluate() }
                    2L -> subPackets.minOf { it.evaluate() }
                    3L -> subPackets.maxOf { it.evaluate() }
                    5L -> if (subPackets[0].evaluate() > subPackets[1].evaluate()) 1 else 0
                    6L -> if (subPackets[0].evaluate() < subPackets[1].evaluate()) 1 else 0
                    7L -> if (subPackets[0].evaluate() == subPackets[1].evaluate()) 1 else 0
                    else -> throw IllegalStateException("unknown header type ${header.type}")
                }
            else -> throw IllegalStateException("unknown packet type")
        }
    }

    fun part1(input: List<String>) = Transmission(input.first()).decode().versionSum()
    fun part2(input: List<String>) = Transmission(input.first()).decode().evaluate()

    check(part1(listOf("8A004A801A8002F478")) == 16L) { "Part 1.1" }
    check(part1(listOf("620080001611562C8802118E34")) == 12L) { "Part 1.2" }
    check(part1(listOf("C0015000016115A2E0802F182340")) == 23L) { "Part 1.3" }
    check(part1(listOf("A0016C880162017C3686B18A3D4780")) == 31L) { "Part 1.4" }
    check(part2(listOf("C200B40A82")) == 3L) { "Part 2.1" }
    check(part2(listOf("04005AC33890")) == 54L) { "Part 2.2" }
    check(part2(listOf("D8005AC2A8F0")) == 1L) { "Part 2.3" }
    check(part2(listOf("9C0141080250320F1802104A08")) == 1L) { "Part 2.4" }

    readOptionalInput(day)?.let { input ->
        println(part1(input)) // 974
        println(part2(input)) // 180616437720
    }
}