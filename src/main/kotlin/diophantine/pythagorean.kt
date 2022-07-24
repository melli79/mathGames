package diophantine

import kotlin.math.roundToInt
import kotlin.math.sqrt

fun findPythagoreanTriples(limit :UInt) :Set<Triple<UInt, UInt, UInt>> {
    val result = mutableSetOf(Triple(0u, 0u, 0u))
    if (limit<1u)
        return result
    (1u..limit).forEach { f -> result.add(Triple(f, 0u, f))}
    for (n in 2u..isqrt(limit.toULong()-1uL)) {
        val a0 = sqr(n.toInt()).toUInt()-1u
        val b0 = 2u*n
        val c0 = a0+2u
        result.addAll((1u..limit/c0).map { f -> Triple(f*c0,f*a0, f*b0)})
    }
    return result
}

fun sqr(x :Int) = x.toULong()*x.toULong()
fun isqrt(x :ULong) = sqrt(x.toDouble()).roundToInt().toUInt()
