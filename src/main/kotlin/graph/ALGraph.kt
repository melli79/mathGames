package graph

class ALGraph(override val numVertices :UInt, edges :Collection<Graph.Edge>, val name :String) :Graph {
    private val edges :List<Set<Int>> = (0 until numVertices.toInt()).map { v ->
        edges.filter { it.v0==v }.map { it.v1 }.toSet()
    }

    override fun equals(other :Any?) :Boolean {
        if (other !is Graph) return false
        if (this===other) return true
        if (other !is ALGraph) return other==this
        return numVertices==other.numVertices && edges==other.edges
    }

    override fun hashCode() = 11+ numVertices.hashCode() +31* edges.hashCode()

    override fun getEdges() = edges.flatMapIndexed { v, ws -> ws.map { w -> Graph.Edge.of(v, w) } }

    override fun countEdges() = edges.sumOf { it.size }
    override fun findNeighbors(v :Int) :List<Int> = edges.getOrElse(v) {emptySet()}.sorted() +
        edges.mapIndexed { w, neighbors ->
            if (v in neighbors) w  else null
        }.filterNotNull()

    override fun toString() = name

    override fun describe() = toString()+"\n  "+ edges.mapIndexed { v, ws -> "$v: "+ws.joinToString() }.joinToString("\n  ")
}
