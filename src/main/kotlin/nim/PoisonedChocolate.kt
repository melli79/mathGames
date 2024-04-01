package nim

data class Grid(val data :Array<BooleanArray>) {
    override fun hashCode() = data.contentDeepHashCode()
    override fun equals(other: Any?): Boolean {
        if (other !is Grid)
            return false
        return data.contentDeepEquals(other.data)
    }

    val size = data.size
    val indices = data.indices
    fun clone(): Grid = Grid(data.map { it.clone() }.toTypedArray())
    override fun toString() = data.joinToString("\n") { it.joinToString(" ") }

    fun sliceArray(intRange :IntRange) = data.sliceArray(intRange)
    operator fun get(index :Int) = data[index]
    operator fun get(index :UInt) = data[index.toInt()]
    fun any(lambda :(BooleanArray)->Boolean) = data.any(lambda)
}

fun List<BooleanArray>.toGrid() = Grid(toTypedArray())

private fun Grid.clear(r0 :UInt, c0 :UInt) :Grid? {
    if (c0==0u)
        return if (r0>0u && this.sliceArray(r0.toInt()..< size).any { it.any() })
            (0u..< r0).map { this[it].clone() }.toGrid()
        else
            null
    if (r0==0u)
        return if (c0>0u && this.any {
                    it.sliceArray(c0.toInt()..< it.size).any()
                })
            (0..< size).map { this[it].sliceArray(0..< c0.toInt()).clone() }.toGrid()
        else
            null
    val candidate = clone()
    var cleared = false
    for (r in r0.toInt()..< size) {
        val row = candidate[r]
        for (c in c0.toInt()..< row.size) {
            if (row[c]) {
                cleared = true
                row[c] = false
            }
        }
    }
    if (cleared)
        return candidate
    return null
}

object PoisonedChocolate {

    private val canWin = mutableSetOf<Grid>()
    private val hasLost = mutableSetOf(listOf(booleanArrayOf(true)).toGrid())

    fun canWin(r :UInt, c :UInt) = r==0u || c==0u || canWin((0u..< r).map {
        BooleanArray(c.toInt()) { true }
    }.toGrid())

    internal fun canWin(grid :Grid) :Boolean {
        if (canWin.contains(grid))
            return true
        if (hasLost.contains(grid))
            return false
        if ((grid.indices).any { r -> (grid[r].indices).any { c ->
            val newGrid = grid.clear(r.toUInt(), c.toUInt())
                    newGrid!=null && !canWin(newGrid)
        } }) {
            canWin.add(grid)
            return true
        } else {
            hasLost.add(grid)
            return false
        }
    }
}
