package diophantine

import kotlin.test.*

class PythagoreanTester {
    @Test fun find0() {
        assertEquals(setOf(Triple(0u, 0u, 0u)), findPythagoreanTriples(0u))
    }

    @Test fun find1() {
        assertEquals(setOf(Triple(0u, 0u, 0u), Triple(1u, 0u, 1u)),
            findPythagoreanTriples(1u))
    }

    @Test fun find5() {
        assertEquals(setOf(Triple(0u, 0u, 0u), Triple(1u, 0u, 1u),
                Triple(2u, 0u, 2u), Triple(3u, 0u, 3u), Triple(4u, 0u, 4u),
                Triple(5u, 0u, 5u), Triple(5u, 3u, 4u)),
            findPythagoreanTriples(5u))
    }

    @Test fun find50() {
        val result = findPythagoreanTriples(50u).sortedBy { t -> t.first }
        println(result)
    }
}
