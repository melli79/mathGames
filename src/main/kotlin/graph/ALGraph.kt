package graph

class ALGraph(override val numVertices :UInt, edges :Collection<Graph.Edge>, val name :String) :Graph {
    private val edges :List<List<Int>> = (0 until numVertices.toInt()).map { v ->
        edges.filter { it.v0==v }.map { it.v1 }
    }

    override fun getEdges() = edges.flatMapIndexed { v, ws -> ws.map { w -> Graph.Edge.of(v, w) } }

    override fun countEdges() = edges.sumOf { it.size }
    override fun findNeighbors(v :Int) :List<Int> = edges[v] +edges.mapIndexed { w, neighbors ->
        if (v in neighbors) w  else null
    }.filterNotNull()

    override fun toString() = name

    override fun describe() = toString()+"\n  "+ edges.mapIndexed { v, ws -> "$v: "+ws.joinToString() }.joinToString("\n  ")
}
