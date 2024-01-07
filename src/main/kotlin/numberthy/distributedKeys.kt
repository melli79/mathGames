package numberthy

import kotlin.random.Random

val random = Random(System.currentTimeMillis())

object KeyDistributions {
    private val distributions = mutableListOf<MutableList<Set<Set<UShort>>>>()
    fun minDistribution(s :UShort, h :UShort) :List<Set<UShort>> {
        check(s<=h) { "You need more holders." }
        val result = (1..h.toInt()).map { (1u..s.toUInt()).map { it.toUShort() }.toMutableSet() }
        if (s<=1u)
            return result
        result.reduce(s, h)
        return result
    }

    private fun List<MutableSet<UShort>>.reduce(s :UShort, numH :UShort) {
        val N = choose(numH, s)
        repeat(numH.toInt()*s.toInt()) {
            val h = random.nextInt(numH.toInt())
            val keys = get(h)
            if (keys.size>1) {
                val k = keys.random(random)
                keys.remove(k)
                if ((1uL..N).any { !verifyOnce(s) }) {
                    keys.add(k)
                }
            }
        }
    }

    private fun List<Set<UShort>>.verifyOnce(s :UShort) :Boolean {
        val holders = mutableSetOf<Int>()
        val keys = (1..s.toInt()).map { it.toUShort() }.toMutableSet()
        while (holders.size<s.toInt()) {
            val h = random.nextInt(size)
            if (h !in holders) {
                keys.removeAll(get(h))
                holders.add(h)
            }
        }
        return keys.isEmpty()
    }
}

fun main() {
    val ns = mutableMapOf<Int, Int>()
    var min = choose(10u, 7u).toInt()
    repeat(100_000) {
        val n = KeyDistributions.minDistribution(7u, 10u).sumOf { it.size }
        ns[n] = (ns[n] ?: 0) + 1
        if (n<min) {
            min = n
            println("Better solution: $min")
        }
    }
    println("Good choices for s=7, k=10:  "+ ns.entries.sortedBy { it.key }.take(5)
        .joinToString { "${it.key}: ${it.value}" })
}

fun choose(n :UShort, k :UShort) :ULong {
    if (k>n)
        return 0uL
    if (k>n/2u)
        return choose(n, (n-k).toUShort())
    return ((n-k+1u)..n.toUInt()).fold(1uL){ p :ULong, f -> p*f.toULong() } /
            (2u..k.toUInt()).fold(1uL){ p :ULong, f -> p*f.toULong() }
}
