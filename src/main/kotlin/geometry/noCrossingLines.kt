package geometry

import common.math.geometry.Point2D
import kotlin.math.sqrt

fun noCrossingLines(blues :List<Point2D>, reds :List<Point2D>) :Set<Pair<Point2D, Point2D>> {
    val result = mutableMapOf<Point2D, MutableList<Point2D>>()
    // greedy spread
    for (blue in blues) {
        var minR :Point2D? = null;  var minD = Double.MAX_VALUE
        for (red in reds) {
            val d2 = (blue-red).norm2()
            if (d2<minD) {
                minD = d2;  minR = red
            }
        }
        if (minR!=null)
            result.getOrPut(minR) { mutableListOf() }.add(blue)
    }
    // divert collisions
    val opens = reds.filter { it !in result.keys }.toMutableSet()
    while (result.size<blues.size && opens.isNotEmpty()) {
        val ambiguous = result.maxBy { it.value.size }
        val diverts = ambiguous.value.iterator()
        diverts.next()
        while (diverts.hasNext() && opens.isNotEmpty()) {
            val blue = diverts.next()
            val red = opens.minBy { (blue-it).norm2() }
            diverts.remove()
            result.getOrPut(red) { mutableListOf() }.add(blue)
        }
    }
    if (blues.size<=1)
        return result.entries.map { Pair(it.value.first(), it.key) }.toSet()
    // shuffle locally
    val order = blues.indices.shuffled()
    var it = order.iterator()
    outer@while (it.hasNext()) {
        val p1 = result.entries.toList()[it.next()]
        for (p2 in result.entries) if (p1.key!=p2.key) {
            val d1 = (p1.key - p1.value.first()).norm2()
            val d2 = (p2.key - p2.value.first()).norm2()
            val x1 = (p1.key - p2.value.first()).norm2()
            val x2 = (p2.key - p1.value.first()).norm2()
            if (sqrt(x1) + sqrt(x2) < sqrt(d1) + sqrt(d2)) {
                val i1 = p1.value.iterator()
                val b2 = i1.next(); i1.remove()
                val i2 = p2.value.iterator()
                val b1 = i2.next(); i2.remove()
                p1.value.add(b1)
                p2.value.add(b2)
                it = order.iterator()
                continue@outer
            }
        }
    }
    return result.entries.map { Pair(it.value.first(), it.key) }.toSet()
}
