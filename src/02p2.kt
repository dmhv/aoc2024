fun main() {
    val input = readInput("02")

    val ls = input.map { line ->
        line.split("""\s+""".toRegex()).map(String::toInt)
    }

    ls.count { l ->
        l.indices.any { i ->
            isListValid(l.filterIndexed { j, _ -> j != i })
        }
    }.println()
}
