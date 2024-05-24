package graph

import graph.Graph.Edge

import kotlin.test.*

class PlanarizationTester {
    @Test fun empty() {
        assertEquals(setOf(), planarize(graphOf(0u)))
    }

    @Test fun singlePoint() {
        assertEquals(setOf(Triple(mapOf(Pair(0, Point(0.0,0.0))), emptySet(), emptySet())), planarize(graphOf(1u)))
    }

    @Test fun twoCC() {
        assertEquals(setOf(Triple(mapOf(Pair(0, Point(0.0,0.0))), emptySet(), emptySet()),
                Triple(mapOf(Pair(1, Point(0.0,0.0))), emptySet(), emptySet())),
            planarize(graphOf(2u)))
    }

    @Test fun line() {
        assertEquals(setOf(Triple(mapOf(Pair(0, Point(0.0,0.0)), Pair(1, Point(1.0,0.0))), emptySet(), emptySet())),
            planarize(graphOf(2u, listOf(Edge.of(0,1)))))
    }

    @Test fun circle() {
        val g = Graph.circle(3u)
        val result = planarize(g)
        println("Planarization of $g is: "+ result.joinToString(" ü "))
        assertEquals(setOf(Triple(mapOf(Pair(0, Point(0.0,0.0)), Pair(1, Point(1.0,0.0)), Pair(2, Point(2.0,0.0))),
                setOf(Edge.of(0,2)), emptySet())),
            result
        )
    }

    @Test fun joinedCircles() {
        val g = graphOf(4u, listOf(Edge.of(0,1), Edge.of(1,2), Edge.of(2,0),
            Edge.of(1,3), Edge.of(3,0)), "OO")
        val result = planarize(g)
        println("Planarization of $g is:"+ result.joinToString(" ü "))
    }
}
