package graph

import common.math.flipCoin
import kotlin.random.Random

val random = Random(System.currentTimeMillis())

fun partition(n :UShort, forbidden :Collection<Graph.Edge>) :Set<Set<Int>> {
    val result = mutableSetOf<MutableSet<Int>>((0..< n.toInt()).toMutableSet())
    outer@for (e in forbidden) {
        val u = e.v0;  val v = e.v1
        val a = result.find { u in it }!!
        val b = result.find { v in it }!!
        if (a===b)
            result.separateEnemies(forbidden, u, v, a)
    }
    return result
}

private fun MutableSet<MutableSet<Int>>.separateEnemies(
    forbidden :Collection<Graph.Edge>,
    u :Int,
    v :Int,
    problem :MutableSet<Int>
) {
    if (random.flipCoin()) {
        if (tryVertex(forbidden, u, problem))
            return
        if (tryVertex(forbidden, v, problem))
            return
        problem.remove(u)
        add(mutableSetOf(u))
    } else {
        if (tryVertex(forbidden, v, problem))
            return
        if (tryVertex(forbidden, u, problem))
            return
        problem.remove(v)
        add(mutableSetOf(v))
    }
}

private fun Set<MutableSet<Int>>.tryVertex(
    forbidden :Collection<Graph.Edge>,
    v :Int,
    problem :MutableSet<Int>
) :Boolean {
    val uNeighbors = forbidden.filter { it.v0 == v || it.v1 == v }
        .map { if (it.v0 == v) it.v1 else it.v0 }
        .toSet()
    for (t in this) {
        if (t.all { it !in uNeighbors }) {
            problem.remove(v)
            t.add(v)
            return true
        }
    }
    return false
}
