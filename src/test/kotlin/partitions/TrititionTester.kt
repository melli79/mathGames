package partitions

import kotlin.test.*

class TrititionTester {
    @Test fun impossible() {
        assertEquals(emptySet(), trititions(emptyList(), 3))
    }

    @Test fun uniqueSolution() {
        assertEquals(setOf(Triple(1,1,1)), trititions(listOf(1), 3))
    }

    @Test fun allSolutions() {
        assertEquals(setOf(Triple(1,1,4), Triple(1,2,3), Triple(2,2,2)),
            trititions(listOf(1,2,3,4), 6))
    }
}
