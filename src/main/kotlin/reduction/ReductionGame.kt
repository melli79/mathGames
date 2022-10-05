package reduction

import common.math.factor
import common.math.isqrt
import kotlin.math.min

object ReductionGame {
    const val LIMIT = 1.shl(28)

    private val values = mutableListOf<Short>()
    private val moves = mutableListOf<Move>()

    fun predictBestMove(n :ULong) :Move {
        if (n < moves.size.toULong())
            return moves[n.toInt()]
        return computeMove(n).second
    }

    fun predictMinimalValue(n :ULong) :Short {
        if (n<values.size.toULong())
            return values[n.toInt()]
        if (values.size<3) {
            values.add(2); moves.add(Move.Add(1)) // 0
            values.add(1); moves.add(Move.Add(1)) // 1
            values.add(0); moves.add(Move.Divide(1u)) // 2
        }
        var lastValue = values.last()
        for (k in values.size until min(LIMIT, n.toInt()+3)) {
            val (value, move) = computeMove(k.toULong())
            values.add(value);  moves.add(move)
            if (value<lastValue) {
                values[k-1] = (value+1).toShort();  moves[k-1] = Move.Add(1)
                if (value+1 < values[k-2]) {
                    values[k-2] = (value+2).toShort();  moves[k-2] = Move.Add(1)
                    if (value+2 < values[k-3]) {
                        values[k-3] = (value+3).toShort();  moves[k-3] = Move.Add(1)
                    }
                }
            }
            lastValue = value
        }
        if (n< values.size.toULong())
            return values[n.toInt()]
        return computeMove(n).first
    }

    private fun computeMove(n: ULong): Pair<Short, Move> {
        val smallFactors :List<UInt> = factors(n)
        if (smallFactors.isEmpty()) {
            return Pair((predictMinimalValue(n-1uL)+1).toShort(), Move.Add(-1))
        } else {
            var best = Pair<Short, Move>((predictMinimalValue(n-1uL)+1).toShort(), Move.Add(-1))
            for (f in smallFactors) if (f>1uL) {
                val value = predictMinimalValue(n/f)
                if (value<best.first) {
                    best = Pair(value, Move.Divide(f))
                }
            }
            return best
        }
    }

    internal fun factors(n: ULong): List<UInt> {
        val limit = isqrt(n)
        if (limit<2u)
            return emptyList()
        val pfs = factor(n)
        return combine(pfs.entries.toList().reversedIterator(), limit)
    }

    internal fun combine(iterator: Iterator<Map.Entry<ULong, UByte>>, limit :UInt) :List<UInt> {
        if (!iterator.hasNext())
            return listOf(1u)
        val (p, eMax) = iterator.next()
        val preFactors = combine(iterator, limit)
        if (p>limit)
            return preFactors
        val q = p.toUInt()
        val result = mutableListOf<UInt>()
        var f = 1u
        for ( e in 0..eMax.toInt()) {
            result.addAll(preFactors.map { p -> p*f }.filter { f -> f<=limit })
            f *= q
        }
        return result.sorted()
    }

    sealed class Move(val value :Short) {
        abstract operator fun invoke(n :ULong) :ULong

        data class Add(val d :Short) : Move(1) {
            override fun toString() = when {
                d < 0 -> "-"
                d > 0 -> "+"
                else -> "0"
            }

            override operator fun invoke(n: ULong) = when {
                d < 0 -> n-1uL
                d > 0 -> n+1uL
                else -> n
            }
        }

        data class Divide(val d :UInt) : Move(0) {
            override fun toString() = "/$d"
            override fun invoke(n: ULong) = n/d
        }
    }
}

fun <E> List<E>.reversedIterator() :Iterator<E> = ReverseIterator(this)

// Warning: this is missing all modification checks
internal class ReverseIterator<E>(private val list :List<E>) :Iterator<E> {
    private var i = list.size -1

    override fun hasNext() = i>=0
    override fun next() = list[i--]
}
