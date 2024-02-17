package numberthy

import common.math.Rational
import common.math.gcd
import java.lang.System.out
import java.util.concurrent.atomic.AtomicInteger

private const val LIMIT = 1024

fun findForSum(sum :Rational) :List<Triple<UInt, UInt, UInt>> {
    val result = mutableListOf<Triple<UInt, UInt, UInt>>()
    if (sum< Rational.of(3,2))
        return result
    for (x in 1L..LIMIT) {
        print(".");  out.flush()
        for (y in 1L..x) {
            for (z in 1L..y) {
                if (gcd(x.toULong(), gcd(y.toULong(),z.toULong()))==1uL &&
                        Rational.of(x, y+z)+ Rational.of(y,z+y)+ Rational.of(z, x+y) == sum) {
                    result.add(Triple(x.toUInt(), y.toUInt(), z.toUInt()))
                    print("v");  out.flush()
                }
            }
        }
        if (x%100L==0L)
            println()
    }
    return result
}

fun groupBySum(limit :UInt) :Map<Rational, Pair<AtomicInteger, List<Triple<UInt, UInt, UInt>>>> {
    val result = mutableMapOf<Rational, Pair<AtomicInteger, MutableList<Triple<UInt, UInt, UInt>>>>()
    for (x in 1u..limit) {
        print(".");  out.flush()
        for (y in 1u..x) {
            for (z in 1u..y) {
                if (gcd(x.toULong(), gcd(y.toULong(),z.toULong()))==1uL) {
                    val sum = (Rational.of(x.toLong(), y.toLong() + z.toLong())
                            + Rational.of(y.toLong(), z.toLong() + x.toLong())
                            + Rational.of(z.toLong(), x.toLong() + y.toLong()))
                    if (sum.denom==1uL||sum.toDouble()<10.0) {
                        val bucket = result.computeIfAbsent(sum) { Pair(AtomicInteger(0), mutableListOf()) }
                        if (bucket.first.getAndIncrement() < 3)
                            bucket.second.add(Triple(x, y, z))
                    }
                }
            }
        }
        if (x%100u==0u)
            println()
    }
    return result
}

fun main() {
    val result = groupBySum(1024u)
    println("\nThe lowest sums are: "+ result.entries.sortedBy { it.key }.joinToString("\n") { (s, v) ->
        "$s (${v.first}: "+ v.second.joinToString()
    })
}
