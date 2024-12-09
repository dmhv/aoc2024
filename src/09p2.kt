fun main() {
    val input = readInput("09")
    val blocks = parseInput(input[0])

    // a Block denotes a consecutive disk region filled with Block.content,
    // from disk address Block.start to disk address Block.end, including both ends
    // Block.content is either a non-negative integer or -1, which denotes free space

    val usedBlocks = blocks.filter { it.content != -1 }
    for (b in usedBlocks.reversed()) {
        val bId = blocks.indexOf(b)
        val bStart = b.start
        val bEnd = b.end
        val bLen = bEnd - bStart
        val bContent = b.content

        val availableBlocks =
            blocks.filter { it.content == -1 && it.end < bStart && (it.end - it.start) >= bLen }

        if (availableBlocks.isNotEmpty()) {
            val f = availableBlocks.first()
            val fId = blocks.indexOf(f)
            val fStart = f.start
            val fEnd = f.end
            val fLen = fEnd - fStart

            // replace this block with free space
            blocks.removeAt(bId)
            blocks.add(bId, Block(bStart, bEnd, -1))

            // replace the free block with this block + any leftover free space
            blocks.removeAt(fId)
            blocks.add(fId, Block(fStart, fStart + bLen, bContent))
            if (fLen > bLen) blocks.add(fId + 1, Block(fStart + bLen + 1, fEnd, -1))
        }
    }

    var checkSum = 0L
    var i = 0
    for (b in blocks) {
        if (b.content == -1) {
            for (foo in b.start..b.end) i++
        } else {
            for (foo in b.start..b.end) {
                checkSum += b.content * i
                i++
            }
        }
    }
    checkSum.println()
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

private fun Block.toStr() =
    List(this.end - this.start + 1) { if (this.content != -1) this.content else "." }.joinToString("")
