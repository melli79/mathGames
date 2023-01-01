package trivia

import kotlin.test.*

class RelativelyPrimeTester {

    @Test fun emptySequence() {
        assertEquals(emptyList(), findWithDisjointGcds(0u, 100uL, 1000uL))
    }

    @Test fun singleNumber() {
        assertEquals(listOf(10uL), findWithDisjointGcds(1u, 10uL, 100uL))
    }

    @Test fun twoNumbers() {
        assertEquals(listOf(10uL, 10uL), findWithDisjointGcds(2u, 10uL, 100uL))
    }
}
