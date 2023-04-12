package graph

import kotlin.test.*

class CliqueTester {
    @Test fun empty() {
        val result = cliqueFinder(0u, emptyList())
        assertEquals(emptySet(), result)
    }

    @Test fun singleton() {
        val result = cliqueFinder(1u, emptyList())
        assertEquals(setOf(setOf(1)), result)
    }

    @Test fun singletons() {
        val result = cliqueFinder(2u, emptyList())
        println(result)
        assertEquals(setOf(setOf(1), setOf(2)), result)
    }

    @Test fun join() {
        val result = cliqueFinder(3u, listOf(UEdge.of(1, 2)))
        println(result)
        assertEquals(setOf(setOf(1,2), setOf(3)), result)
    }

    @Test fun path() {
        val result = cliqueFinder(4u, listOf(UEdge.of(1,2), UEdge.of(2,3), UEdge.of(3,4)))
        println(result)
        assertEquals(setOf(setOf(1,2,3,4)), result)
    }
}
