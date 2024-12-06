fun main() {
    val input = readInput("06")
    val (guard, lab) = parseInput(input)

    val guardInitialPosition = guard.position
    val guardInitialDirection = guard.direction
    val visited = patrol(guard, lab)
    visited.remove(guardInitialPosition)

    val obstructionsCausingLoops = mutableListOf<Pair<Int, Int>>()

    visited.forEach { (row, col) ->
        val newMap = lab.map.toMutableMap()
        newMap[row to col] = '#'
        val labWithObstruction = Lab(newMap, lab.nRows, lab.nCols)
        val looped = isLooping(Guard(guardInitialPosition, guardInitialDirection), labWithObstruction)
        if (looped) {
            obstructionsCausingLoops.add(Pair(row, col))
        }
    }
    obstructionsCausingLoops.size.println()
}

private fun isLooping(guard: Guard, lab: Lab): Boolean {
    val seen = mutableSetOf<Pair<Pair<Int, Int>, Direction>>()

    while (true) {
        val nextPosition = when (guard.direction) {
            Direction.NORTH -> guard.position.first - 1 to guard.position.second
            Direction.SOUTH -> guard.position.first + 1 to guard.position.second
            Direction.WEST -> guard.position.first to guard.position.second - 1
            Direction.EAST -> guard.position.first to guard.position.second + 1
        }

        if (nextPosition.first < 0 || nextPosition.first == lab.nRows || nextPosition.second < 0 || nextPosition.second == lab.nCols) {
            return false
        }
        if (seen.contains(Pair(nextPosition, guard.direction))) return true

        val atNextPosition = lab.map[nextPosition]!!
        when (atNextPosition) {
            '.' -> {
                guard.position = nextPosition
                seen.add(Pair(guard.position, guard.direction))
            }
            '#' -> guard.turnRight()
        }
    }
}

private fun parseInput(input: List<String>): Pair<Guard, Lab> {
    val m = mutableMapOf<Pair<Int, Int>, Char>()
    lateinit var guard: Guard
    input.forEachIndexed { nr, row ->
        row.forEachIndexed { nc, c ->
            if (setOf('>', '<', '^', 'v').contains(c)) {
                val direction = when (c) {
                    '>' -> Direction.EAST
                    '<' -> Direction.WEST
                    '^' -> Direction.NORTH
                    'v' -> Direction.SOUTH
                    else -> throw IllegalArgumentException()
                }
                guard = Guard(Pair(nr, nc), direction)
                m[nr to nc] = '.'
            } else {
                m[nr to nc] = c
            }
        }
    }
    val lab = Lab(m, m.keys.maxOf { it.first } + 1, m.keys.maxOf { it.second } + 1)
    return Pair(guard, lab)
}

