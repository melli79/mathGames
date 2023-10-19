package diophantine

import common.math.gcd
import common.math.isqrt

fun findPythagoreanTriples(limit :UInt) :Set<Triple<UInt, UInt, UInt>> {
    val result = mutableSetOf(Triple(0u, 0u, 0u))
    if (limit<1u)
        return result
    (1u..limit).forEach { f -> result.add(Triple(f, 0u, f))}
    for (m in 2u..isqrt(limit.toULong()-1uL)) {
        var n = 1u
        while (n<m) {
            if (gcd(m.toULong(), n.toULong())==1uL) {
                val a0 = sqr(m.toInt()).toUInt() - sqr(n.toInt()).toUInt()
                val b0 = 2u *m * n
                val c0 = a0 + 2u*sqr(n.toInt()).toUInt()
                result.addAll((1u..limit / c0).map { f -> Triple(f * c0, f * a0, f * b0) })
            }
            n++
        }
    }
    return result
}

fun sqr(x :Int) = x.toULong()*x.toULong()
