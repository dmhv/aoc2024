fun main() {
    val input = readInput("10")
    val (maze, trailStarts) = parseInput(input)
    trailStarts.sumOf { bfs(maze, it) }.println()
}

private fun parseInput(input: List<String>): Pair<Map<Cell, Int>, List<Cell>> {
    val maze = mutableMapOf<Cell, Int>()
    val trailStarts = mutableListOf<Cell>()
    for (i in input.indices) {
        val line = input[i].toCharArray()
        for (j in input[i].indices) {
            maze[Cell(i, j)] = line[j].digitToInt()
            if (line[j].digitToInt() == 0) trailStarts.add(Cell(i, j))
        }
    }
    return Pair(maze, trailStarts)
}

private val DIRECTIONS = listOf(Pair(-1, 0), Pair(0, -1), Pair(1, 0), Pair(0, 1))

private fun bfs(maze: Map<Cell, Int>, head: Cell): Int {
    val queue = ArrayDeque(listOf(head))
    val maxRow = maze.maxOf { it.key.row }
    val maxCol = maze.maxOf { it.key.col }

    val visited = mutableSetOf<Cell>()
    val trailEnds = mutableSetOf<Cell>() // all trail ends reachable from this trail start
    while (!queue.isEmpty()) {
        val curr = queue.removeFirst()
        for (delta in DIRECTIONS) {
            val next = Cell(curr.row + delta.first, curr.col + delta.second)
            if (next.row in 0..maxRow && next.col in 0..maxCol && maze[next]!! - maze[curr]!! == 1) {
                if (maze[next]!! == 9 && !trailEnds.contains(next)) {
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
