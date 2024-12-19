fun main() {
    val input = readInput("19")
    val designs = input[0].split(", ").toSet()
    val patterns = buildSet {
        input.subList(2, input.size).forEach { line -> this.add(line) }
    }
    val possiblePatterns = patterns.filter { isPossible(it, designs) }
    possiblePatterns.size.println()
}

private fun isPossible(pattern: String, designs: Set<String>): Boolean {
    val prefixes = designs.filter { pattern.startsWith(it) }
    if (prefixes.isEmpty()) return false
    if (prefixes.any { pattern == it }) return true
    return prefixes.any { isPossible(pattern.removePrefix(it), designs) }
}

