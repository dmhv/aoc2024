fun main() {
    val input = readInput("03")

    val pat = Regex("""mul\((\d{1,3}),(\d{1,3})\)""")

    var result = 0
    for (line in input) {
        val matches = pat.findAll(line)
        for (match in matches) {
            val (a, b) = match.destructured
            result += a.toInt() * b.toInt()
        }
    }
    result.println()
}

