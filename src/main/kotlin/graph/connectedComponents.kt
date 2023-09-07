package graph

fun computeCC(g :Graph) :Set<Set<Int>> {
    val result = (0 until g.numVertices.toInt()).map { setOf(it) }.toMutableSet()
    for (e in g.getEdges()) {
        val s1 = result.findSet(e.v0)
        val s2 = result.findSet(e.v1)
        if (s1!=null && s2!=null && s1!=s2) {
            result.remove(s1);  result.remove(s2)
            result.add(s1+s2)
            if (result.size==1)
                break
        }
    }
    return result
}

private fun <T> Collection<Set<T>>.findSet(e :T) :Set<T>? = find { e in it }
