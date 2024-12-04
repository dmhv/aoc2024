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

    ls.filter {
        it.value == 'A'
                && it.key.first > 0 && it.key.first < nRow
                && it.key.second > 0 && it.key.second < nCol
    }.map { (key, _) ->
        val (i, j) = key
        val d1 = "${ls[Pair(i - 1, j - 1)]}${ls[Pair(i + 1, j + 1)]}"
        val d2 = "${ls[Pair(i - 1, j + 1)]}${ls[Pair(i + 1, j - 1)]}"
        if ((d1 in listOf("MS", "SM")) && (d2 in listOf("MS", "SM"))) 1 else 0
    }.sum().println()
}
