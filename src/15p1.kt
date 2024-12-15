import java.lang.StringBuilder

fun main() {
    val input = readInput("15")

    val mapLines = input.filter { line -> line.startsWith('#') }
    val map = mutableMapOf<Loc, Char>()
    for ((r, line) in mapLines.withIndex()) {
        for ((c, char) in line.toCharArray().withIndex()) {
            map[Loc(r, c)] = char
        }
    }

    val robotLoc = map.filter { it.value == '@' }.keys.first()
    val robot = Rob(robotLoc)

    val lastMapLine = input.withIndex().filter { (_, line) -> line.startsWith('#') }.maxOf { it.index }
    val directions = input
        .filterIndexed { i, _ -> i > lastMapLine }
        .joinToString("").toCharArray()
        .map {
            when (it) {
                '^' -> Direction.NORTH
                '>' -> Direction.EAST
                'v' -> Direction.SOUTH
                '<' -> Direction.WEST
                else -> throw IllegalArgumentException()
            }
        }

    val game = Sokoban(map, robot, directions)
    game.run()
    println("FINAL SCORE: ${game.score()}")
}

class Sokoban(val map: MutableMap<Loc, Char>, private val robot: Rob, private val directions: List<Direction>) {
    fun run(doPrint: Boolean = false) {
        for (direction in directions) {
            move(direction)
            if (doPrint) printMap()
        }
    }

    private fun move(direction: Direction) {
        val delta = when (direction) {
            Direction.NORTH -> -1 to 0
            Direction.EAST -> 0 to 1
            Direction.SOUTH -> 1 to 0
            Direction.WEST -> 0 to -1
        }
        val nextLoc = robot.loc + delta

        if (map[nextLoc] == '#') {
            return
        } else if (map[nextLoc] == '.') {
            map[robot.loc] = '.'
            map[nextLoc] = '@'
            robot.loc = nextLoc
        } else if (map[nextLoc] == 'O') {
            var thisLoc = nextLoc
            while (map[thisLoc] == 'O') {
                thisLoc += delta
            }
            if (map[thisLoc] == '#') {
                return
            } else if (map[thisLoc] == '.') {
                map[robot.loc] = '.'
                map[nextLoc] = '@'
                map[thisLoc] = 'O'
                robot.loc = nextLoc
            }
        }
    }

    private fun printMap() {
        val out = StringBuilder()
        for (r in 0..map.maxOf { it.key.r }) {
            for (c in 0..map.maxOf { it.key.c }) {
                out.append(map[Loc(r, c)])
            }
            out.append('\n')
        }
        println(out)
    }

    fun score(): Long {
        val out = map.filter { it.value == 'O' }.keys.sumOf { (r, c) -> r.toLong() * 100 + c }
        return out
    }
}

data class Loc(val r: Int, val c: Int)

operator fun Loc.plus(other: Pair<Int, Int>) = Loc(r + other.first, c + other.second)

data class Rob(var loc: Loc)
