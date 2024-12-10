fun main() {
    val input = readInput("10")
    val (map, heads) = parseInput(input)
    heads.sumOf { bfs(map, it) }.println() // 1242
}

private fun parseInput(input: List<String>): Pair<Map<Cell, Int>, List<Cell>> {
    val map = mutableMapOf<Cell, Int>()
    val heads = mutableListOf<Cell>()
    for (i in input.indices) {
        val line = input[i].toCharArray()
        for (j in input[i].indices) {
            map[Cell(i, j)] = line[j].digitToInt()
            if (line[j].digitToInt() == 0) heads.add(Cell(i, j))
        }
    }
    return Pair(map, heads)
}

private val DIRECTIONS = listOf(Pair(-1, 0), Pair(0, -1), Pair(1, 0), Pair(0, 1))

private fun bfs(map: Map<Cell, Int>, head: Cell): Int {
    val queue = ArrayDeque(listOf(head))
    val maxRow = map.maxOf { it.key.row }
    val maxCol = map.maxOf { it.key.col }

    val pathCounts = mutableMapOf<Cell, Int>()
    while (!queue.isEmpty()) {
        val curr = queue.removeFirst()
        for (delta in DIRECTIONS) {
            val next = Cell(curr.row + delta.first, curr.col + delta.second)
            if (next.row in 0..maxRow && next.col in 0..maxCol && map[next]!! - map[curr]!! == 1) {
                if (map[next]!! == 9) {
                    if (!pathCounts.containsKey(next)) {
                        pathCounts[next] = 1
                    } else {
                        pathCounts[next] = pathCounts[next]!! + 1
                    }
                } else {
                    queue.add(next)
                }
            }
        }
    }
    return pathCounts.values.sumOf { it }
}
