package diophantine

import java.lang.StrictMath.cbrt
import kotlin.math.abs
import kotlin.math.roundToInt

fun findSumOfCubes(limit :UInt) :Map<UInt, Set<Triple<Int, Int, Int>>> {
    val result = mutableMapOf(Pair(0u, mutableSetOf(Triple(0, 0, 0))))
    val tol = icbrt(limit.toLong())
    for (x in 1..30*tol) {
        val c = cb(x)
        for (y in -x..x) {
            val s2 = c + cb(y)
            val z0 = -icbrt(s2)
            for (z in z0-tol..z0+2*tol) {
                if (abs(z) <= abs(y)) {
                    val sum = s2 + cb(z)
                    if (0<=sum && sum<=limit.toLong()) {
                        result.computeIfAbsent(sum.toUInt()) { mutableSetOf() }.add(Triple(x, y, z))
                    }
                }
            }
        }
    }
    return result
}

fun findSumOf4Cubes(limit :UInt) :Map<UInt, Set<Quadruple<Int, Int, Int, Int>>> {
    val result = mutableMapOf(Pair(0u, mutableSetOf(Quadruple(0, 0, 0, 0))))
    val tol = icbrt(limit.toLong())
    for (x in 1..2*tol) {
        val c = cb(x)
        for (y in -x..x) {
            val s2 = c + cb(y)
            val z0 = icbrt(-s2)
            for (z in z0-tol..z0+3*tol) if (abs(z)<=abs(y)) {
                val s3 = s2 + cb(z)
                val w0 = -icbrt(s3)
                for (w in w0-tol..w0+2*tol) {
                    if (abs(w) <= abs(z)) {
                        val sum = s3 + cb(w)
                        if (0 <= sum && sum <= limit.toLong()) {
                            result.computeIfAbsent(sum.toUInt()) { mutableSetOf() }.add(Quadruple(x, y, z, w))
                        }
                    }
                }
            }
        }
    }
    return result
}

fun cb(x :Int) = x.toLong()*x*x
fun icbrt(x :Long) = cbrt(x.toDouble()).roundToInt()

data class Quadruple<R,S,T,U>(val first :R, val second :S, val third :T, val fourth :U)

fun <R :Comparable<R>,S :Comparable<S>,T :Comparable<T>,U :Comparable<U>> Quadruple<R,S,T,U>.compareTo(q :Quadruple<R,S,T,U>) :Int {
    val cmp1 = first.compareTo(q.first)
    if (cmp1!=0)  return cmp1
    val cmp2 = second.compareTo(q.second)
    if (cmp2!=0)  return cmp2
    val cmp3 = third.compareTo(q.third)
    if (cmp3!=0)  return cmp3
    return fourth.compareTo(q.fourth)
}
