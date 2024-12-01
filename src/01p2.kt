fun main() {
    val input = readInput("01")

    val (lefts, rights) = input.map { line ->
        line.split("""\s+""".toRegex()).let {
            require(it.size == 2) { "Expected two numbers per line, got $it" }
            it[0].toInt() to it[1].toInt()
        }
    }.unzip()

    val frequencies = rights.groupingBy { it }.eachCount()
    lefts.sumOf { it * (frequencies[it] ?: 0) }.println()
}
