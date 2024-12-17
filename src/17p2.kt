import java.util.*
import kotlin.Comparator

private var A = 0L
private var B = 0L
private var C = 0L

fun main() {
    val input = readInput("17")
    val registerStrings = input.filter { it.startsWith("Register") }

    A = registerStrings[0].removePrefix("Register A: ").toLong()
    B = registerStrings[1].removePrefix("Register B: ").toLong()
    C = registerStrings[2].removePrefix("Register C: ").toLong()

    val program = input.filter { it.startsWith("Program") }[0].removePrefix("Program: ")
        .split(",").map { it.toLong() }

    val compareBySteps: Comparator<Pair<List<Int>, Long>> = compareBy { it.second }
    val queue = PriorityQueue(compareBySteps)
    queue.add(listOf<Int>() to 0)

    outer@ while (true) {
        var weThereYet = false
        val (prefixList, _) = queue.remove()
        for (v in (0..7)) {
            val foo = prefixList.withIndex().sumOf { (i, v) -> v * 8L.pow(i + 1L) }
            A = foo + v
            val out = run(program)
            val first = out[0]
            if (first == program.reversed()[prefixList.size]) {
                if (prefixList.size == program.size - 1) {
                    weThereYet = true
                }
                println("$v -> ${out.joinToString(",")}")
                val newPrefixList = mutableListOf(v)
                newPrefixList.addAll(prefixList)
                println("Adding ${newPrefixList to foo + v}")
                queue.add(newPrefixList to foo + v)
            }
        }
        if (weThereYet) {
            break@outer
        }
    }

    queue.filter { it.first.size == 16 }.minBy { it.second }.println()
}

private fun run(program: List<Long>): MutableList<Long> {
    val out = mutableListOf<Long>()
    var ptr = 0
    while (ptr < program.size) {
        val op = program[ptr]
        val operand = program[ptr + 1]
        when (op) {
            0L -> {
                A /= 2L.pow(operandValue(operand) % 8L)
                ptr += 2
            }

            1L -> {
                B = B xor operand
                ptr += 2
            }

            2L -> {
                B = operandValue(operand) % 8
                ptr += 2
            }

            3L -> {
                if (A > 0) {
                    ptr = operand.toInt()
                } else {
                    ptr += 2
                }
            }

            4L -> {
                B = B xor C
                ptr += 2
            }

            5L -> {
                out.add(operandValue(operand) % 8)
                ptr += 2
            }

            6L -> {
                B = A / 2L.pow(operandValue(operand) % 8)
                ptr += 2
            }

            7L -> {
                C = A / 2L.pow(operandValue(operand) % 8)
                ptr += 2
            }
        }
    }
    return out
}

private fun Long.pow(exponent: Long): Long {
    val base = this
    var exp = exponent
    var res = 1L

    while (exp != 0L) {
        res *= base
        --exp
    }
    return res
}

private fun operandValue(operand: Long): Long {
    return when (operand) {
        0L -> 0L
        1L -> 1L
        2L -> 2L
        3L -> 3L
        4L -> A
        5L -> B
        6L -> C
        else -> throw IllegalArgumentException("Invalid operand $operand")
    }
}