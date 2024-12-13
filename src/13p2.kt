private val RE_BUTTON = Regex(""".*X\+(?<x>\d+), Y\+(?<y>\d+)""")
private val RE_PRIZE = Regex(""".*X=(?<x>\d+), Y=(?<y>\d+)""")
private const val PRIZE_OFFSET = 10000000000000L

fun main() {
    val input = readInput("13")
    val clawMachines = parseInput(input)

    var totalCost = 0L
    for (cm in clawMachines) {
        val (found, cost) = solve(cm)
        if (found) {
            println("Claw machine $cm -> cost $cost")
            totalCost += cost
        }
    }
    totalCost.println()
}

private fun solve(cm: ClawMachine): Pair<Boolean, Long> {
    val det = cm.ax * cm.by - cm.bx * cm.ay
    if (det == 0L) return false to 0

    val a = cm.by * cm.px - cm.bx * cm.py
    val b = -cm.ay * cm.px + cm.ax * cm.py
    val aNorm = a / det
    val bNorm = b / det

    return if ((aNorm * cm.ax + bNorm * cm.bx == cm.px) && (aNorm * cm.ay + bNorm * cm.by == cm.py)) {
        true to aNorm * 3 + bNorm
    } else {
        false to 0
    }
}

private fun parseInput(input: List<String>): MutableList<ClawMachine> {
    val numClaws = input.size / 4
    val clawMachines = mutableListOf<ClawMachine>()

    for (i in 0..numClaws) {
        val matchA = RE_BUTTON.find(input[i * 4])
        val ax = matchA?.groups?.get("x")?.value?.toLong()
        val ay = matchA?.groups?.get("y")?.value?.toLong()

        val matchB = RE_BUTTON.find(input[i * 4 + 1])
        val bx = matchB?.groups?.get("x")?.value?.toLong()
        val by = matchB?.groups?.get("y")?.value?.toLong()

        val matchP = RE_PRIZE.find(input[i * 4 + 2])
        val px = matchP?.groups?.get("x")?.value?.toLong()?.plus(PRIZE_OFFSET)
        val py = matchP?.groups?.get("y")?.value?.toLong()?.plus(PRIZE_OFFSET)

        if (ax != null && ay != null && bx != null && by != null && px != null && py != null) {
            clawMachines.add(ClawMachine(ax, ay, bx, by, px, py))
        }
    }
    return clawMachines
}
