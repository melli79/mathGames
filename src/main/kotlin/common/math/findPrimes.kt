package common.math

fun findPrimes(limit :UInt) :List<UInt> {
    val result = mutableListOf(2u)
    if (limit<=2u) {
        return result
    }
    val sieve = (0u ..limit/2u+isqrt(limit.toULong())).map { true }.toMutableList()
    var p = 3u
    while (p*p<=limit) {
        result.add(p)
        for (k in p/2u..(limit/p/2u)) {
            val f = 2u*k +1u
            sieve[(f*p).toInt()/2] = false
        }
        do {
            p+= 2u
        } while (p<limit && !sieve[(p/2u).toInt()])
    }
    while (p<limit) {
        if (sieve[(p/2u).toInt()])
            result.add(p)
        p += 2u
    }
    return result
}