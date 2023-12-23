package nim

/**
 * You have $n$ heaps of height 0..< (n-1) and can reduce 2 heaps at a time by 1 each.
 * You win if you can reduce all heaps to height 0, otherwise you lose.
 */
object DiagonalStart {
    private val winning = mutableSetOf(0u, 1u)
    private val losing = mutableSetOf(2u,3u)

    fun canWin(n :UInt) :Boolean {
        if (n in winning)
            return true
        if (n in losing)
            return false
        // move the highest 2 heaps down to (1,0) and then combine with a possibly remaining stone.
        val result = !canWin(n-2u)
        if (result)
            winning.add(n)
        else
            losing.add(n)
        return result
    }
}
