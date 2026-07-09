package graph

// Greedy Graph coloriation with dynamic degree of saturation ordering
internal fun List<Int>.maxDsat(dsat :List<Int>, degs :List<Int>) :Int? {
   var candidate :Int? = null
   var maxDs = 0;  var maxD = 0
   for (v in indices) if (get(v)<0)
     if (candidate==null || dsat[v]>maxDs || dsat[v]==maxDs && degs[v]>maxD) {
       candidate = v;  maxDs = dsat[v];  maxD = degs[v]
     }
   return candidate
}

fun ALGraph.colorizeDS() :List<Int> {
  val colors = MutableList(numVertices.toInt()) { -1 }
  val dsat = MutableList(numVertices.toInt()) { 0 }
  val degs = (0..numVertices.toInt()).map { deg(it) }.toMutableList()
  while (true) {
    val v = colors.maxDsat(dsat, degs) ?: break
//    print("colorizing $v");  System.out.flush()
    colors[v] = colorize(v, colors)
    getNeighbors(v, colors).forEach { w ->
      degs[w] = degs[w]-1
      dsat[w] = computeDsat(w, colors)
    }
//    println()
  }

  for (v in colors.indices)
    if (colors[v]<0)  colors[v] = 0
  return colors
}

fun ALGraph.colorizeDSbt() :List<Int> {
  val colors = MutableList(numVertices.toInt()) { -1 }
  val dsat = MutableList(numVertices.toInt()) { 0 }
  val degs = (0..numVertices.toInt()).map { deg(it) }.toMutableList()

  return colorizeDSrec(colors, dsat, degs)!!
}

internal fun ALGraph.colorizeDSrec(colors :MutableList<Int>, dsat :MutableList<Int>, degs :MutableList<Int>) :List<Int>? {
  val v = colors.maxDsat(dsat, degs) ?: return colors
//  print("colorizing $v");  System.out.flush()
  val options = mutableSetOf(0, 1, 2, 3)
  val ns = findNeighbors(v)
  ns.forEach { w -> if (colors[w]>=0) options.remove(colors[w]) }
  for (c in options) {
    colors[v] = c
    ns.forEach { w ->
      degs[w] = degs[w]-1
      dsat[w] = computeDsat(w, colors)
    }
    val candidate = colorizeDSrec(colors, dsat, degs)
    if (candidate!=null)
      return candidate
    colors[v] = -1
    ns.forEach { w ->
      degs[w] = degs[w]+1
    }
    // println()
  }
  ns.forEach { w ->
     dsat[w] = computeDsat(w, colors)
  }
  return null
}

internal fun Graph.computeDsat(w :Int, colors :List<Int>) :Int
  = getNeighbors(w, colors).map { u -> colors[u] }.distinct().size

internal fun Graph.getNeighbors(v :Int, threshold :List<Int>)
 = findNeighbors(v).filter { threshold[it]>0 }.toSet()
