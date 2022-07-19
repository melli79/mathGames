package sequences

import common.math.gcd

fun findWithDisjointGcds(num :UInt, min :ULong, max :ULong) :List<ULong>? {
    val result = mutableListOf<ULong>()
    outer@for (i in 1uL..num.toULong()) {
        for (candidate in min..max) {
            val d = gcd(i, candidate)
            if (d == i) {
                result.add(candidate)
                if (i == num.toULong())
                    return result
                continue@outer
            }
        }
        return null
    }
    if (result.size == num.toInt())
        return result
    return null
}
