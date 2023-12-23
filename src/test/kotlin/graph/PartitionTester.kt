package graph

import org.junit.jupiter.api.RepeatedTest
import kotlin.test.*

class PartitionTester {
    val n = 10

    @Test fun unconstrained() {
        val result = partition(n.toUShort(), emptySet())
        assertEquals(1, result.size)
        assertEquals((0..< n).toSet(), result.first())
    }

    @Test fun singleConstraint() {
        val result = partition(10u, setOf(Graph.Edge.of(0,1)))
        assertEquals(2, result.size)
        val (s1, s2) = result.toList()
        assertEquals((0..< n).toSet(), s1+s2)
        assertTrue(0 in s1 && 1 in s2 || 0 in s2 && 1 in s1)
    }

    @Test fun connectedConstraints() {
        var m = 10
        val input = setOf(Graph.Edge.of(0, 1), Graph.Edge.of(0, 2))
        var result = partition(n.toUShort(), input)
        repeat(10) {
            val candidate = partition(n.toUShort(), input)
            if (candidate.size < m) {
                m = candidate.size
                result = candidate
            }
        }
        assertEquals(2, m)
        val (s1, s2) = result.toList()
        assertEquals((0..< n).toSet(), s1+s2)
        assertTrue(0 in s1 && 1 in s2 || 0 in s2 && 1 in s1)
        assertTrue(0 in s1 && 2 in s2 || 0 in s2 && 2 in s1)
    }

    companion object {
        val mm = mutableSetOf(1)
    }

    @RepeatedTest(1000) fun try20constraints() {
        val constraints = (1..20).map {
            random.edge(n)
        }.toMutableSet()
        while (constraints.size<20) {
            constraints.add(random.edge(n))
        }
        var m = 10
        var result = partition(n.toUShort(), constraints)
        repeat(50) {
            val candidate = partition(n.toUShort(), constraints)
            if (candidate.size < m) {
                m = candidate.size
                result = candidate
            }
        }
        if (m>mm.max()) {
            mm.add(m)
            println("20 constraints into $m")
        }
    }
}
