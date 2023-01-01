package trivia

import kotlin.test.*

class DistributionTester {
    @Test fun emptyWork() {
        val result = distribute(emptyList(), emptyList())
        assertEquals(Pair(emptyList(), emptyList()), result)
    }

    @Test fun singleTask() {
        val result = distribute(listOf(1u,2u,3u), listOf(2u))
        assertEquals(Pair(listOf(Pair(1,0)), emptyList()), result)
    }

    @Test fun rejectsTooLargeTask() {
        val result = distribute(listOf(1u,2u), listOf(3u))
        assertEquals(listOf(0), result.second)
    }

    @Test fun groups() {
        val result = distribute(listOf(3u,4u,5u), listOf(1u,2u,3u))
        assertNotNull(result.first.firstOrNull { p -> p.second == 0 })
        assertNotNull(result.first.firstOrNull { p -> p.second == 1 })
        assertNotNull(result.first.firstOrNull { p -> p.second == 2 })
        assertEquals(emptyList(), result.second)
    }
}
