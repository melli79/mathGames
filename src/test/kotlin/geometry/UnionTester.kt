package geometry

import kotlin.test.*

class UnionTester {
    @Test fun empty() {
        val result = computeTotalArea(emptyList())
        assertEquals(0uL, result)
    }

    @Test fun disjoint() {
        val result = computeTotalArea(listOf(Rectangle(0,0,1u,1u), Rectangle(0,1,1u,1u)))
        assertEquals(2uL, result)
    }

    @Test fun tripleIntersection() {
        val rects = listOf(Rectangle(0,0, 2u,2u), Rectangle(1,0,2u,2u), Rectangle(0,1,2u,2u))
        val result = computeTotalArea(rects)
        assertEquals(8uL, result)
    }
}
