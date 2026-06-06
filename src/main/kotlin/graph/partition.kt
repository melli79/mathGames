package graph

import common.math.flipCoin
import kotlin.random.Random

val random = Random(System.currentTimeMillis())

fun partition(n :UShort, forbidden :Collection<Graph.Edge>) :Set<Set<Int>> {
    val result = mutableSetOf((0..< n.toInt()).toMutableSet())
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

fun Set<Int>.can(table :Set<Int>, newPlayer :Int) =
    newPlayer !in this || table.all { it !in this }

fun Collection<Collection<Set<Int>>>.can(table :Set<Int>, newPlayer :Int) =
    flatten().all { round -> round.can(table, newPlayer) }

fun partition(k :UInt, n :UInt) :List<Set<Set<Int>>> {
    val N = k*(1u+n*(k-1u))
    val days = mutableListOf<MutableSet<Set<Int>>>()
    for (d in 1u..(N-1u)/(k-1u)) {
        val day = mutableSetOf<Set<Int>>()
        val players = (0..<N.toInt()).toList().toMutableList()
        for (nr in 0u..<N/k) {
            val iterator = players.iterator()
            val row :MutableSet<Int> = mutableSetOf(iterator.next())
            iterator.remove()
            while (iterator.hasNext()) {
                val p = iterator.next()
                if (days.can(row, p)) {
                    row.add(p)
                    iterator.remove()
                    if (row.size.toUInt()==k) break
                }
            }
            if (row.size.toUInt()<k) println("Cannot fill row $nr on day $d!")
            day.add(row)
        }
        if (day.size.toUInt()<N/k) println("Cannot fill day $d!")
            days.add(day)
    }
    return days
}

fun marjaPussi(n :UInt) {
    val k = 4u
    val N = k*(1u+n*(k-1u))
    println("Game setup: $N players in groups of $k are playing ${(N-1u)/(k-1u)} rounds:")
    val rounds = partition(k, n)
    println("\n"+ rounds.mapIndexed { r, round -> Pair(r, round) }.joinToString("\n") { (r, round) ->
          "round $r: "+round.joinToString() })
}

fun girls(n :UInt) {
    val k = 3u
    val N = k*(1u+n*(k-1u))
    println("Game setup: $N girls in groups of $k are walking ${(N-1u)/(k-1u)} days:")
    val rounds = partition(k, n)
    println("\n"+ rounds.mapIndexed { d, day -> Pair(d, day) }.joinToString("\n") { (d, day) ->
        "day $d: "+day.joinToString() })
}
