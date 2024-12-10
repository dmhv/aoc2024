fun main() {
    val input = readInput("10")
    val (map, heads) = parseInput(input)
    heads.sumOf { bfs(map, it) }.println()
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

private fun bfs(map: Map<Cell, Int>, head: Cell): Int {
    val queue = ArrayDeque<Cell>()
    queue.add(head)
    val maxRow = map.maxOf { it.key.row }
    val maxCol = map.maxOf { it.key.col }

    val visited = mutableSetOf<Cell>()
    val trailEnds = mutableSetOf<Cell>()
    while (!queue.isEmpty()) {
        val curr = queue.removeFirst()
        for (delta in listOf(Pair(-1, 0), Pair(0, -1), Pair(1, 0), Pair(0, 1))) {
            val next = Cell(curr.row + delta.first, curr.col + delta.second)
            if (next.row in 0..maxRow && next.col in 0..maxCol && map[next]!! - map[curr]!! == 1) {
                if (map[next]!! == 9 && !trailEnds.contains(next)) {
                    trailEnds.add(next)
                } else {
                    if (!visited.contains(next)) {
                        queue.add(next)
                        visited.add(next)
                    }
                }
            }
        }
    }
    return trailEnds.size
}

data class Cell(val row: Int, val col: Int)
