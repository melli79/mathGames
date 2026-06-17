package graph
// vertex colorization by Malatya centrality ordering

internal fun ALGraph.split1(v :Int) :ALGraph {
   val edges = this.getEdges().filter { it.v0!=v && it.v1!=v }
   val neighs = findNeighbors(v)
   val maxV = this.maxV() +1
   return graphOf(numVertices-1u+neighs.size.toUInt(),
       edges + neighs.mapIndexed { i, v1 -> Graph.Edge.of(maxV+i, v1) },
       "$name'"
   )
}

fun Graph.mcentrality(v :Int) :Double {
   val ns = findNeighbors(v)
   return ns.sumOf { ns.size.toDouble()/findNeighbors(it).size }
}

internal fun ALGraph.maxCentrality() :Int? {
   val cs = (0..maxV()).map { mcentrality(it) }
   if (cs.isEmpty()) return null
   return cs.indices.maxBy { cs[it] }
}

fun ALGraph.colorize() :List<Int> {
   val numV = maxV()
   val cs = MutableList(numV+1) {-1}
   var g = this.copy()
   while (true) {
      val v = g.maxCentrality() ?: break
      if (v>numV) break
      cs[v] = g.colorize(v, cs)
//      println("colorizing $v: ${cs[v]}")
      g = g.split1(v)
      val newV = g.maxV()+1
//      println("reduced graph: ${g.describe()}")
      while (cs.size<newV) cs.add(cs[v])
   }
   (0..numV).forEach { v ->
      if (cs[v]<0) cs[v] = 0
   }
   return cs.slice(0..numV)
}

internal fun ALGraph.colorize(v :Int, cs :List<Int>) :Int {
    val candidates = mutableSetOf(0, 1, 2, 3, 4)
    findNeighbors(v).forEach { v ->
        val c = cs[v]
        if (c>=0) candidates.remove(c)
    }
    return candidates.min()
}
