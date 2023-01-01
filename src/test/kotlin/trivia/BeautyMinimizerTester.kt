package trivia

import kotlin.test.*

class BeautyMinimizerTester {
    @Test fun minOfPair() {
        val result = minimizeBeauty(listOf(1, 2))
        assertEquals(0, computeBeauty(result.first, result.second))
    }

    @Test fun minOfTriple() {
        val result = minimizeBeauty(listOf(1, 2, 4))
        assertEquals(1, computeBeauty(result.first, result.second))
    }
}
