import java.lang.IllegalStateException
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayDeque
import kotlin.math.absoluteValue

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

    val backTrack = mutableMapOf<Triple<Loc, Direction, Int>, MutableSet<Triple<Loc, Direction, Int>>>()

    val visited = mutableSetOf<Pair<Loc, Direction>>()
    val reachedEnd = mutableListOf<Triple<Loc, Direction, Int>>()
    while (queue.isNotEmpty()) {
        val (curr, dir, steps) = queue.remove()
        //println("At $curr, going $dir, done $steps steps")
        visited.add(curr to dir)
        if (curr == endLoc) {
            reachedEnd.add(Triple(curr, dir, steps))
        }

        val neighbors = getNeighbors(curr, map)
        for (neighbor in neighbors) {
            val nextDirection = computeDirection(neighbor, curr)
            if (!visited.contains(neighbor to nextDirection)) {
                if (nextDirection == dir) {
                    queue.add(Triple(neighbor, nextDirection, steps + 1))
                    if (!backTrack.contains(Triple(neighbor, nextDirection, steps + 1))) {
                        backTrack[Triple(neighbor, nextDirection, steps + 1)] =
                            mutableSetOf(Triple(curr, dir, steps))
                    } else {
                        backTrack[Triple(neighbor, nextDirection, steps + 1)]!!.add(Triple(curr, dir, steps))
                    }
                } else {
                    queue.add(Triple(neighbor, nextDirection, steps + 1001)) // rotate AND move
                    if (!backTrack.contains(Triple(neighbor, nextDirection, steps + 1001))) {
                        backTrack[Triple(neighbor, nextDirection, steps + 1001)] =
                            mutableSetOf(Triple(curr, dir, steps))
                    } else {
                        backTrack[Triple(neighbor, nextDirection, steps + 1001)]!!.add(Triple(curr, dir, steps))
                    }
                }
                // println(" .. considering going $nextDirection")
            }
        }
    }
    val minSteps = reachedEnd.minOf { it.third }
    val optimalEnds = reachedEnd.filter { it.third == minSteps }
    println("Got ${optimalEnds.size} different optimal paths")

    val onAnyOptimalPath = mutableSetOf<Loc>()
    val toProcess = ArrayDeque<Triple<Loc, Direction, Int>>()

    for (end in optimalEnds) {
        toProcess.add(end)
    }
    while (toProcess.isNotEmpty()) {
        val curr = toProcess.removeLast()
        onAnyOptimalPath.add(curr.first)

        if (!backTrack.containsKey(curr)) {
            continue
        }
        val beforeCurr = backTrack[curr]!!
        for (b in beforeCurr) {
            toProcess.addLast(b)
        }
    }

    onAnyOptimalPath.add(startLoc)
    onAnyOptimalPath.add(endLoc)
    println("Found ${onAnyOptimalPath.size} tiles at any of the optimal paths")

//    val maxRow = map.keys.maxOf { it.r }
//    val maxCol = map.keys.maxOf { it.r }
//    for (r in 0..maxRow) {
//        var out = ""
//        for (c in 0..maxCol) {
//            if (onAnyOptimalPath.contains(Loc(r, c))) {
//                out += "O"
//            } else {
//                out += map[Loc(r, c)]!!
//            }
//        }
//        println(out)
//    }
}

private fun computeDirection(self: Loc, other: Loc): Direction {
    val deltaRow = self.r - other.r
    val deltaCol = self.c - other.c
    check(
        (deltaRow.absoluteValue == 1 && deltaCol.absoluteValue == 0)
                || (deltaRow.absoluteValue == 0 && deltaCol.absoluteValue == 1)
    )
    if (deltaRow == -1) return Direction.NORTH
    if (deltaRow == 1) return Direction.SOUTH
    if (deltaCol == -1) return Direction.WEST
    if (deltaCol == 1) return Direction.EAST
    throw IllegalStateException("Dunno where we going mate, $deltaRow, $deltaCol")
}

private fun getNeighbors(cell: Loc, map: Map<Loc, Char>): List<Loc> {
    val neighbors = mutableListOf<Loc>()
    for (delta in listOf(-1 to 0, 0 to 1, 1 to 0, 0 to -1)) {
        val newLoc = cell + delta
        if (map[newLoc] in listOf('.', 'E')) neighbors.add(newLoc)
    }
    return neighbors
}

