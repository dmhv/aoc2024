private val XMAS = Regex("XMAS")
private val SAMX = Regex("SAMX")

fun main() {
    val input = readInput("04")

    val ls = mutableMapOf<Pair<Int, Int>, Char>()

    input.forEachIndexed { i, line ->
        line.toCharArray().forEachIndexed { j, c ->
            ls[Pair(i, j)] = c
        }
    }

    val nRow = ls.keys.maxOf { it.first }
    val nCol = ls.keys.maxOf { it.second }

    var count = 0
    // horizontal
    input.forEachIndexed { i, line ->
        val cntFound = XMAS.findAll(line).toList().size + SAMX.findAll(line).toList().size
//        println("$line -> $cntFound")
        count += cntFound
    }

    // vertical
    for (j in 0..nCol) {
        val line = ls.filter { it.key.second == j }.values.joinToString("") { it.toString() }
        val cntFound = XMAS.findAll(line).toList().size + SAMX.findAll(line).toList().size
        count += cntFound
    }

    // diagonal top-left to bottom-right
    for (k in 0..(nRow + nCol)) {
        val line = ls.filter { it.key.first + it.key.second == k }.values.joinToString("") { it.toString() }
        val cntFound = XMAS.findAll(line).toList().size + SAMX.findAll(line).toList().size
//        println("$line -> $cntFound")
        count += cntFound
    }

    // diagonal top-right to bottom-left
    for (k in 0..(nRow + nCol)) {
        val line = ls.filter { it.key.first + (nCol - it.key.second) == k }.values.joinToString("") { it.toString() }
        val cntFound = XMAS.findAll(line).toList().size + SAMX.findAll(line).toList().size
        count += cntFound
    }
    count.println()
}
