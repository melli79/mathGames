package partitions

import kotlin.math.sqrt

fun findShortestSquarePartition(n :UInt) :List<UInt> {
    if (n==0u)
        return emptyList()
    if (n==1u)
        throw IllegalArgumentException("Cannot be solved for $n.")
    if (n==2u)
        return listOf(1u, 1u, 1u, 1u)
    var remainder = sqr(n)
    var next = n-1u
    val n1 = isqrt(next.toULong())
    if (next==sqr(n1).toUInt())
        return listOf(next -1u, 2u*n1)
    val result = mutableListOf<UInt>()
    while (remainder>0uL) {
        while (next>1u && sqr(next)>remainder)
            next--
        if (next == 0u)
            throw IllegalArgumentException("Cannot be solved for $n.")
        result.add(next)
        remainder -= sqr(next)
    }
    return result
}

fun sqr(n :UInt) = n.toULong()*n.toULong()
fun isqrt(x :ULong) = sqrt(x.toDouble()).toUInt()
