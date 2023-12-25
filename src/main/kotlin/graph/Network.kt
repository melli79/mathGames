package graph

class Network(override val numVertices :UInt, w0s :Collection<Pair<Graph.Edge, Double>>, val name :String ="N$numVertices") :
        Graph {
    private val weights = (0..< numVertices.toInt()).map { mutableMapOf<Int, Double>() }

    init {
        w0s.forEach { (e, w) ->
            weights[e.v0][e.v1] = w
        }
    }

    override fun getEdges() = weights.flatMapIndexed { v :Int, ws :MutableMap<Int, Double> ->
        ws.keys.map { Graph.Edge.of(v, it) }
    }

    override fun toString() = name

    override fun describe() = toString()+":\n"+ weights.mapIndexed { v, ws :Map<Int, Double> ->
        "$v:  "+ ws.entries.joinToString { (v1, w :Double) -> "$v1: %.2g".format(w) }
    }.joinToString("\n")

    override fun findNeighbors(v :Int) :Collection<Int> = weights[v].keys + weights.mapIndexed { v1, ws ->
        if (v in ws.keys) v1  else null
    }.filterNotNull()

    operator fun get(e :Graph.Edge) :Double? = weights[e.v0][e.v1]
}

fun Network.isMetric() :Boolean = (0..< numVertices.toInt()).all { u ->
    (u+1..< numVertices.toInt()).all { v ->
        (v+1..< numVertices.toInt()).all { w ->
            val a = this[Graph.Edge.of(u, v)];  val b = this[Graph.Edge.of(u, w)];  val c = this[Graph.Edge.of(v, w)]
            a==null || b==null || c==null || (a+b>=c && b+c>=a && c+a>=b)
        }
    }
}

fun Network.computeLength(path :List<Int>) = path.windowed(2)
    .sumOf { (v :Int, w :Int) -> this[Graph.Edge.of(v, w)] ?: Double.POSITIVE_INFINITY }