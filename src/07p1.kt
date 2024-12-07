fun main() {
    val input = readInput("07")
    val ces = parseInput(input)

    val cesOk = mutableListOf<CalibrationEquation>()
    ces.forEach { ce ->
        var partials = setOf(0L)
        outer@ for ((i, v) in ce.vs.withIndex()) {
            val newPartials = mutableSetOf<Long>()
            for (p in partials) {
                for (candidate in setOf(p + v, p * v).minus(0L)) {
                    if (candidate == ce.result && i == ce.vs.lastIndex) {
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
        val vs = r.split(" ").map { it.toLong() }.toList()
        ces.add(CalibrationEquation(l.toLong(), vs))
    }
    return ces
}

data class CalibrationEquation(val result: Long, val vs: List<Long>)