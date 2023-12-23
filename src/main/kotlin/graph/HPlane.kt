package graph

fun generate44444graph(n :UShort) :Graph {
    if (n<1u)
        return graphOf(1u)
    val edges = mutableListOf<Graph.Edge>()
    var v = 0
    var rim = Direction.entries.map { v }
    for (level in 1..n.toInt()) {
        val v0 = v+1
        var vl = -1
        rim = rim.flatMap { vi ->
            val v1 = ++v
            edges.add(Graph.Edge.of(vi, v1))
            if (vl>=0)
                edges.add(Graph.Edge.of(vl, v1))
            val v2 = ++v
            edges.add(Graph.Edge.of(v1,v2))
            vl = ++v
            edges.add(Graph.Edge.of(v2, vl))
            listOf(v1, v2, vl)
        }
        edges.add(Graph.Edge.of(vl, v0))
    }
    return graphOf((v+1).toUInt(), edges)
}

fun generate44444maze(n :UShort) :Maze {
    val c = Cell()
    val rooms = mutableListOf(c)
    if (n<1u)
        return Maze(rooms, c)
    var rim :List<Pair<Cell, Direction>> = Direction.entries.map { Pair(c, it) }
    for (level in 1..n.toInt()) {
        var vl = c
        var first = true
        rim = rim.flatMap { (vi, d) ->
            val v1 = Cell();  rooms.add(v1)
            vi.connect(d, v1)
            if (!first)
                vl.connect(d.next(), v1)
            else
                first = false
            val v2 = Cell();  rooms.add(v2)
            v1.connect(d.next(), v2)
            vl = Cell();  rooms.add(vl)
            v2.connect(d.next(), vl)
            listOf(Pair(v1, d), Pair(v2, d), Pair(vl, d))
        }
        rim.first().first.connect(Direction.Right, vl)
    }
    return Maze(rooms, c)
}
