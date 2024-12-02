fun main() {
    val input = readInput("02")

    val ls = input.map { line -> line.split("""\s+""".toRegex()).map(String::toInt) }
    ls.count { isListValid(it) }.println()
}

fun isListValid(l: List<Int>): Boolean {
    val diffs = l.subList(1, l.size).zip(l.subList(0, l.size - 1)).map { (a, b) -> a - b }
    return diffs.all { it in 1..3 } || diffs.all { it in -3..-1 }
}