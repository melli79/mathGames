package graph

import algebra.ZPoly

object cpoly {
    private val cache = mutableMapOf<ALGraph, ZPoly>()
    operator fun invoke(g :Graph) :ZPoly {
        val graph = graphOf(g.numVertices, g.getEdges())
        return lookup(graph)
    }

    private fun lookup(graph :ALGraph) :ZPoly {
        if (graph in cache)
            return cache[graph]!!
        val es = graph.getEdges()
        //println("Computing ${graph.numVertices}: ${es.size}")
        val e = es.firstOrNull()
        if (e==null) {
            val result = ZPoly.monomial(graph.numVertices)
            cache[graph] = result
            return result
        }
        val u = e.v0;  val v = e.v1; assert(u!=v)
        val neighs = graph.findNeighbors(v).filter { it!=u }.toMutableSet()
        val w = graph.numVertices.toInt()-1
        val result = if (v!=w) {
            val wNeighs = graph.findNeighbors(w).filter { it!=v }
            if (w in neighs) {
                neighs.remove(w)
                neighs.add(v)
            }
            lookup(graphOf(graph.numVertices, es.filter { it != e })) -
                lookup(graphOf(graph.numVertices - 1u,
                es.filter { it.v0!=w && it.v1!=w && it.v0!=v && it.v1!=v }
                            + neighs.map { Graph.Edge.of(u, it) } + wNeighs.map { Graph.Edge.of(v, it) })
                )
        } else {
            lookup(graphOf(graph.numVertices, es.filter { it != e })) -
                lookup(graphOf(graph.numVertices - 1u, es.filter { it.v0 != w && it.v1 != w }
                        + neighs.map { Graph.Edge.of(u, it) }))
        }
        cache[graph] = result
        return result
    }
}
