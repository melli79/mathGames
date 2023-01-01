package memory

import common.math.Rational

data class Game(val pairs :UInt, val opens :UInt) {
    override fun toString() = "Game(p=$pairs, o=$opens)"
}

enum class Move {
    NN, NO, ON, OO
}

fun optimize(maxPairs :UInt) :Map<Pair<UInt, UInt>, Pair<Move, Rational>> {
    if (maxPairs<1u)
        return emptyMap()
    val result = mutableMapOf(Pair(Pair<UInt, UInt>(1u, 0u), Pair(Move.NN, Rational.ONE)),
        Pair(Pair(1u, 1u), Pair(Move.NO, Rational.ONE)))
    for (pairs in 2u..maxPairs) {
        for (opens in (0u..pairs).reversed()) {
            val options = mutableListOf<Pair<Move, Rational>>()
            if (opens<pairs) {
                val hiddens = 2*pairs.toLong() -opens.toLong()
                options.add(Pair(Move.NN,
                    Rational.of(2*(pairs-opens).toLong()-2*sqr((pairs-opens).toInt()), hiddens*(hiddens-1L))
                            *(Rational.ONE +result[Pair(pairs-1u, opens)]!!.second)
                    - Rational.of(opens.toLong()*(opens-1u).toLong(), hiddens*(hiddens-1L))
                            *(Rational.TWO +(result[Pair(pairs-2u,opens-2u)]?.second ?: Rational.ZERO))
                    - Rational.of(4*opens.toLong()*(pairs-opens).toLong(), hiddens*(hiddens-1L))
                            *(Rational.ONE +result[Pair(pairs-1u, opens)]!!.second)))

                if (opens>0u)
                    options.add(Pair(Move.NO,
                        Rational.of(-2*(pairs-opens).toLong(), hiddens)
                                *result[Pair(pairs, opens+1u)]!!.second
                        + Rational.of(opens.toLong(), hiddens)
                                *(Rational.ONE +result[Pair(pairs-1u, opens-1u)]!!.second)))
            } else
                options.add(Pair(Move.NO, Rational.of(pairs.toLong())))
            if (opens>1u)
                options.add(Pair(Move.OO, Rational.ZERO))
            result[Pair(pairs, opens)] = options.maxBy { p -> p.second }
        }
    }
    return result
}

fun sqr(x :Int) = x.toLong()*x.toLong()
