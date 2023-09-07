package graph

import kotlin.test.*

class CCTester {
    @Test
    fun empty() {
        assertEquals(emptySet(), computeCC(graphOf(0u)))
    }

    @Test
    fun singleton() {
        assertEquals(setOf(setOf(0)), computeCC(graphOf(1u)))
    }

    @Test
    fun pair() {
        assertEquals(setOf(setOf(0), setOf(1)), computeCC(graphOf(2u)))
    }

    @Test
    fun line() {
        assertEquals(setOf(setOf(0,1)), computeCC(graphOf(2u, listOf(Graph.Edge.of(0,1)))))
    }

    @Test
    fun pointAndLine() {
        assertEquals(setOf(setOf(0,1), setOf(2)), computeCC(graphOf(3u, listOf(Graph.Edge.of(0,1)))))
    }
}
