package rollingdice

import kotlin.test.*

class RollingDiceTester {
    @Test fun singleDice() {
        val result = repeatedDices(6u, 1u)
        assertEquals(6, result.size)
        val f = result[1u]
        assertEquals(f, result[2u])
        assertEquals(f, result[3u])
        assertEquals(f, result[4u])
        assertEquals(f, result[5u])
        assertEquals(f, result[6u])
        assertEquals(1.0, f?.times(6.0))
    }

    @Test fun twoDice() {
        val result = repeatedDices(12u, 2u)
        assertEquals(23, result.size)
        val f = result[2u]
        assertEquals(1.0/144.0, f)
        assertEquals(f, result[24u])
        assertEquals(f!!*2, result[3u])
        assertEquals(f*2, result[23u])
        assertEquals(f*3, result[4u])
        assertEquals(f*3, result[22u])
        assertEquals(f*4, result[5u])
        assertEquals(f*4, result[21u])
        assertEquals(f*5, result[6u])
        assertEquals(f*5, result[20u])
        assertEquals(f*6, result[7u])
        assertEquals(f*6, result[19u])
        assertEquals(f*7, result[8u])
        assertEquals(f*7, result[18u])
        assertEquals(f*8, result[9u])
        assertEquals(f*8, result[17u])
        assertEquals(f*9, result[10u])
        assertEquals(f*9, result[16u])
        assertEquals(f*10, result[11u])
        assertEquals(f*10, result[15u])
        assertEquals(f*11, result[12u])
        assertEquals(f*11, result[14u])
        assertEquals(f*12, result[13u])
    }

    @Test fun d4AndD6() {
        val result = multiDices(listOf(Pair(4u,1u), Pair(6u, 1u)))
        val f = result[2u]
        assertEquals(1.0/24.0, f)
        assertEquals(2*f!!, result[3u])
        assertEquals(3*f, result[4u])
        assertEquals(4*f, result[5u])
        assertEquals(4*f, result[6u])
        assertEquals(4*f, result[7u])
        assertEquals(3*f, result[8u])
        assertEquals(2*f, result[9u])
        assertEquals(f, result[10u])
    }
}
