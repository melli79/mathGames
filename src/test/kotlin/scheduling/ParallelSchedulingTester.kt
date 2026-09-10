package scheduling

import kotlin.test.*

class ParallelSchedulingTester {
    @Test fun empty() {
        assertEquals(emptySet(), scheduleParallel(emptySet<Interval<UInt>>()))
    }

    @Test fun singleQueue() {
        val i = Interval(0, 1);  val i2 = Interval(1,2);  val i3 = Interval(2,3)
        assertEquals(setOf(listOf(i, i2, i3)), scheduleParallel(setOf(i, i2, i3)))
    }

    @Test fun concurring() {
        val i = Interval(0, 2);  val i2 = Interval(1,3);  val i3 = Interval(2,4)
        val result = scheduleParallel(setOf(i, i2, i3))
        println(result)
        assertEquals(setOf(listOf(i, i3), listOf(i2)), result)
    }
}
