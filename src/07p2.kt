fun main() {
    val input = readInput("07")
    val ces = parseInput(input)

    val cesOk = mutableListOf<CalibrationEquation>()
    ces.forEach { ce ->
        var partials = setOf(ce.vs.removeFirst())
        outer@ while (ce.vs.isNotEmpty()) {
            val v = ce.vs.removeFirst()
            val newPartials = mutableSetOf<Long>()
            for (p in partials) {
                for (candidate in setOf(p + v, p * v, "$p$v".toLong())) {
                    if (candidate == ce.result && ce.vs.isEmpty()) {
                        cesOk.add(ce)
                        break@outer
                    }
                    if (candidate <= ce.result) newPartials.add(candidate)
                }
            }
            partials = newPartials
        }
    }
    cesOk.sumOf { it.result }.println()
}

private fun parseInput(input: List<String>): MutableList<CalibrationEquation> {
    val ces = mutableListOf<CalibrationEquation>()
    input.forEach { line ->
        val (l, r) = line.split(": ")
        val vs = r.split(" ").map { it.toLong() }.toMutableList()
        ces.add(CalibrationEquation(l.toLong(), vs))
    }
    return ces
}
