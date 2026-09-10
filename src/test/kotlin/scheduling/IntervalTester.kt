package scheduling

import kotlin.test.*

class IntervalTester {
    fun <F :Comparable<F>> singleton(x :F) = Interval(x,x)

    @Test fun singleton() {
        assertEquals(Interval(0,0), singleton(0))
    }

    @Test fun noReverseIntervals() {
        assertFails { Interval(1,0) }
    }

    @Test fun disjoint() {
        assertFalse(Interval(0,1).overlaps(Interval(2,3)))
    }

    @Test fun disjointReversed() {
        assertFalse(Interval(2,3).overlaps(Interval(0,1)))
    }

    @Test fun leftOverlap() {
        assertTrue(Interval(0,2).overlaps(Interval(1,3)))
    }

    @Test fun rightOverlap() {
        assertTrue(Interval(1,3).overlaps(Interval(0,2)))
    }
}
