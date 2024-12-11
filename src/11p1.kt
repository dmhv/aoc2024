fun main() {
    val input = readInput("11")
    var stones = input[0].split(" ").map { it.toULong() }

    for (i in 1..25) {
        val newStones = mutableListOf<ULong>()
        for (stone in stones) {
            if (stone == 0UL) {
                newStones.add(1UL)
            } else if (stone.toString().length % 2 == 0) {
                val left = stone.toString().substring(0, stone.toString().length / 2).toULong()
                val right = stone.toString().substring(stone.toString().length / 2).toULong()
                newStones.add(left)
                newStones.add(right)
            } else {
                newStones.add(stone * 2024u)
            }
        }
        stones = newStones
        println("[$i]: ${stones.size}")
    }
    println(stones.size)
}
