package dynamic

import kotlin.test.*

class KnapsackTester {
    @Test fun dontOverload() {
        val result = computeKnapsack(listOf(Pair(1u,2u), Pair(3u,4u)), 0u)

        assertEquals(emptySet(), result)
    }

    @Test fun takingFreeItems() {
        val result = computeKnapsack(listOf(Pair(1u, 0u)), 0u)

        assertEquals(setOf(0), result)
    }

    @Test fun bestItem() {
        val result = computeKnapsack(listOf(Pair(2u, 1u), Pair(3u, 1u)), 1u)

        assertEquals(setOf(1), result)
    }

    @Test fun fullestKnapsack() {
        val result = computeKnapsack(listOf(Pair(3u,3u),Pair(2u,2u),Pair(2u,2u)), 4u)

        assertEquals(setOf(1,2), result)
    }
}
