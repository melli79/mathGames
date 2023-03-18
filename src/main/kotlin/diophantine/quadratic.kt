package diophantine

import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * Solutions of x^2-dy^2 = 1, i.e. units in Q[√d]
 */
fun findPrimitive(d :UInt) :Pair<UInt, UInt>? {
    if (sqrt(d.toDouble()).frac()==0.0)
        return null
    for (y in 1u..cb(d.toInt()).toUInt()*sqr(d.toInt()).toUInt()) {
        val x = sqrt((d* sqr(y.toInt())+1uL).toDouble()).roundToInt()
        if (sqr(x).toLong() == d.toInt()*sqr(y.toInt()).toLong() + 1L)
            return Pair(x.toUInt(), y)
    }
    return null
}

private fun Double.frac() = this - this.toLong()
