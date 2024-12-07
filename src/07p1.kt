fun main() {
    val input = readInput("07")
    val ces = mutableListOf<CalibrationEquation>()
    input.forEach { line ->
        val (l, r) = line.split(": ")
        val vs = r.split(" ").map { it.toLong() }.toList()
        ces.add(CalibrationEquation(l.toLong(), vs))
    }

    val cesOk = mutableListOf<CalibrationEquation>()
    ces.forEach { ce ->
        var partials = setOf(0L)
        outer@ for ((i, v) in ce.vs.withIndex()) {
            val newPartials = mutableSetOf<Long>()
            for (p in partials) {
                if (p + v == ce.result && i == ce.vs.lastIndex) {
                    cesOk.add(ce)
                    break@outer
                }
                if (p + v <= ce.result) newPartials.add(p + v)
                if (p * v == ce.result && i == ce.vs.lastIndex) {
                    cesOk.add(ce)
                    break@outer
                }
                if (p * v <= ce.result && p * v != 0L) newPartials.add(p * v)
            }
            partials = newPartials
        }
    }
    cesOk.sumOf { it.result }.println()
}

data class CalibrationEquation(val result: Long, val vs: List<Long>)