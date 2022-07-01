package stoxx

import kotlin.test.*

class HfTradeTester {
    @Test fun noTrade() {
        assertEquals(emptyList(), hfTrade(listOf(1)))
    }

    @Test fun singleTrade() {
        assertEquals(listOf(Pair(1,2)), hfTrade(listOf(1,2)))
    }

    @Test fun prolongedTrade() {
        assertEquals(listOf(Pair(1,3)), hfTrade(listOf(1,2,3)))
    }

    @Test fun startLow() {
        assertEquals(listOf(Pair(1,3)), hfTrade(listOf(2,1,3)))
    }

    @Test fun interSell() {
        assertEquals(listOf(Pair(1,2), Pair(1,3)), hfTrade(listOf(1,2,1,3)))
    }
}
