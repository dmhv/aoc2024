private const val MAP_WIDTH = 101
private const val MAP_HEIGHT = 103

fun main() {
    val input = readInput("142")
    val robots = mutableListOf<Robot>()
    for (line in input) {
        val (p, v) = line.split(" ")
        val (x, y) = p.removePrefix("p=").split(",").map { it.trim().toInt() }
        val (vx, vy) = v.removePrefix("v=").split(",").map { it.trim().toInt() }
        robots.add(Robot(x, y, vx, vy))
    }

    val stepToCheckSum = mutableMapOf<Int, Long>()

    for (step in 1..10_000) {
        for (robot in robots) {
            robot.move()
        }
        val topLeftQuadrantCount = robots.count { it.x < MAP_WIDTH / 2 && it.y < MAP_HEIGHT / 2 }
        val topRightQuadrantCount = robots.count { it.x > MAP_WIDTH / 2 && it.y < MAP_HEIGHT / 2 }
        val bottomLeftQuadrantCount = robots.count { it.x < MAP_WIDTH / 2 && it.y > MAP_HEIGHT / 2 }
        val bottomRightQuadrantCount = robots.count { it.x > MAP_WIDTH / 2 && it.y > MAP_HEIGHT / 2 }
        val checkSum = (topLeftQuadrantCount.toLong() * topRightQuadrantCount * bottomLeftQuadrantCount * bottomRightQuadrantCount)
        stepToCheckSum[step] = checkSum
    }

    stepToCheckSum.values.sorted().take(10).println()
    stepToCheckSum.filter { it.value == 95830020L }.println()
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
