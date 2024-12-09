fun main() {
    val input = readInput("09")
    // in the list below, full blocks are represented by non-negative consecutive integers
    // while the empty space is represented by the value -1
    val disk = parseInput(input[0])
    var l = 0
    var r = disk.size - 1
    while (l < r) {
        if (disk[l] != -1) l++ else if (disk[r] == -1) r-- else {
            disk[l] = disk[r].also { disk[r] = disk[l] }
        }
    }
    disk.filter { it != -1 }.mapIndexed { i, c -> (i * c).toLong() }.sum().println()
}

private fun parseInput(input: String): MutableList<Int> {
    val out = mutableListOf<Int>()
    input.forEachIndexed { i, c ->
        out.addAll(List(c.digitToInt()) { if (i % 2 == 0) i / 2 else -1 })
    }
    return out
}
