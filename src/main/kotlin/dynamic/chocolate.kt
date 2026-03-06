package dynamic

import common.removeFrom
import dynamic.Partition.Companion.canContain

/**
 * Chocolate bar
 *
 * given a chocolate bar of dimensions w x h, we wish to break it into
 * pieces w_i x h_i for i= 1, ..., n.
 *
 * It is only allowed to break any chocolate bar
 * either between 2 rows 1\le r <h or between 2 columns 1\le c < w.
 *
 * A breakup consists of choosing such a row r or column c and a partition of the remaining pieces into S_1 = { w_{i_1} x h_{i_1}, ..., w_{i_k} x h_{i_k} } with S = S_1 u S_2.
 *
 * Obvious constraints are 1\le r < h, 1\le c < w, 1\le w_{i_j}\le w^{(k)}, 1\le h_{i)j}\le h^{(k)}, \sum_j s_{i_j} \le s^{(k)}.
 *
 */

// import kotlin.random.Random

// val random = Random(System.currentTimeMillis())

typealias Piece = Pair<UInt, UInt>
typealias Bar = Pair<UInt, UInt>

class Partition(val remainingBars :MutableList<Bar>, val remainingPieces :MutableList<List<Piece>>, val result :MutableList<Piece>) {
    companion object {
        fun Bar.canContain(pieces :Collection<Piece>) =
            pieces.all { it.first <= first && it.second <= second || it.second <= first && it.first <= second } &&
                    pieces.sumOf { it.first*it.second } <= first*second

        fun Collection<Piece>.gather(bar :Bar) :List<Piece> {
            val candidates = this.sortedWith(compareBy({ -it.first.toInt() }, { -it.second.toInt() }))
            val result = mutableListOf<Piece>()
            var remainingSize = bar.first*bar.second
            for (piece in candidates) if ((piece.first <= bar.first && piece.second <= bar.second ||
                        piece.second <= bar.first && piece.first <= bar.second) &&
                    piece.first*piece.second <= remainingSize) {
                result.add(piece)
                remainingSize -= piece.first*piece.second
                if (remainingSize < 1u) break
            }
            return result
        }
    }

    fun partition() :Boolean {
        if (remainingBars.isNotEmpty() && remainingPieces.isNotEmpty()) {
            val start = result.size
            val bar = remainingBars.removeFirst()
            val pieces = remainingPieces.removeFirst()
            if (pieces.size <= 1) {
                print(pieces.firstOrNull()); System.out.flush()
                if (pieces.isNotEmpty()) result.add(pieces.first())
                if (partition())
                    return true
                result.removeFrom(start)
            } else {
                for (c in 1u..<bar.first) {
                    val b1 = Bar(c, bar.second)
                    val p1s = pieces.gather(b1)
                    val p2s = pieces.toMutableList()
                    p2s.removeAll(p1s)
                    if (p1s.isEmpty() || p2s.isEmpty()) continue
                    val b2 = Bar(bar.first - c, bar.second)
                    if (b2.canContain(p2s)) {
                        remainingBars.addAll(listOf(b1, b2))
                        remainingPieces.addAll(listOf(p1s, p2s))
                        if (partition())
                            return true
                        remainingBars.removeLast(); remainingPieces.removeLast()
                        remainingBars.removeLast(); remainingPieces.removeLast()
                        result.removeFrom(start)
                    }
                }
                if (bar.first != bar.second) for (r in 1u..<bar.second) {
                    val b1 = Bar(bar.first, r)
                    val p1s = pieces.gather(b1)
                    val p2s = pieces.toMutableList()
                    p2s.removeAll(p1s)
                    if (p1s.isEmpty() || p2s.isEmpty()) continue
                    val b2 = Bar(bar.first, bar.second - r)
                    if (b2.canContain(p2s)) {
                        remainingBars.addAll(listOf(b1, b2))
                        remainingPieces.addAll(listOf(p1s, p2s))
                        if (partition())
                            return true
                        remainingBars.removeLast(); remainingPieces.removeLast()
                        remainingBars.removeLast(); remainingPieces.removeLast()
                        result.removeFrom(start)
                    }
                }
            }
            remainingBars.add(0, bar)
            remainingPieces.add(0, pieces)
        } else println()
        return remainingPieces.isEmpty()
    }
}

fun Bar.partition(pieces :Collection<Piece>) :List<Piece>? {
    if (!canContain(pieces)) return null
    val remainingPieces = mutableListOf(pieces.toList())
    val remainingBars = mutableListOf(this)
    val p = Partition(remainingBars, remainingPieces,mutableListOf())
    if (p.partition())
        return p.result
    return null
}
