package equilibrium

import kotlin.test.*

class Nash22Tester {
    @Test fun empty() {
        val (r1, r2) = nash22(emptyList(), emptyList())
        assertTrue(r1.isEmpty())
        assertTrue(r2.isEmpty())
    }

    @Test fun onePair() {
        val input = listOf(1, 2)
        val (r1, r2) = nash22(input, emptyList())
        assertEquals(r1.size, r2.size)
        assertEquals(input.toSet(), (r1+r2).toSet())
        assertEquals(listOf(1), r1)
    }

    @Test fun oddLength() {
        val input = listOf(1,2,3,4,5)
        val (r1, r2) = nash22(input, emptyList())
        assertEquals(r2.size+1, r1.size)
        assertEquals(input.toSet(), (r1+r2).toSet())
        assertEquals(listOf(1,4,5), r1)
    }

    @Test fun threePairs() {
        val input = listOf(1, 2, 3, 4, 5, 6)
        val (r1, r2) = nash22(input, listOf(2,3,4,5,6,1))
        assertEquals(r1.size, r2.size)
        assertEquals(input.toSet(), (r1+r2).toSet())
        assertEquals(listOf(2, 5, 1), (r1))
    }
}
