package partitions

import kotlin.test.*

class PartitionTester {
    @Test fun zero() {
        assertEquals(emptySet(), partitions(0u))
    }

    @Test fun one() {
        assertEquals(setOf(listOf(1u)), partitions(1u))
    }

    @Test fun two() {
        assertEquals(setOf(listOf(1u, 1u), listOf(2u)), partitions(2u))
    }

    @Test fun three() {
        assertEquals(setOf(listOf(1u, 1u, 1u), listOf(1u, 2u), listOf(3u)), partitions(3u))
    }

    @Test fun four() {
        assertEquals(setOf(listOf(1u,1u,1u,1u), listOf(1u,1u,2u), listOf(1u,3u), listOf(2u,2u), listOf(4u)),
            partitions(4u))
    }

    @Test fun upTo20() {
        println((0u..20u).map { s -> partitions(s).size})
    }
}
