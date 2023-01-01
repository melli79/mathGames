package trivia

import kotlin.test.*

class Nash2Tester {
    @Test fun empty() {
        val (r1, r2) = nash2(emptyList(), emptyList())
        assertTrue(r1.isEmpty())
        assertTrue(r2.isEmpty())
    }

    @Test fun onePair() {
        val input = listOf(1, 2)
        val (r1, r2) = nash2(input, emptyList())
        assertEquals(r1.size, r2.size)
        assertEquals(input.toSet(), (r1+r2).toSet())
        assertEquals(listOf(1), r1)
    }

    @Test fun oddLength() {
        val input = listOf(1,2,3,4,5)
        val (r1, r2) = nash2(input, emptyList())
        assertEquals(r2.size+1, r1.size)
        assertEquals(input.toSet(), (r1+r2).toSet())
    }

    @Test fun threePairs() {
        val input = listOf(1, 2, 3, 4, 5, 6)
        val (r1, r2) = nash2(input, listOf(2,3,4,5,6,1))
        assertEquals(r1.size, r2.size)
        assertEquals(input.toSet(), (r1+r2).toSet())
        assertEquals(listOf(2, 4, 1), (r1))
    }
}
