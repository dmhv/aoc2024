import java.lang.IllegalStateException
import java.util.*
import kotlin.Comparator

fun main() {
    val input = readInput("16")
    val map = buildMap {
        input.forEachIndexed { r, line ->
            line.forEachIndexed { c, char ->
                this[Loc(r, c)] = char
            }
        }
    }

    val startLoc = map.filter { it.value == 'S' }.keys.first()
    val endLoc = map.filter { it.value == 'E' }.keys.first()

    val compareBySteps: Comparator<Triple<Loc, Direction, Int>> = compareBy { it.third }
    val queue = PriorityQueue(compareBySteps)
    queue.add(Triple(startLoc, Direction.EAST, 0))

    val visited = mutableSetOf<Pair<Loc, Direction>>()
    val reachedEnd = mutableListOf<Triple<Loc, Direction, Int>>()
    while (queue.isNotEmpty()) {
        val (curr, dir, steps) = queue.remove()
        //println("At $curr, going $dir, done $steps steps")
        visited.add(curr to dir)
        if (curr == endLoc) {
            reachedEnd.add(Triple(curr, dir, steps))
        }

        for (neighbor in getNeighbors(curr, map)) {
            val nextDirection = computeDirection(neighbor, curr)
            if (!visited.contains(neighbor to nextDirection)) {
                if (nextDirection == dir) {
                    queue.add(Triple(neighbor, nextDirection, steps + 1))
                } else {
                    queue.add(Triple(neighbor, nextDirection, steps + 1001)) // rotate AND move
                }
                // println(" .. considering going $nextDirection")
            }
        }
    }
    reachedEnd.minBy { it.third }.println()
}

private fun computeDirection(self: Loc, other: Loc): Direction {
    val deltaRow = self.r - other.r
    val deltaCol = self.c - other.c
    return when (deltaRow to deltaCol) {
        -1 to 0 -> Direction.NORTH
        1 to 0 -> Direction.SOUTH
        0 to -1 -> Direction.WEST
        0 to 1 -> Direction.EAST
        else -> throw IllegalStateException("Dunno where we going mate, $deltaRow, $deltaCol")
        }
}

private fun getNeighbors(cell: Loc, map: Map<Loc, Char>): List<Loc> {
    val neighbors = mutableListOf<Loc>()
    for (delta in listOf(-1 to 0, 0 to 1, 1 to 0, 0 to -1)) {
        val newLoc = cell + delta
        if (map[newLoc] in listOf('.', 'E')) neighbors.add(newLoc)
    }
    return neighbors
}

