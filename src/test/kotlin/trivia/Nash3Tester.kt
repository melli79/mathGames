package trivia

import kotlin.test.*

class Nash3Tester {
    @Test fun empty() {
        val (r1, r2, r3) = nash3(emptyList(), emptyList(), emptyList())
        assertTrue(r1.isEmpty())
        assertTrue(r2.isEmpty())
        assertTrue(r3.isEmpty())
    }

    @Test fun oneEach() {
        val input = listOf(1, 2, 3)
        val (r1, r2, r3) = nash3(input, input, listOf(3,1,2))
        assertEquals(r1.size, r2.size)
        assertEquals(r1.size, r3.size)
        assertEquals(input.toSet(), (r1+r2+r3).toSet())
        assertEquals(listOf(1), r1)
        assertEquals(listOf(2), r2)
    }

    @Test fun oneTooFew() {
        val input = listOf(1, 2)
        val (r1, r2, r3) = nash3(input, input, input)
        assertEquals(r1.size, r2.size)
        assertEquals(r1.size-1, r3.size)
        assertEquals(input.toSet(), (r1+r2+r3).toSet())
        assertEquals(listOf(1), r1)
    }

    @Test fun oneTooMany() {
        val input = listOf(1)
        val (r1, r2, r3) = nash3(input, input, input)
        assertEquals(r1.size-1, r2.size)
        assertEquals(r1.size-1, r3.size)
        assertEquals(input.toSet(), (r1+r2+r3).toSet())
        assertEquals(listOf(1), r1)
    }
}
