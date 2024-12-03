import java.io.File


fun readInput(name: String) = File("inp/$name.txt").readLines()

fun Any?.println() = println(this)
