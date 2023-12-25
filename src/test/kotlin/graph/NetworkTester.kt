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

    @Test
    fun k7() {
        val n = Network(7u, listOf(
            Pair(edge(0, 1), 19.0), Pair(edge(0, 2), 18.0),
            Pair(edge(0, 3), 15.0), Pair(edge(0, 4), 17.0), Pair(edge(0, 5), 18.0), Pair(edge(0, 6), 16.0),
            Pair(edge(1, 2), 17.0), Pair(edge(1, 3), 13.0), Pair(edge(1, 4), 16.0), Pair(edge(1, 5), 18.0),
            Pair(edge(1, 6), 15.0), Pair(edge(2, 3), 12.0), Pair(edge(2, 4), 13.0), Pair(edge(2, 5), 16.0),
            Pair(edge(2, 6), 12.0), Pair(edge(3, 4), 11.0), Pair(edge(3, 5), 12.0), Pair(edge(3, 6), 9.0),
            Pair(edge(4, 5), 14.0), Pair(edge(4, 6), 12.0), Pair(edge(5, 6), 13.0)
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
