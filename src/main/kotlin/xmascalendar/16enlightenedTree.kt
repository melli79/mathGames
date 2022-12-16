package xmascalendar

import xmasaoc.Matrix

private val tree = Matrix(listOf(
    "...OO...".toList(),
    "...OO...".toList(),
    "..OOOO..".toList(),
    "..OOOO..".toList(),
    ".OOOOOO.".toList(),
    ".OOOOOO.".toList(),
    "OOOOOOOO".toList(),
    "OOOOOOOO".toList()
), '.')

typealias Candle = Char

private const val EMPTY = '.'
private const val CANDLE = 'O'
private const val LIT = 'X'
private const val COLIT = 'Y'

fun main() {
    val brightTree = tree.copy()
    brightTree.enlighten(Pair(0,3))
    brightTree.enlighten(Pair(1,4))
    brightTree.enlighten(Pair(2,2))
    brightTree.enlighten(Pair(3,5))
    brightTree.enlighten(Pair(4,1))
    brightTree.enlighten(Pair(5,6))
    brightTree.enlighten(Pair(6,0))
    brightTree.enlighten(Pair(7,7))
    println(brightTree)
    println("It is possible with enlightening 8 candles.")
    val c40_7 = 40L*39*38*37*36*35*34/2/3/4/5/6/7
    println("You need to check ${c40_7} combinations to rule out any smaller solution.")
}

private fun Matrix<Candle>.enlighten(candle :Pair<Int, Int>) :Matrix<Candle> {
    if (get(candle.first,candle.second)==CANDLE)
        set(candle.first,candle.second, LIT)
    coEnlighten(getOffNeighbors(candle).toMutableList())
    return this
}

private fun Matrix<Candle>.coEnlighten(opens :MutableList<Pair<Int, Int>>) {
    while (opens.isNotEmpty()) {
        val candle = opens.removeAt(0)
        val ignitedNeighbors = getNeighbors(candle).count { get(it.first, it.second)!=CANDLE }
        if (ignitedNeighbors >= 2) {
            set(candle.first, candle.second, COLIT)
            opens.addAll(getOffNeighbors(candle))
        }
    }
}

fun Matrix<Candle>.getNeighbors(candle :Pair<Int, Int>) =
    listOf(Pair(candle.first-1, candle.second), Pair(candle.first, candle.second-1),
            Pair(candle.first, candle.second+1), Pair(candle.first+1, candle.second))
        .filter {
            it.first in 0 until numRows.toInt() && it.second in 0 until numCols.toInt() &&
                    get(candle.first, it.second)!=EMPTY
        }

fun Matrix<Candle>.getOffNeighbors(candle :Pair<Int, Int>) =
    getNeighbors(candle).filter { get(it.first, it.second)==CANDLE }
