import arrow.core.MemoizedDeepRecursiveFunction


fun main() {
    val input = readInput("11")
    val stones = input[0].split(" ").map { it.toLong() }

    // https://arrow-kt.io/learn/collections-functions/recursive/#memoized-recursive-functions
    val memoCountStones = MemoizedDeepRecursiveFunction<Pair<Long, Int>, Long> { pair ->
        val (stone, steps) = pair
        if (steps == 0) {
            1L
        } else if (stone == 0L) {
            callRecursive(Pair(1L, steps - 1))
        } else if (stone.toString().length % 2 == 0) {
            val left = stone.toString().substring(0, stone.toString().length / 2).toLong()
            val right = stone.toString().substring(stone.toString().length / 2).toLong()
            callRecursive(Pair(left, steps - 1)) + callRecursive(Pair(right, steps - 1))
        } else {
            callRecursive(Pair(stone * 2024L, steps - 1))
        }
    }

    var res = 0L
    for (stone in stones) {
        val key = Pair(stone, 75)
        val thisRes = memoCountStones(key)
        println("[$stone] -> $thisRes")
        res += thisRes
    }
    res.println()
}
