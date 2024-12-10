fun main() {
    val input = readInput("10")
    val (map, heads) = parseInput(input)

    var score = 0
    for (head in heads) {
        val s = bfs(map, head)
        println("$head -> $s")
        score += s
    }
    println("Score: $score")
}

private fun parseInput(input: List<String>): Pair<Map<Pair<Int, Int>, Int>, List<Pair<Int, Int>>> {
    val map = mutableMapOf<Pair<Int, Int>, Int>()
    val heads = mutableListOf<Pair<Int, Int>>()
    for (i in input.indices) {
        val line = input[i].toCharArray()
        for (j in input[i].indices) {
            map[Pair(i, j)] = line[j].digitToInt()
            if (line[j].digitToInt() == 0) heads.add(Pair(i, j))
        }
    }
    return Pair(map, heads)
}

private fun bfs(map: Map<Pair<Int, Int>, Int>, head: Pair<Int, Int>): Int {
    val queue = ArrayDeque<Pair<Int, Int>>()
    queue.add(head)
    val maxRow = map.maxOf { it.key.first }
    val maxCol = map.maxOf { it.key.second }

    val trailEnds = mutableMapOf<Pair<Int, Int>, Int>()
    while (!queue.isEmpty()) {
        val curr = queue.removeFirst()
        for (delta in listOf(Pair(-1, 0), Pair(0, -1), Pair(1, 0), Pair(0, 1))) {
            val next = Pair(curr.first + delta.first, curr.second + delta.second)
            if (next.first in 0..maxRow && next.second in 0..maxCol && map[next]!! - map[curr]!! == 1) {
                if (map[next]!! == 9) {
//                    println("..reached $next from $head")
                    if (!trailEnds.containsKey(next)) {
                        trailEnds[next] = 1
                    } else {
                        trailEnds[next] = trailEnds[next]!! + 1
                    }
                } else {
                    queue.add(next)
                }
            }
        }
    }
    return trailEnds.values.sumOf { it }
}
