fun main() {
    val input = readInput("03")
    val pat = Regex("""mul\((\d{1,3}),(\d{1,3})\)""")

    input.sumOf { line ->
        pat.findAll(line).sumOf {
            val (a, b) = it.destructured
            a.toInt() * b.toInt()
        }
    }.println()
}
