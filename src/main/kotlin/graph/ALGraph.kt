package graph

class ALGraph(override val numVertices :UInt, edges :Collection<Graph.Edge>, val name :String) :Graph {
    private val edges :List<List<Int>> = (0 until numVertices.toInt()).map { v ->
        edges.filter { it.v0==v }.map { it.v1 }
    }

    override fun getEdges() = edges.flatMapIndexed { v, ws -> ws.map { w -> Graph.Edge.of(v, w) } }

    override fun toString() = name
}
