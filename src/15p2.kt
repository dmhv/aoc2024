import java.lang.IllegalStateException
import java.lang.StringBuilder

fun main() {
    val input = readInput("15")

    val mapLines = input.filter { line -> line.startsWith('#') }
    val map = mutableMapOf<Loc, Char>()
    for ((r, line) in mapLines.withIndex()) {
        for ((c, char) in line.toCharArray().withIndex()) {
            when (char) {
                '#' -> {
                    map[Loc(r, c * 2)] = '#'
                    map[Loc(r, c * 2 + 1)] = '#'
                }

                'O' -> {
                    map[Loc(r, c * 2)] = '['
                    map[Loc(r, c * 2 + 1)] = ']'
                }

                '@' -> {
                    map[Loc(r, c * 2)] = '@'
                    map[Loc(r, c * 2 + 1)] = '.'
                }

                '.' -> {
                    map[Loc(r, c * 2)] = '.'
                    map[Loc(r, c * 2 + 1)] = '.'
                }
            }
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

    val game = Sokoban2(map, robot, directions)
    game.run(doPrint = false)
    println("FINAL SCORE: ${game.score()}")
}

class Sokoban2(val map: MutableMap<Loc, Char>, private val robot: Rob, private val directions: List<Direction>) {
    fun run(doPrint: Boolean = false, steps: Int? = null) {
        val ds = if (steps == null) directions else directions.take(steps)
        for ((i, direction) in ds.withIndex()) {
            if (doPrint) println("=== GOING $direction ===")
            move(direction)
            if (doPrint) printMap(i)
        }
    }

    private fun move(direction: Direction) {
        val delta = directionToDelta(direction)
        val nextLoc = robot.loc + delta

        if (map[nextLoc] == '#') {
            return
        } else if (map[nextLoc] == '.') {
            map[robot.loc] = '.'
            map[nextLoc] = '@'
            robot.loc = nextLoc
        } else if (map[nextLoc] == ']') {
            if (direction == Direction.EAST) throw IllegalStateException("Can not move EAST into ]")
            if (direction == Direction.WEST) {
                val canPush = canPushHorizontally(nextLoc, direction)
                if (canPush) pushHorizontally(nextLoc, direction)  // handles the robot's coords
            } else {
                val canPush = canPushVertically(nextLoc, direction)
                if (canPush) {
                    pushVertically(nextLoc, direction) // does NOT handle robot's coords
                    map[robot.loc] = '.'
                    map[nextLoc] = '@'
                    robot.loc = nextLoc
                }
            }
        } else if (map[nextLoc] == '[') {
            if (direction == Direction.WEST) throw IllegalStateException("Can not move WEST into [")
            if (direction == Direction.EAST) {
                val canPush = canPushHorizontally(nextLoc, direction)
                if (canPush) pushHorizontally(nextLoc, direction)
            } else {
                val canPush = canPushVertically(nextLoc, direction)
                if (canPush) {
                    pushVertically(nextLoc, direction)
                    map[robot.loc] = '.'
                    map[nextLoc] = '@'
                    robot.loc = nextLoc
                }
            }
        }
    }

    private fun canPushVertically(loc: Loc, direction: Direction): Boolean {
        // evaluates if it's possible to push something into the given location from the given direction
        check(direction in listOf(Direction.NORTH, Direction.SOUTH))
        if (map[loc] == '#') return false
        if (map[loc] == '.') return true

        // if we're here, we are pushing into a box and need to check if that other box is pushable
        val delta = directionToDelta(direction)
        val locsThatNeedToMove = mutableListOf(Loc(loc.r + delta.first, loc.c))
        if (map[loc] == ']') locsThatNeedToMove.add(Loc(loc.r + delta.first, loc.c - 1))
        if (map[loc] == '[') locsThatNeedToMove.add(Loc(loc.r + delta.first, loc.c + 1))
        return locsThatNeedToMove.map { canPushVertically(it, direction) }.all { it }
    }

    private fun pushVertically(loc: Loc, direction: Direction) {
        check(direction in listOf(Direction.NORTH, Direction.SOUTH))

        // if we get '.' at the location of the push, it means we've already pushed this box from the other side
        if (map[loc] == '.') return

        check(map[loc]!! in listOf(']', '['))
        // this is the other side of the box we are pushing
        val sideLoc = if (map[loc]!! == '[') Loc(loc.r, loc.c + 1) else Loc(loc.r, loc.c - 1)
        check(map[sideLoc]!! in listOf(']', '['))

        val nextLoc = loc + directionToDelta(direction)
        val nextSideLoc = sideLoc + directionToDelta(direction)

        if (map[nextLoc]!! != '.' || map[nextSideLoc]!! != '.') {
            pushVertically(nextLoc, direction)
            pushVertically(nextSideLoc, direction)
        }

        map[loc] = map[nextLoc]!!.also { map[nextLoc] = map[loc]!! }
        map[sideLoc] = map[nextSideLoc]!!.also { map[nextSideLoc] = map[sideLoc]!! }
    }

    private fun pushHorizontally(loc: Loc, direction: Direction) {
        check(direction in listOf(Direction.EAST, Direction.WEST))
        val delta = directionToDelta(direction)
        var thisLoc = loc
        val locsToMove = mutableListOf<Loc>()
        while (map[thisLoc] != '.') {
            locsToMove.add(thisLoc)
            thisLoc += delta
        }
        locsToMove.add(thisLoc)

        for ((curr, next) in locsToMove.reversed().zipWithNext()) {
            map[curr] = map[next]!!.also { map[next] = map[curr]!! }
        }
        map[robot.loc] = '.'
        map[loc] = '@'
        robot.loc = loc
    }

    private fun canPushHorizontally(loc: Loc, direction: Direction): Boolean {
        check(direction in listOf(Direction.EAST, Direction.WEST))
        val delta = directionToDelta(direction)
        val nextLoc = loc + delta // this is supposed to be the other half of the same box
        if (direction == Direction.WEST) check(map[nextLoc] == '[') { "Expected [, got ${map[nextLoc]}" }
        if (direction == Direction.EAST) check(map[nextLoc] == ']') { "Expected ], got ${map[nextLoc]}" }
        val nextNextLoc = nextLoc + delta
        if (map[nextNextLoc] == ']' && direction == Direction.WEST) return canPushHorizontally(nextNextLoc, direction)
        if (map[nextNextLoc] == '[' && direction == Direction.EAST) return canPushHorizontally(nextNextLoc, direction)
        if (map[nextNextLoc] == '#') return false
        if (map[nextNextLoc] == '.') return true
        throw IllegalStateException("This not gud")
    }

    private fun printMap(steps: Int? = null) {
        if (steps != null) println("After $steps steps:")
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
        val out = map.filter { it.value == '[' }.keys.sumOf { (r, c) -> r.toLong() * 100 + c }
        return out
    }
}

private fun directionToDelta(direction: Direction) = when (direction) {
    Direction.NORTH -> -1 to 0
    Direction.EAST -> 0 to 1
    Direction.SOUTH -> 1 to 0
    Direction.WEST -> 0 to -1
}
