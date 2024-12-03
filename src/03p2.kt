fun main() {
    val input = readInput("03")

    val pat = Regex("""mul\((\d{1,3}),(\d{1,3})\)|do\(\)|don't\(\)""")

    var result = 0
    var isOn = true

    for (line in input) {
        pat.findAll(line).forEach {
            when (it.value) {
                "do()" -> isOn = true
                "don't()" -> isOn = false
                else -> {
                    if (isOn) {
                        val (a, b) = it.destructured
                        result += a.toInt() * b.toInt()
                    }
                }
            }
        }
    }
    result.println()
}
