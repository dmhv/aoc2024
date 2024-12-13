private val RE_BUTTON = Regex(""".*X\+(?<x>\d+), Y\+(?<y>\d+)""")
private val RE_PRIZE = Regex(""".*X=(?<x>\d+), Y=(?<y>\d+)""")

fun main() {
    val input = readInput("13")
    val clawMachines = parseInput(input)

    var totalCost = 0L
    for (cm in clawMachines) {
        val (found, minCost) = spin(cm)
        if (found) {
            println("Claw machine $cm -> min cost $minCost")
            totalCost += minCost
        }
    }
    totalCost.println()
}

private fun spin(cm: ClawMachine): Pair<Boolean, Long> {
    var minCost = Long.MAX_VALUE
    for (clicksA in 0..100) {
        val x = clicksA * cm.ax
        if (x > cm.px) break
        val y = clicksA * cm.ay
        if (y > cm.py) break
        val clicksB = (cm.px - x) / cm.bx
        if (x + clicksB * cm.bx == cm.px && y + clicksB * cm.by == cm.py) {
            val cost = 3 * clicksA + clicksB
            if (cost < minCost) minCost = cost
        }
    }
    return if (minCost < Long.MAX_VALUE) true to minCost else false to 0
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
        val px = matchP?.groups?.get("x")?.value?.toLong()
        val py = matchP?.groups?.get("y")?.value?.toLong()

        if (ax != null && ay != null && bx != null && by != null && px != null && py != null) {
            clawMachines.add(ClawMachine(ax, ay, bx, by, px, py))
        }
    }
    return clawMachines
}

data class ClawMachine(val ax: Long, val ay: Long, val bx: Long, val by: Long, val px: Long, val py: Long) {
    override fun toString(): String =
        "A: X+${this.ax}, Y+${this.ay}, B: X+${this.bx}, Y+${this.by}, Prize: X=${this.px}, Y=${this.py}"
}
