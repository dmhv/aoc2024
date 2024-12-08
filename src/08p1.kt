private val REGEX_ANTENNA = Regex("""[a-zA-Z\d]""")

fun main() {
    val input = readInput("08")
    val (m, antennas) = parseInput((input))

    val maxRow = m.keys.maxOf { it.first }
    val maxCol = m.keys.maxOf { it.second }

    val antinodes = mutableSetOf<Pair<Int, Int>>()
    for (c in antennas.keys) {
        val ants = antennas[c] ?: emptyList()
        for (i in ants.indices) {
            for (j in i + 1 until ants.size) {
                val dRow = ants[i].first - ants[j].first
                val dCol = ants[i].second - ants[j].second
                val minusRow = ants[i].first + dRow
                val minusCol = ants[i].second + dCol
                val plusRow = ants[j].first - dRow
                val plusCol = ants[j].second - dCol

                if (minusRow in 0..maxRow && minusCol in 0..maxCol) {
                    antinodes.add(minusRow to minusCol)
                }
                if (plusRow in 0..maxRow && plusCol in 0..maxCol) {
                    antinodes.add(plusRow to plusCol)
                }
            }
        }
    }

    antinodes.size.println()
//    val filteredAntinodes = antinodes.filter { m[it] == '.' }
//    printMap(m, filteredAntinodes)
}

private fun parseInput(input: List<String>): Pair<MutableMap<Pair<Int, Int>, Char>, MutableMap<Char, MutableList<Pair<Int, Int>>>> {
    val m = mutableMapOf<Pair<Int, Int>, Char>()
    val antennas = mutableMapOf<Char, MutableList<Pair<Int, Int>>>()
    input.forEachIndexed { nr, row ->
        row.forEachIndexed { nc, c ->
            m[nr to nc] = c
            if (REGEX_ANTENNA.matches(c.toString())) {
                if (antennas.containsKey(c)) antennas[c]!!.add(nr to nc) else antennas[c] = mutableListOf(nr to nc)
            }
        }
    }
    return m to antennas
}

private fun printMap(m: Map<Pair<Int, Int>, Char>, antinodes: List<Pair<Int, Int>>) {
    for (row in 0..m.keys.maxOf { it.first }) {
        for (col in 0..m.keys.maxOf { it.second }) {
            if (antinodes.contains(row to col)) {
                print("#")
            } else {
                print(m[row to col])
            }
        }
        print("\n")
    }
}