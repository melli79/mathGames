package geometry

typealias Grid = Array<ByteArray>
typealias Cell = Byte

const val Empty = 0.toByte()
const val Forbidden = (-1).toByte()
const val Gift = 1.toByte()
fun Grid.show() = joinToString("\n") { it.joinToString("") {
    when (it) {
        Empty -> "_"
        Gift -> "*"
        Forbidden -> "X"
        else -> "?"
    }
} }

fun Grid.isReachable(start :Pair<Int, Int>, end :Pair<Int, Int>) :Boolean {
    if (start==end)
        return true
    if (start.first<0||start.second<0 || start.first>=this.size || start.second>=this[0].size)
        return false
    val visited  = this.map { it.clone() }.toTypedArray()
    val opens = mutableListOf(start)
    while (opens.isNotEmpty()) {
        val current = opens.removeFirst()
        if (visited.try4Directions(current, end, opens))
            return true
    }
    return false
}

private fun Array<ByteArray>.try4Directions(
    current :Pair<Int, Int>,
    end :Pair<Int, Int>,
    opens :MutableList<Pair<Int, Int>>
) :Boolean {
    if (current.first+1 < size) {
        val next = Pair(current.first+1, current.second)
        if (next == end)
            return true
        if (this[next.first][next.second] <= 0) {
            this[next.first][next.second] = 2
            opens.add(next)
        }
    }
    if (current.second+1 < this[0].size) {
        val next = Pair(current.first, current.second+1)
        if (next == end)
            return true
        if (this[next.first][next.second] <= 0) {
            this[next.first][next.second] = 2
            opens.add(next)
        }
    }
    if (current.first > 0) {
        val next = Pair(current.first-1, current.second)
        if (next == end)
            return true
        if (this[next.first][next.second] <= 0) {
            this[next.first][next.second] = 2
            opens.add(next)
        }
    }
    if (current.second > 0) {
        val next = Pair(current.first, current.second-1)
        if (next == end)
            return true
        if (this[next.first][next.second] <= 0) {
            this[next.first][next.second] = 2
            opens.add(next)
        }
    }
    return false
}
