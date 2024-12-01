fun main() {
    val input = readInput("01")

    val lefts = mutableListOf<Int>()
    val rights = mutableMapOf<Int, Int>()
    for (line in input) {
        val pair = line.split("\\s+".toRegex()).map { it.toInt() }
        lefts.add(pair.first())
        rights[pair.last()] = rights[pair.last()]?.plus(1) ?: 1
    }

    lefts.sumOf { it * (rights[it] ?: 0) }.println()
}
