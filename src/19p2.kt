fun main() {
    val input = readInput("19")
    val designs = input[0].split(", ").toSet()
    val patterns = buildSet {
        input.subList(2, input.size).forEach { line -> this.add(line) }
    }

    val cache = mutableMapOf<String, Long>()
    fun numPossibilities(pattern: String): Long = cache.getOrPut(pattern) {
        val prefixes = designs.filter { pattern.startsWith(it) }
        if (prefixes.isEmpty()) {
            0L
        } else {
            var out = 0L
            for (pr in prefixes) {
                out += if (pattern == pr) {
                    1L
                } else {
                    numPossibilities(pattern.removePrefix(pr))
                }
            }
            out
        }
    }

    patterns.sumOf { numPossibilities(it) }.println()
}
