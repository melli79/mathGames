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

fun cb(x :Int) = x.toLong()*x*x
fun icbrt(x :Long) = cbrt(x.toDouble()).roundToInt()
