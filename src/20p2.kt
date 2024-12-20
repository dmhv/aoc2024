import java.util.*
import kotlin.Comparator
import kotlin.math.absoluteValue

private const val MAX_CHEAT = 20
private const val MIN_SAVED = 100

fun main() {
    val input = readInput("20")
    val (map, startLoc, endLoc) = parseInput(input)

    val (stepsNoCheat, cameFromStart) = shortestPath(startLoc, endLoc, map)
    println("Start -> End: $stepsNoCheat")

    val forward = getPath(endLoc, cameFromStart, startLoc)
    val backward = forward.reversed()

    var cntShortcuts = 0L
    for ((stepsFromEnd, backLoc) in backward.withIndex()) {
        val shortcuts = forward.withIndex()
            .filter { (i, loc) -> loc.closeEnough(backLoc) && (stepsFromEnd + i) < stepsNoCheat - MAX_CHEAT }
        for ((stepsFromStart, forwardLoc) in shortcuts) {
            val stepsSaved = stepsNoCheat - stepsFromStart - stepsFromEnd - manhattanDistance(forwardLoc, backLoc)
            if (stepsSaved >= MIN_SAVED) cntShortcuts += 1
        }
    }
    println("Total shortcuts: $cntShortcuts")
}

private fun parseInput(input: List<String>): Triple<Map<Loc, Char>, Loc, Loc> {
    val map = buildMap {
        input.forEachIndexed { r, line ->
            line.forEachIndexed { c, char ->
                this[Loc(r, c)] = char
            }
        }
    }

    val startLoc = map.filter { it.value == 'S' }.keys.first()
    val endLoc = map.filter { it.value == 'E' }.keys.first()
    return Triple(map, startLoc, endLoc)
}

private fun Loc.closeEnough(other: Loc) = manhattanDistance(this, other) <= MAX_CHEAT

private fun manhattanDistance(head: Loc, tail: Loc): Int {
    return (head.r - tail.r).absoluteValue + (head.c - tail.c).absoluteValue
}

private fun getPath(
    endLoc: Loc,
    cameFromStart: Map<Loc, Loc>,
    startLoc: Loc
): List<Loc> {
    val pathStartEnd = mutableListOf<Loc>()
    var curr = endLoc
    while (curr in cameFromStart) {
        pathStartEnd.add(curr)
        curr = cameFromStart[curr]!!
    }
    pathStartEnd.add(startLoc)
    pathStartEnd.reverse()
    return pathStartEnd
}

private fun shortestPath(
    startLoc: Loc,
    endLoc: Loc,
    map: Map<Loc, Char>,
): Pair<Int, Map<Loc, Loc>> {
    val compareBySteps: Comparator<Pair<Loc, Int>> = compareBy { it.second }
    val frontier = PriorityQueue(compareBySteps)
    frontier.add(Pair(startLoc, 0))

    val cameFrom = mutableMapOf<Loc, Loc>()
    val visited = mutableSetOf<Loc>()

    while (frontier.isNotEmpty()) {
        val (curr, steps) = frontier.remove()
        visited.add(curr)

        if (curr == endLoc) {
            return steps to cameFrom
        }
        val neighbors = getNeighbors(curr, map)
        for (neighbor in neighbors) {
            val newCost = steps + 1
            if (neighbor !in visited) {
                cameFrom[neighbor] = curr
                frontier.add(Pair(neighbor, newCost))
            }
        }
    }
    return -1 to cameFrom
}

private fun getNeighbors(cell: Loc, map: Map<Loc, Char>): List<Loc> {
    val maxRow = map.keys.maxOf { it.r }
    val maxCol = map.keys.maxOf { it.c }
    val neighbors = mutableListOf<Loc>()
    for (delta in listOf(-1 to 0, 0 to 1, 1 to 0, 0 to -1)) {
        val newLoc = cell + delta
        if (newLoc.r !in (0..maxRow) || newLoc.c !in (0..maxCol)) continue
        if (map[newLoc] in listOf('.', 'E', 'S')) {
            neighbors.add(newLoc)
        }
    }
    return neighbors
}
