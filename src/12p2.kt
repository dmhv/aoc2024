fun main() {
    val input = readInput("12")
    val map = buildMap {
        input.forEachIndexed { i, line ->
            line.forEachIndexed { j, c ->
                this[Pair(i, j)] = c
            }
        }
    }
    val maxRow = map.keys.maxOf { it.first }
    val maxCol = map.keys.maxOf { it.second }

    val regions = mutableListOf<Region>()
    val toVisit = map.keys.toMutableSet()

    while (toVisit.isNotEmpty()) {
        val start = toVisit.first()
        val (thisRegionPoints, plantType) = bfs(start, map, maxRow, maxCol)
        regions.add(Region(plantType, thisRegionPoints))
        toVisit.removeAll(thisRegionPoints.toSet())
    }

    regions.sumOf { it.area() * it.perimeter() }.println()
}

private fun bfs(
    start: Pair<Int, Int>,
    map: Map<Pair<Int, Int>, Char>,
    maxRow: Int,
    maxCol: Int
): Pair<MutableSet<Pair<Int, Int>>, Char> {
    val visited = mutableSetOf<Pair<Int, Int>>()
    val queue = ArrayDeque<Pair<Int, Int>>()
    queue.add(start)
    val thisRegionPoints = mutableSetOf<Pair<Int, Int>>()
    val plantType = map[start]!!
    while (!queue.isEmpty()) {
        val (r, c) = queue.removeFirst()
        visited.add(r to c)
        thisRegionPoints.add(r to c)
        for ((dr, dc) in listOf(-1 to 0, 0 to 1, 1 to 0, 0 to -1)) {
            val (nr, nc) = r + dr to c + dc
            if (
                !visited.contains(nr to nc) &&
                nr in 0..maxRow && nc in 0..maxCol &&
                map[nr to nc]!! == plantType
            ) {
                queue.add(nr to nc)
            }
        }
    }
    return Pair(thisRegionPoints, plantType)
}


private fun Region.perimeter(): Int {
    var total = 0
    for (r in this.points) {
        // immediate neighbors := the ones to the left/right or above/below
        val neighbors = getImmediateNeighbors(this.points, r)
        total += when (neighbors.size) {
            0 -> 4
            1 -> 2
            2 -> countTwoNeighborCorners(this.points, r, neighbors)
            3 -> countThreeNeighborCorners(this.points, r, neighbors)
            4 -> countFourNeighborCorners(this.points, r)
            else -> throw Exception("No idea how we got ${neighbors.size} immediate neighbors!")
        }
    }
    return total
}

fun countThreeNeighborCorners(ps: Set<Pair<Int, Int>>, p: Pair<Int, Int>, neighbors: List<Pair<Int, Int>>): Int {
    val (r, c) = p
    var cnt = 0
    // figure out if the shape is T or -|
    val sameRowNeighbors = neighbors.count { it.first == r }
    if (sameRowNeighbors == 2) {
        // T shape, could be flipped
        val rr = neighbors.first { it.first != r }.first
        for (dc in listOf(-1, 1)) {
            val nc = c + dc
            if (rr to nc !in ps) cnt++
        }
        return cnt
    } else {
        // -| shape, could be flipped
        val cc = neighbors.first { it.second != c }.second
        for (dr in listOf(-1, 1)) {
            val nr = r + dr
            if (nr to cc !in ps) cnt++
        }
        return cnt
    }
}

fun countFourNeighborCorners(ps: Set<Pair<Int, Int>>, p: Pair<Int, Int>): Int {
    var cnt = 0
    val (r, c) = p
    // add a corner for each empty diagonal neighbor
    for ((dr, dc) in listOf(-1 to -1, -1 to 1, 1 to -1, 1 to 1)) {
        val nr = r + dr
        val nc = c + dc
        if ((nr to nc) !in ps) cnt += 1
    }
    return cnt
}

fun countTwoNeighborCorners(ps: Set<Pair<Int, Int>>, p: Pair<Int, Int>, neighbors: List<Pair<Int, Int>>): Int {
    val (r, c) = p
    // if the two neighbors are on a line, no corners
    if (neighbors.map { it.first }.toSet().size == 1 || neighbors.map { it.second }.toSet().size == 1) {
        return 0
    } else {
        // we know we have an L shape, if its inside is filled, 1 corner, else 2 corners
        val rInside = neighbors.map { it.first }.toSet().minus(r).first()
        val cInside = neighbors.map { it.second }.toSet().minus(c).first()
        return when (rInside to cInside in ps) {
            true -> 1
            false -> 2
        }
    }
}

fun getImmediateNeighbors(ps: Set<Pair<Int, Int>>, p: Pair<Int, Int>): List<Pair<Int, Int>> {
    val neighbors = mutableListOf<Pair<Int, Int>>()
    val (r, c) = p
    for ((dr, dc) in listOf(-1 to 0, 0 to 1, 1 to 0, 0 to -1)) {
        val (nr, nc) = r + dr to c + dc
        if ((nr to nc) in ps) neighbors.add(nr to nc)
    }
    return neighbors
}
