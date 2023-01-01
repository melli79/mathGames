package memory

import common.math.Rational
import kotlin.test.*

class MemoryStrategyTester {
    @Test fun trivialGame() {
        assertEquals(emptyMap(), optimize(0u))
    }

    @Test fun firstGames() {
        assertEquals(mapOf(
                Pair(Pair(1u, 0u), Pair(Move.NN, Rational.ONE)),
                Pair(Pair(1u, 1u), Pair(Move.NO, Rational.ONE))),
            optimize(1u))
    }

    @Test fun twoPairs() {
        val result = optimize(2u)
        assertEquals(Pair(Move.NO, Rational.TWO), result[Pair(2u, 2u)])
        assertEquals(Pair(Move.NO, Rational.of(1L, 3L)*(Rational.ONE+result[Pair(1u, 1u)]!!.second)
                                    - Rational.of(2L, 3L)*result[Pair(2u, 2u)]!!.second),
            result[Pair(2u,1u)])
        assertEquals(Pair(Move.NN, Rational.of(1L,3L)* Rational.TWO - Rational.of(2L, 3L)* Rational.TWO),
            result[Pair(2u, 0u)])
        assertEquals(5, result.size)
    }

    @Test fun threePairs() {
        val result = optimize(3u)
        assertEquals(Pair(Move.NO, Rational.of(3L)), result[Pair(3u, 3u)])
        assertEquals(Pair(Move.NO, Rational.of(1L,3L)), result[Pair(3u, 2u)])
        assertEquals(5+4, result.size)
    }
}
