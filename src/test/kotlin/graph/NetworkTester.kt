package graph

import java.util.TreeSet
import kotlin.test.*
import graph.Graph.Edge.Companion.of as edge

class NetworkTester {
    @Test fun k3() {
        val n = Network(3u, listOf(Pair(edge(0,1), 1.0), Pair(edge(1,2), 2.0),
            Pair(edge(0,2), 2.5)))
        println(n.describe())
        assertTrue(n.isMetric())
    }

    infix fun Int.to(w :Int) = Graph.Edge.of(this, w)
    infix fun Graph.Edge.to(w :Double) = Pair(this, w)

    @Test
    fun k7() {
        val n = Network(7u, listOf<Pair<Graph.Edge, Double>>(
            (0 to 1) to 19.0, (0 to 2) to 18.0,
            (0 to 3) to 15.0, (0 to 4) to 17.0, (0 to 5) to 18.0, (0 to 6) to 16.0,
            (1 to 2) to 17.0, (1 to 3) to 13.0, (1 to 4) to 16.0, (1 to 5) to 18.0,
            (1 to 6) to 15.0, (2 to 3) to 12.0, (2 to 4) to 13.0, (2 to 5) to 16.0,
            (2 to 6) to 12.0, (3 to 4) to 11.0, (3 to 5) to 12.0, (3 to 6) to 9.0,
            (4 to 5) to 14.0, (4 to 6) to 12.0, (5 to 6) to 13.0
        ))
        println("$n is ${if (n.isMetric()) "" else "not "}metric.")
        var m = Double.POSITIVE_INFINITY;  var sPath = listOf<Int>()
        (1..6).toList().permute6 { a, b, c, d, e, f ->
            val path = listOf(0, a, b, c, d, e, f, 0)
            val len = n.computeLength(path)
            if (len < m) {
                m = len
                sPath = path
                println("shorter path $path with length: $len")
            }
        }
        println("Shortest path $sPath of length: $m")
    }
}

fun <T> Collection<T>.permute6(
    visitor :(T, T, T, T, T, T) -> Unit
) {
    val vertices = TreeSet(this)
    for (a in vertices.toList()) {
        vertices.remove(a)
        for (b in vertices.toList()) {
            vertices.remove(b)
            for (c in vertices.toList()) {
                vertices.remove(c)
                for (d in vertices.toList()) {
                    vertices.remove(d)
                    for (e in vertices.toList()) {
                        vertices.remove(e)
                        val f = vertices.first()
                        visitor(a, b, c, d, e, f)
                        vertices.add(e)
                    }
                    vertices.add(d)
                }
                vertices.add(c)
            }
            vertices.add(b)
        }
        vertices.add(a)
    }
}
