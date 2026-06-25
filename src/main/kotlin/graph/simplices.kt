package graph

// Computing L_k of a Graph
fun computeL0(g :ALGraph) = (g.minV()..g.maxV()).toSet()

fun computeL1(g :Graph) = g.getEdges().toSet()

fun Graph.computeLk(k :UInt) :Set<Set<Int>> = when (k) {
   0u -> return computeL0(this as ALGraph).map { setOf(it) }.toSet()
   1u -> return computeL1(this).map { setOf(it.v0, it.v1) }.toSet()
   else -> {
      val lk1 = computeLk(k-1u)
      val result = mutableSetOf<Set<Int>>()
      for (ck in lk1) {
        val u = ck.first()
         for (v in findNeighbors(u)) if (v !in ck) {
            if (ck.all { it==u || hasEdge(Graph.Edge.of(v, it)) })
              result.add(ck+setOf(v))
         }
      }
      result
   }
}

