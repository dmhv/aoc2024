private val XMAS = Regex("XMAS")
private val SAMX = Regex("SAMX")

private fun findXmas(s: String) = XMAS.findAll(s).toList().size + SAMX.findAll(s).toList().size

fun main() {
    val input = readInput("04tiny")
    val ls = buildMap {
        input.forEachIndexed { i, line ->
            line.toCharArray().forEachIndexed { j, c ->
                this[Pair(i, j)] = c
            }
        }
    }

    val nRow = ls.keys.maxOf { it.first }
    val nCol = ls.keys.maxOf { it.second }
    var count = 0

    // horizontal
    count += input.sumOf { findXmas(it) }

    // vertical
    for (j in 0..nCol) {
        val line = ls.filter { it.key.second == j }.values.joinToString("")
        count += findXmas(line)
    }

    // diagonal top-left to bottom-right
    for (k in 0..(nRow + nCol)) {
        val line = ls.filter { it.key.first + it.key.second == k }.values.joinToString("")
        count += findXmas(line)
    }

    // diagonal top-right to bottom-left
    for (k in 0..(nRow + nCol)) {
        val line = ls.filter { it.key.first + (nCol - it.key.second) == k }.values.joinToString("")
        count += findXmas(line)
    }

    count.println()
}
