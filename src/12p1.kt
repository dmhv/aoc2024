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
    println("Found ${regions.size} region(s)")

    val regionPerimeters = mutableMapOf<Region, Int>()
    for (r in regions) {
        regionPerimeters[r] = r.perimeter()
        println("Perimeter of $r is ${r.perimeter()}")
    }

    for (r in regions) {
        println("$r -> perimeter: ${regionPerimeters[r]}, area: ${r.area()}")
    }
    println()
    println()

    var score = 0
    for (r in regions) {
        val thisScore = regionPerimeters[r]!! * r.area()
        println("Score of $r is $thisScore")
        score += thisScore
    }
    score.println()
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

data class Region(val value: Char, val points: Set<Pair<Int, Int>>) {
    override fun toString(): String =
        "Region of ${this.value} at (${this.points.minOf { it.first }}, ${this.points.minOf { it.second }})"
}

private fun Region.perimeter(): Int {
    return this.points.sumOf { this.countNeighbors(it) }
}

private fun Region.countNeighbors(p: Pair<Int, Int>): Int {
    val (r, c) = p
    var count = 0
    for ((dr, dc) in listOf(-1 to 0, 0 to 1, 1 to 0, 0 to -1)) {
        val (nr, nc) = r + dr to c + dc
        if (this.points.contains(nr to nc)) count++
    }
    return 4 - count
}

fun Region.area(): Int = this.points.size
