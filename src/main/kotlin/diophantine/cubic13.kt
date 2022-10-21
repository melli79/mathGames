package diophantine

import kotlin.math.absoluteValue

/**
 * Solutions for X+Y+Z = XYZ, i.e. X = (YZ-1)/(Y+Z)
 */
fun findTriples13(limit :UInt) :Set<Set<Int>> {
    val result = mutableSetOf(setOf(0))
    for (z in 1..limit.toInt()) {
        result.add(setOf(-z,0,z))
        for (y in 1..z) {
            val npp = y+z;  val dpp = y*z -1
            if (dpp!=0 && npp%dpp==0) {
                val x = npp/dpp
                if (x!=0 && x.absoluteValue<=limit.toInt()) {
                    result.add(setOf(x, y, z))
                    result.add(setOf(-x, -y, -z))
                }
            }
            val npn = z-y;  val dpn = -y*z -1
            if (npn%dpn==0) {
                val x = npn/dpn
                if (x!=0 && x.absoluteValue<=limit.toInt()) {
                    result.add(setOf(x, -y, z))
                    result.add(setOf(-x, y, -z))
                }
            }
        }
    }
    return result
}
