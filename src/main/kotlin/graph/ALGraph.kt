package graph

import kotlin.math.max
import kotlin.math.min

class ALGraph private constructor(override val numVertices :UInt, protected val edges :Map<Int, Set<Int>>, override val name :String) :Graph {
    constructor(numVertices :UInt, edges :Collection<Graph.Edge> =emptyList(), name :String? =null) :this(numVertices,
        edges.groupBy { it.v0 }.entries.map { Pair(it.key, it.value.map{ it.v1 }.toSet()) }.toMap(),
        name?: "G$numVertices")

    fun copy() = ALGraph(numVertices, edges, name)

    override fun equals(other :Any?) :Boolean {
        if (other !is Graph) return false
        if (this===other) return true
        if (other !is ALGraph) return other==this
        return numVertices==other.numVertices && edges==other.edges
    }

    override fun hashCode() = 11+ numVertices.hashCode() +31* edges.hashCode()

    override fun getEdges() = edges.entries.flatMap { (v, ws) -> ws.map { w -> Graph.Edge.of(v, w) } }
    override val numEdges = edges.values.sumOf { it.size.toUInt() }

    override fun findNeighbors(v :Int) :Set<Int> = (edges.getOrElse(v) {emptySet()}.sorted() +
        edges.entries.mapNotNull { (w, neighbors) ->
            if (v in neighbors) w  else null
        }).toSet()

    override fun toString() = name

    override fun describe() = toString()+"\n  "+ edges.entries.map { (v, ws) -> "$v: "+ws.joinToString() }.joinToString("\n  ")

    override fun minV() = min( edges.keys.min(), edges.values.minOf { it.min() })
    override fun maxV() = max( edges.keys.max(), edges.values.maxOf { it.max() })
}
