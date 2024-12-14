private const val MAP_WIDTH = 101
private const val MAP_HEIGHT = 103

fun main() {
    val input = readInput("14")
    val robots = mutableListOf<Robot>()
    for (line in input) {
        val (p, v) = line.split(" ")
        val (x, y) = p.removePrefix("p=").split(",").map { it.trim().toInt() }
        val (vx, vy) = v.removePrefix("v=").split(",").map { it.trim().toInt() }
        robots.add(Robot(x, y, vx, vy))
    }

    // I arrived to these specific coordinates by first looking for frames with unusually high
    // column counts, trying to find the trunk of the tree - but probably found the frame around it :)
    val x0 = 35
    val x1 = 66
    val y0 = 30
    val y1 = 62

    for (step in 1..300_000) {
        for (robot in robots) {
            robot.move()
        }
        val countX0 = robots.count { it.x == x0 && it.y in y0..y1 }
        val countX1 = robots.count { it.x == x1 && it.y in y0..y1 }
        val countY0 = robots.count { it.y == y0 && it.x in x0..x1 }
        val countY1 = robots.count { it.y == y1 && it.x in x0..x1 }
        val cnt = countX0 + countX1 + countY0 + countY1
        if (cnt > 60) {
            printMap(robots)
            println("..@STEP NUMBER $step")
        }
    }
}

private fun printMap(robots: MutableList<Robot>) {
    for (y in 0 until MAP_HEIGHT) {
        var out = ""
        for (x in 0 until MAP_WIDTH) {
            val cnt = robots.count { it.x == x && it.y == y }
            out += if (cnt > 0) cnt else "."
        }
        println(out)
    }
}

private fun Robot.move() {
    x = (x + vx + MAP_WIDTH) % MAP_WIDTH
    y = (y + vy + MAP_HEIGHT) % MAP_HEIGHT
}
