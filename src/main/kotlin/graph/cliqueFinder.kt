package graph

fun cliqueFinder(n :UInt, es :Collection<Graph.Edge>) :Set<Set<Int>> {
    val result = (1..n.toInt()).map { setOf(it) }.toMutableSet()
    for (edge in es) if (edge.v0!=edge.v1) {
        val first = result.find { edge.v0 in it }
        val second = result.find { edge.v1 in it }
        if (first!=null && second!=null && first!=second) {
            result.remove(first); result.remove(second)
            result.add(first+second)
            if (result.size<=1)
                break
        }
    }
    return result
}
