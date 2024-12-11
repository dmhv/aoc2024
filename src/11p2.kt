fun main() {
    val input = readInput("11")
    val stones = input[0].split(" ").map { it.toLong() }

    val cache = mutableMapOf<Pair<Long, Int>, Long>()
    fun countStones(stone: Long, steps: Int): Long = cache.getOrPut(Pair(stone, steps)) {
        if (steps == 0) {
            1L
        } else if (stone == 0L) {
            countStones(1L, steps - 1)
        } else if (stone.toString().length % 2 == 0) {
            val left = stone.toString().substring(0, stone.toString().length / 2).toLong()
            val right = stone.toString().substring(stone.toString().length / 2).toLong()
            countStones(left, steps - 1) + countStones(right, steps - 1)
        } else {
            countStones(stone * 2024L, steps - 1)
        }
    }

    var res = 0L
    for (num in stones) {
        val thisRes = countStones(num, 75)
        println("[$num] -> $thisRes")
        res += thisRes
    }
    res.println()
}
