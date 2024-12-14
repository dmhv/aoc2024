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

    for (step in 1..100) {
        for (robot in robots) {
            robot.move()
        }
    }

    val topLeftQuadrantCount = robots.count { it.x < MAP_WIDTH / 2 && it.y < MAP_HEIGHT / 2 }
    val topRightQuadrantCount = robots.count { it.x > MAP_WIDTH / 2 && it.y < MAP_HEIGHT / 2 }
    val bottomLeftQuadrantCount = robots.count { it.x < MAP_WIDTH / 2 && it.y > MAP_HEIGHT / 2 }
    val bottomRightQuadrantCount = robots.count { it.x > MAP_WIDTH / 2 && it.y > MAP_HEIGHT / 2 }
    println("topLeftQuadrantCount: $topLeftQuadrantCount")
    println("topRightQuadrantCount: $topRightQuadrantCount")
    println("bottomLeftQuadrantCount: $bottomLeftQuadrantCount")
    println("bottomRightQuadrantCount: $bottomRightQuadrantCount")
    println(topLeftQuadrantCount * topRightQuadrantCount * bottomLeftQuadrantCount * bottomRightQuadrantCount)
}

data class Robot(var x: Int, var y: Int, val vx: Int, val vy: Int)

private fun Robot.move() {
    x = (x + vx + MAP_WIDTH) % MAP_WIDTH
    y = (y + vy + MAP_HEIGHT) % MAP_HEIGHT
}