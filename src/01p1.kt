import kotlin.math.abs

fun main() {
    val input = readInput("01")

    val lefts = mutableListOf<Int>()
    val rights = mutableListOf<Int>()
    for (line in input) {
        val pair = line.split("\\s+".toRegex()).map { it.toInt() }
        lefts.add(pair.first())
        rights.add(pair.last())
    }

    lefts.sorted()
        .zip(rights.sorted())
        .sumOf { (left, right) -> abs(left - right) }
        .println()
}
