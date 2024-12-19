fun main() {
    val input = readInput("19tiny")
    val designs = input[0].split(", ").toSet()
    val patterns = buildSet {
        input.subList(2, input.size).forEach { line -> this.add(line) }
    }

    fun numPossibilities(pattern: String): Int {
        val prefixes = designs.filter { pattern.startsWith(it) }
        if (prefixes.isEmpty()) return 0
        var out = 0
        for (pr in prefixes) {
            out += if (pattern == pr) {
                1
            } else {
                numPossibilities(pattern.removePrefix(pr))
            }
        }
        return out
    }

    var cnt = 0
    for (p in patterns) {
        val pOptions = numPossibilities(p)
        println("$p -> $pOptions")
        cnt += pOptions
    }
    cnt.println()
}


