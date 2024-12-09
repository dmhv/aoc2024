fun main() {
    val input = readInput("09")
    val blocks = parseInput(input[0])

    // a Block denotes a consecutive disk region filled with Block.content,
    // from disk address Block.start to disk address Block.end, including both ends
    // Block.content is either a non-negative integer or -1, which denotes free space

    val usedBlocks = blocks.filter { it.content != -1 }
    for (b in usedBlocks.reversed()) {
        val bId = blocks.indexOf(b)

        val availableBlocks =
            blocks.filter { it.content == -1 && it.end < b.start && it.len() >= b.len() }

        if (availableBlocks.isNotEmpty()) {
            val f = availableBlocks.first()
            val fId = blocks.indexOf(f)

            // replace this block with free space
            blocks.removeAt(bId)
            blocks.add(bId, Block(b.start, b.end, -1))

            // replace the free block with this block + any leftover free space
            blocks.removeAt(fId)
            blocks.add(fId, Block(f.start, f.start + b.len(), b.content))
            if (f.len() > b.len()) blocks.add(fId + 1, Block(f.start + b.len() + 1, f.end, -1))
        }
    }

    blocks.filter { it.content != -1 }.sumOf { b ->
        run {
            (b.start..b.end).sumOf { it * b.content.toLong() }
        }
    }.println()
}

private fun parseInput(input: String): MutableList<Block> {
    val out = mutableListOf<Block>()
    var ptr = 0
    for ((i, c) in input.withIndex()) {
        val len = c.digitToInt()
        if (len == 0) continue
        val content = if (i % 2 == 0) i / 2 else -1
        out.add(Block(ptr, ptr + len - 1, content))
        ptr += len
    }
    return out
}

private data class Block(val start: Int, val end: Int, val content: Int)

private fun Block.len() = end - start

private fun Block.toStr() =
    List(this.end - this.start + 1) { if (this.content != -1) this.content else "." }.joinToString("")
