fun main() {
    val input = readInput("02")

    val ls = input.map { line ->
        line.split("""\s+""".toRegex()).map(String::toInt)
    }

    var countValid = 0
    for (l in ls) {
        for (i in l.indices) {
            if (isListValid(l.filterIndexed { index, _ -> index != i })) {
                countValid++
                break
            }
        }
    }
    countValid.println()
}
