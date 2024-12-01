import kotlin.math.abs

fun main() {
    val input = readInput("01")

    val (lefts, rights) = input.map { line ->
        val left = line.substringBefore(" ").toInt()
        val right = line.substringAfterLast(" ").toInt()
        left to right
    }.unzip()

    lefts.sorted()
        .zip(rights.sorted())
        .sumOf { (left, right) -> abs(left - right) }
        .println()
}
