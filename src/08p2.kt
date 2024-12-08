private val REGEX_ANTENNA = Regex("""[a-zA-Z\d]""")

fun main() {
    val input = readInput("08")
    val (m, antennas) = parseInput((input))

    val maxRow = m.keys.maxOf { it.first }
    val maxCol = m.keys.maxOf { it.second }

    val antinodes = mutableSetOf<Pair<Int, Int>>()
    antennas.values.forEach { ants ->
        ants.indices.forEach { i ->
            (i + 1 until ants.size).forEach { j ->
                val (r1, c1) = ants[i]
                val (r2, c2) = ants[j]
                val dRow = r2 - r1
                val dCol = c2 - c1

                generateSequence(r1 - dRow to c1 - dCol) { (row, col) -> row - dRow to col - dCol }
                    .takeWhile { (row, col) -> row in 0..maxRow && col in 0..maxCol }
                    .forEach(antinodes::add)

                generateSequence(r2 + dRow to c2 + dCol) { (row, col) -> row + dRow to col + dCol }
                    .takeWhile { (row, col) -> row in 0..maxRow && col in 0..maxCol }
                    .forEach(antinodes::add)
            }
        }
    }

    val filteredAntinodes = antinodes.filter { m[it] == '.' }
    (filteredAntinodes.size + antennas.values.sumOf { it.size }).println()
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
