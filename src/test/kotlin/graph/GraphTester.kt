package graph

import kotlin.test.*

class GraphTester {
    @Test fun emptyGraph() {
        val g = graphOf(0u, emptyList(), "O")
        println(g.describe())
        assertEquals(0u, g.numVertices)
        assertEquals(0u, g.numEdges)
    }

    @Test fun neighbors() {
        val k3 = graphOf(3u, listOf(0 to 1, 1 to 2, 2 to 0), "K3")
        println("$k3: "+ k3.describe())
        println("V= 0..${k3.maxV()}")
        assertEquals(3u, k3.numVertices)
        assertEquals(3u, k3.numEdges)
        assertEquals(setOf(1,2), k3.findNeighbors(0))
    }

    @Test fun hashing() {
        val g = graphOf(3u, listOf(Graph.Edge.of(0,1), Graph.Edge.of(1,2),))
        val h = g.hashCode()
        val g2 = graphOf(3u, listOf(Graph.Edge.of(0,1), Graph.Edge.of(1,2),))
        val h2 = g2.hashCode()
        assertEquals(h, h2)
        assertEquals(g, g2)
    }

   infix fun Int.to(w :Int) = Graph.Edge.of(this, w)
}
