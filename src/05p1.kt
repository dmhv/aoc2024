fun main() {
    val input = readInput("05")
    val (goesAfter, updates) = parseInput(input)
    val correctUpdates = updates.filter {isUpdateCorrect(it, goesAfter)}
    correctUpdates.sumOf { it[it.size / 2] }.println()
}

private fun isUpdateCorrect(update: List<Int>, goesAfter: Map<Int, Set<Int>>): Boolean {
    val seen = mutableSetOf<Int>()
    return update.all { x ->
        val shouldBeAfterX = goesAfter.getOrDefault(x, emptySet())
        val thisIsFine =
            shouldBeAfterX.none { it in seen } &&
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
