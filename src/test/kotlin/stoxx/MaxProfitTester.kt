package stoxx

import kotlin.test.*

class MaxProfitTester {
    @Test fun noTrade() {
        assertEquals(null, findMaxProfit(listOf(1)))
    }

    @Test fun twoTrade() {
        assertEquals(Pair(1, 2), findMaxProfit(listOf(1,2)))
    }

    @Test fun gettingBetter() {
        assertEquals(Pair(1,3), findMaxProfit(listOf(1,2,3)))
    }

    @Test fun bestIsYetToCome() {
        assertEquals(Pair(1,4), findMaxProfit(listOf(2,3,1,4)))
    }

    @Test fun earlierWasBetter() {
        assertEquals(Pair(2,4), findMaxProfit(listOf(2,4,1,2)))
    }
}
