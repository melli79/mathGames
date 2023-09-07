package graph

import einstein.removeFirst

class NotPlanarException(msg :String ="Graph is not planar") :Throwable(msg) {}

fun planarize(g :Graph) = computeCC(g).map { planarizeCC(it, g.getEdges()) }.toSet()

private fun planarizeCC(vertices :Set<Int>, edges :Collection<Graph.Edge>) :Triple<Map<Int, Point>, Set<Graph.Edge>, Set<Graph.Edge>> {
    val remainingEdges = edges.filter { it.v0 in vertices && it.v1 in vertices }.toMutableSet()
    val base = findTremeauxTree(vertices, remainingEdges)
    val (left, right) = base.partitionEdges(remainingEdges)
    return Triple(base.mapIndexed { i, v -> Pair(v, Point(i.toDouble(), 0.0)) }.toMap(), left, right)
}

fun findTremeauxTree(vertices: Set<Int>, edges: MutableSet<Graph.Edge>): List<Int> {
    if (vertices.isEmpty())
        return emptyList()
    val remainingVertices = vertices.toMutableSet()
    val result = mutableListOf(remainingVertices.removeFirst()!!)
    var last = result.last()
    while (remainingVertices.isNotEmpty()) {
        var v :Int
        while (true) {
            v = (edges.filter { it.v0==last && it.v1 in remainingVertices }.map { it.v1 } +edges.filter { it.v1==last && it.v0 in remainingVertices }.map { it.v0 })
                .firstOrNull() ?: -1
            if (v<0)
                last = result[result.indexOf(last)-1]
            else
                break
        }
        result.add(v)
        remainingVertices.remove(v);  edges.remove(Graph.Edge.of(last, v))
        last = v
    }
    return result
}

/* may throw {@link NotPlanarException} if the edges cannot be partitioned in 2 sets w.r.t.
 * the Trémeaux tree.
 */
private fun List<Int>.partitionEdges(edges :Collection<Graph.Edge>) :Pair<Set<Graph.Edge>, Set<Graph.Edge>> {
    if (edges.isEmpty())
        return Pair(emptySet(), emptySet())
    if (edges.size>2)
        TODO("to be implemented")
    return Pair(setOf(edges.first()), edges.drop(1).toSet())
}

data class Point(val x :Double, val y :Double) {
    override fun toString() = "Point($x, $y)"
}
