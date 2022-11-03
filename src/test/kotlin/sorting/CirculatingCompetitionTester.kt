package sorting

import kotlin.test.*

class CirculatingCompetitionTester {
    @Test fun empty() {
        assertEquals(emptyList(), compete(emptyList(), 3u))
    }

    @Test fun singlePlayer() {
        assertEquals(listOf(10), compete(listOf(1), 10u))
    }

    @Test fun twoPlayers() {
        assertEquals(listOf(10, -10), compete(listOf(1, 2), 10u))
    }

    @Test fun threePlayers() {
        assertEquals(listOf(-5,-4,9), compete(listOf(3,2,1), 10u))
    }

    @Test fun fourPlayers() {
        assertEquals(listOf(-4,-2,-2,8), compete(listOf(4,3,2,1), 10u))
    }
}
