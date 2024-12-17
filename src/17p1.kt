private var A = 0
private var B = 0
private var C = 0

fun main() {
    val input = readInput("17")
    val registerStrings = input.filter { it.startsWith("Register") }

    A = registerStrings[0].removePrefix("Register A: ").toInt()
    B = registerStrings[1].removePrefix("Register B: ").toInt()
    C = registerStrings[2].removePrefix("Register C: ").toInt()

    val program = input.filter { it.startsWith("Program") }[0].removePrefix("Program: ")
        .split(",").map { it.toInt() }

    val out = mutableListOf<Int>()
    var ptr = 0
    while (ptr < program.size) {
        val op = program[ptr]
        val operand = program[ptr + 1]
        when (op) {
            0 -> {
                A /= 2.pow(operandValue(operand) % 8)
                ptr += 2
            }

            1 -> {
                B = B xor operand
                ptr += 2
            }

            2 -> {
                B = operandValue(operand) % 8
                ptr += 2
            }

            3 -> {
                if (A > 0) {
                    ptr = operand
                } else {
                    ptr += 2
                }
            }

            4 -> {
                B = B xor C
                ptr += 2
            }

            5 -> {
                out.add(operandValue(operand) % 8)
                ptr += 2
            }

            6 -> {
                B = A / 2.pow(operandValue(operand) % 8)
                ptr += 2
            }

            7 -> {
                C = A / 2.pow(operandValue(operand) % 8)
                ptr += 2
            }
        }
    }
    out.joinToString(",").println()
}

private fun Int.pow(exponent: Int): Int {
    val base = this
    var exp = exponent
    var res = 1

    while (exp != 0) {
        res *= base
        --exp
    }
    return res
}

private fun operandValue(operand: Int): Int {
    return when (operand) {
        0 -> 0
        1 -> 1
        2 -> 2
        3 -> 3
        4 -> A
        5 -> B
        6 -> C
        else -> throw IllegalArgumentException("Invalid operand $operand")
    }
}