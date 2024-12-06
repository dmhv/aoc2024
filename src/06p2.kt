fun main() {
    val input = readInput("06")
    val (guard, lab) = parseInput(input)

    val guardInitialPosition = guard.position
    val guardInitialDirection = guard.direction

    val obstructionsCausingLoops = mutableListOf<Pair<Int, Int>>()
    for (row in 0 until lab.nRows) {
        for (col in 0 until lab.nCols) {
            if (lab.map[row to col] == '#' || guardInitialPosition == row to col) continue

            val newMap = lab.map.toMutableMap()
            newMap[row to col] = '#'
            val labWithObstruction = Lab(newMap, lab.nRows, lab.nCols)
            val looped = isLooping(Guard(guardInitialPosition, guardInitialDirection), labWithObstruction)
            if (looped) {
                obstructionsCausingLoops.add(Pair(row, col))
            }
        }
    }
    obstructionsCausingLoops.size.println()
}

private fun isLooping(guard: Guard, lab: Lab, maxSteps: Int = 10000): Boolean {
    var steps = 0
    while (steps < maxSteps) {
        val nextPosition = when (guard.direction) {
            Direction.NORTH -> guard.position.first - 1 to guard.position.second
            Direction.SOUTH -> guard.position.first + 1 to guard.position.second
            Direction.WEST -> guard.position.first to guard.position.second - 1
            Direction.EAST -> guard.position.first to guard.position.second + 1
        }

        if (
            nextPosition.first < 0 || nextPosition.first == lab.nRows ||
            nextPosition.second < 0 || nextPosition.second == lab.nCols
        ) {
            return false
        }

        val atNextPosition = lab.map[nextPosition]!!
        when (atNextPosition) {
            '.' -> {
                guard.position = nextPosition
                steps++
            }

            '#' -> guard.turnRight()
        }
    }
    return true
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

