package dynamic

import java.lang.System.out
import kotlin.random.Random

fun iter(n :UInt) :UInt {
    val digits = listOf(n%10u, (n/10u)%10u, (n/100u)%10u, (n/1000u)%10u)
    val min = digits.sorted().fold(0u) { s, d -> s*10u+d }
    val max = digits.sortedDescending().fold(0u) { s, d -> s*10u+d }
    return max - min
}

fun main() {
    val random = Random(System.currentTimeMillis())
    var limits = (0u..9999u).map { iter(it) }.toSet()
    while (true) {
        val next = limits.map { iter(it) }.toSet()
        if (next==limits)
            break
        print(".");  out.flush()
        limits = next
    }
    val orbiting = limits.filter { it!= iter(it) }
    println("Interesting orbits: "+ orbiting.joinToString())
    println("Fixed points (${limits.size -orbiting.size}): "+limits.joinToString())
    repeat(100) {
        verifyOnce(random.nextInt(10_000).toUInt())
    }
    println("Valid codes: "+ (1..< 10_000).filter {
        (it%10==6 || (it/10)%10==6 || (it/100)%10==6 || (it/1000)%10==6) && iter(it.toUInt())==it.toUInt()
        }.joinToString()
    )
}

private fun verifyOnce(n0 :UInt) {
    var l = n0
    for (i in 1..100) {
        val n = iter(l)
        if (n == l)
            return
        l = n
    }
    println("Convergence fails for $n0 (within 100 steps).")
}

private fun List<Int>.findPeriod(start :Int) :Pair<UInt, UInt> {
    var current = start;  var next = start
    for (i in 1u..(size.toUInt())) {
        val v = get(current)
        if (v<0)
            return Pair(i, (-v).toUInt())
        current = v
        val v1 = get(next)
        if (v1 < 0)
            return Pair(i+i, (-v1).toUInt())
        val v2 = get(v1)
        if (v2 < 0)
            return Pair(i+i, (-v2).toUInt())
        next = v2
        if (current==next)
            return Pair(i, findLength(current))
    }
    error("Cannot find period in List.")
}

private fun List<Int>.findLength(start :Int) :UInt {
    var next = get(start)
    for (p in 1u..(size.toUInt())) {
        if (next<0)
            return (-next).toUInt()
        if (next==start)
            return p
        next = get(next)
    }
    error("Cannot find period")
}
