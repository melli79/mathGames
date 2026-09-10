package scheduling

import kotlin.test.*

class IntervalSchedulerTester {
    @Test fun empty() {
        assertEquals(emptySet(), schedule(emptySet<Interval<UInt>>()))
    }

    @Test fun single() {
        val i = Interval(0, 1)
        assertEquals(setOf(i), schedule(setOf(i)))
    }

    @Test fun both() {
        val i = Interval(0, 1);  val i2 = Interval(1, 2)
        assertEquals(setOf(i, i2), schedule(setOf(i, i2)))
    }

    @Test fun either() {
        val i = Interval(0, 2);  val i2 = Interval(1, 3)
        val result = schedule(setOf(i, i2))
        println(result)
        assertEquals(1, result.size)
    }
}
