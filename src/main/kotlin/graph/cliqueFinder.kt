package graph

import kotlin.math.max
import kotlin.math.min

data class UEdge private constructor(val v0 :Int, val v1 :Int) :Cloneable {
    companion object {
        fun of(v1 :Int, v2 :Int) = UEdge(min(v1,v2), max(v1,v2))
    }

    override fun clone() = UEdge(v0, v1)
}

fun cliqueFinder(n :UInt, es :Collection<UEdge>) :Set<Set<Int>> {
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
