package graph

import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

interface Graph {
    companion object {
        fun complete(numVertices: UInt) :Graph {
            val edges = (0 until numVertices.toInt()).flatMap { v ->
                (v+1 until numVertices.toInt()).map { w -> Edge.of(v, w) }
            }
            return graphOf(numVertices, edges, "K_$numVertices")
        }

        fun circle(numVertices :UInt) :Graph {
            val edges = (0 until numVertices.toInt()-1).map { v -> Edge.of(v, v+1) }
            return graphOf(numVertices, edges+ listOf(Edge.of(0, numVertices.toInt()-1)))
        }
    }

    val numVertices :UInt

    fun getVertices() = (0 until numVertices.toInt()).toList()

    fun getEdges() :Collection<Edge>

    data class Edge private constructor(val v0 :Int, val v1 :Int) :Cloneable {
        companion object {
            fun of(v1: Int, v2: Int) = Edge(min(v1, v2), max(v1, v2))
        }

        override fun clone() = Edge(v0, v1)
    }

    fun describe() :String

    fun countEdges() = getEdges().size
    fun findNeighbors(v :Int) :Collection<Int>
}

fun graphOf(numVertices :UInt, edges :Collection<Graph.Edge> =emptyList(), name :String="G$numVertices")
    = ALGraph(numVertices, edges, name)

fun Random.edge(n :Int) :Graph.Edge {
    val u = nextInt(n)
    var v = u
    while (v == u) {
        v = nextInt(n)
    }
    return Graph.Edge.of(u, v)
}