fun main() {
    val input = readInput("06")
    val (guard, lab) = parseInput(input)

    val visited = patrol(guard, lab)
    visited.size.println()
}

private fun patrol(guard: Guard, lab: Lab): MutableSet<Pair<Int, Int>> {
    val visited = mutableSetOf<Pair<Int, Int>>()

    while (true) {
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
            break
        }

        val atNextPosition = lab.map[nextPosition]!!
        when (atNextPosition) {
            '.' -> {
                visited.add(nextPosition)
                guard.position = nextPosition
            }

            '#' -> guard.turnRight()
        }
    }
    return visited
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

enum class Direction { NORTH, SOUTH, WEST, EAST }

data class Guard(var position: Pair<Int, Int>, var direction: Direction) {
    fun turnRight() {
        this.direction = when (direction) {
            Direction.NORTH -> Direction.EAST
            Direction.EAST -> Direction.SOUTH
            Direction.SOUTH -> Direction.WEST
            Direction.WEST -> Direction.NORTH
        }
    }
}

data class Lab(val map: Map<Pair<Int, Int>, Char>, val nRows: Int, val nCols: Int)

fun Lab.print(guard: Guard, visited: Set<Pair<Int, Int>>) {
    for (row in 0 until nRows) {
        for (col in 0 until nCols) {
            if (row == guard.position.first && col == guard.position.second) {
                when (guard.direction) {
                    Direction.EAST -> print('>')
                    Direction.WEST -> print('<')
                    Direction.NORTH -> print('^')
                    Direction.SOUTH -> print('v')
                }
            } else if (visited.contains(row to col)) {
                print('X')
            } else {
                print(map[row to col].toString())
            }
        }
        print("\n")
    }
}
