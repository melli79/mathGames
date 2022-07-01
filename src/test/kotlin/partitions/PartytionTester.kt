package partitions

import kotlin.test.*

class PartytionTester {
    @Test fun findNone() {
        assertEquals(emptySet(), partytion(listOf(2,3,4), 2))
    }

    @Test fun findUnique() {
        assertEquals(setOf(Pair(1, 1)), partytion(listOf(1, 1, 2), 2))
    }

    @Test fun findAll() {
        assertEquals(setOf(Pair(1, 3), Pair(2, 2)), partytion(listOf(1,2,3,4), 4))
    }
}
