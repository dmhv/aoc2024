fun main() {
    val input = readInput("05")

    val (goesAfter, updates) = parseInput(input)

    val correctUpdates = mutableListOf<List<Int>>()

    updates.forEach { update ->
        val seen = mutableSetOf<Int>()
        var isCorrect = true
        for ((i, x) in update.withIndex()) {
            if (!isCorrect) {
                break
            }
            // check "y is after x for each y in goesAfter[x]"
            val shouldBeAfterX = goesAfter.getOrDefault(x, setOf())
            for (y in shouldBeAfterX) {
                if (seen.contains(y)) {
                    isCorrect = false
//                    println("$update is NOT correct, as $y came before $x")
                    break
                }
            }
            // check "x is after each z for z in seen"
            for (z in seen) {
                val shouldBeAfterZ = goesAfter.getOrDefault(z, setOf())
                if (!shouldBeAfterZ.contains(x)) {
                    isCorrect = false
//                    println("$update is NOT correct, as $x does not follow $z")
                }
            }

            seen.add(x)
        }
        if (isCorrect) {
            correctUpdates.add(update)
        }
    }

//    println("Got ${correctUpdates.size} correct updates")

    correctUpdates.sumOf { it[it.size / 2] }.println()
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

            if (goesAfter.containsKey(a)) {
                goesAfter[a] = goesAfter[a]!!.plus(b)
            } else {
                goesAfter[a] = setOf(b)
            }
        } else {
            updates.add(line.split(",").map { it.toInt() }.toList())
        }
    }
    return Pair(goesAfter, updates)
}
