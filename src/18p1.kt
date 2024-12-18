private val DIRECTIONS = listOf(Pair(-1, 0), Pair(0, -1), Pair(1, 0), Pair(0, 1))
private const val MAX_ROW = 70
private const val MAX_COL = 70
private val END = Cell(MAX_ROW, MAX_COL)
private const val NUM_BYTES = 1024

fun main() {
    val input = readInput("18")
    val byteLocations = parseInput(input)
    shortestPathLength(byteLocations, Cell(0, 0)).println()
}

private fun parseInput(input: List<String>): Set<Cell> {
    val byteLocations = mutableSetOf<Cell>()
    var cnt = 0
    for (line in input) {
        val (c, r) = line.split(",").map { it.toInt() }
        byteLocations.add(Cell(r, c))
        cnt++
        if (cnt == NUM_BYTES) break
    }
    return byteLocations
}

private fun shortestPathLength(byteLocations: Set<Cell>, head: Cell): Int {
    val queue = ArrayDeque(listOf(head to 0))
    val visited = mutableSetOf<Cell>()
    while (!queue.isEmpty()) {
        val (curr, steps) = queue.removeFirst()
        if (curr == END) return steps
        for (delta in DIRECTIONS) {
            val next = Cell(curr.row + delta.first, curr.col + delta.second)
            if (
                next.row in 0..MAX_ROW && next.col in 0..MAX_COL
                && !visited.contains(next) && !byteLocations.contains(next)
            ) {
                queue.add(next to steps + 1)
                visited.add(next)
            }
        }
    }
    return -1
}
