fun main() {
    val input = readInput("05")
    val (goesAfter, updates) = parseInput(input)
    val correctUpdates = updates.filter { isUpdateCorrect(it, goesAfter) }
    val incorrectUpdates = updates.filter { !correctUpdates.contains(it) }

    val correctedUpdates = mutableListOf<List<Int>>()
    for (update in incorrectUpdates) {
        val upd = update.toMutableList()

        var isOrdered = false
        while (!isOrdered) {
            isOrdered = true
            upd.withIndex().zipWithNext { (i, x), (_, next) ->
                if (
                    !goesAfter.getOrDefault(x, setOf()).contains(next) ||
                    goesAfter.getOrDefault(next, setOf()).contains(x)
                ) {
                    upd[i] = upd[i + 1].also { upd[i + 1] = upd[i] }
                    isOrdered = false
                }
            }
        }
        correctedUpdates.add(upd)
    }

    correctedUpdates.sumOf { it[it.size / 2] }.println() // 4480
}

private fun isUpdateCorrect(
    update: List<Int>,
    goesAfter: Map<Int, Set<Int>>
): Boolean {
    val seen = mutableSetOf<Int>()
    return update.all { x ->
        val shouldBeAfterX = goesAfter.getOrDefault(x, emptySet())
        val thisIsFine = shouldBeAfterX.none { it in seen } &&
                seen.all { z -> x in goesAfter.getOrDefault(z, emptySet()) }
        seen.add(x)
        thisIsFine
    }
}

private fun parseInput(input: List<String>): Pair<MutableMap<Int, Set<Int>>, MutableList<List<Int>>> {
    val goesAfter = mutableMapOf<Int, Set<Int>>()
    val updates = mutableListOf<List<Int>>()
    var foundBlank = false
    for (line in input) {
        if (!foundBlank) {
            if (line.isEmpty()) {
                foundBlank = true
                continue
            }
            val (a, b) = line.split("|").map { it.toInt() }.toList()
            goesAfter[a] = goesAfter.getOrDefault(a, mutableSetOf()).plus(b)
        } else {
            updates.add(line.split(",").map { it.toInt() }.toList())
        }
    }
    return Pair(goesAfter, updates)
}
