package scheduling

import kotlin.test.*

class EarliestScheduleTester {
    @Test fun empty() {
        assertEquals(emptyList(), schedule(emptyList()))
        assertEquals(0uL, computeSlack(emptyList()))
    }

    @Test fun singleton() {
        val task = Task(1u, 1)
        val s = schedule(listOf(task))
        assertEquals(listOf(task), s)
        assertEquals(0uL, computeSlack(s))
    }

    @Test fun twoTasks() {
        val t1 = Task(1u, 1);  val t2 = Task(2u, 2)
        val s = schedule(listOf(t1, t2))
        assertEquals(listOf(t1, t2), s)
        assertEquals(1uL, computeSlack(s))
    }

    @Test fun cumulativeSlack() {
        val t1 = Task(2u, 1);  val t2 = Task(2u, 3)
        val s = schedule(listOf(t1, t2))
        assertEquals(listOf(t1, t2), s)
        assertEquals(2uL, computeSlack(s, Norm.L1))
    }

    @Test fun countingSlack() {
        val t1 = Task(2u, 1);  val t2 = Task(2u, 2)
        val s = schedule(listOf(t1, t2))
        assertEquals(listOf(t1, t2), s)
        assertEquals(2uL, computeSlack(s, Norm.Counting))
    }

    @Test fun l2Slack() {
        val t1 = Task(2u, 1);  val t2 = Task(2u, 2)
        val s = schedule(listOf(t1, t2))
        assertEquals(listOf(t1, t2), s)
        assertEquals(5uL, computeSlack(s, Norm.L2))
    }
}
