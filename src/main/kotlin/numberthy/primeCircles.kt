package numberthy

import common.math.findPrimes
import einstein.next
import einstein.removeFirst

@OptIn(ExperimentalUnsignedTypes::class)
fun findAll(n :UInt) :Set<UIntArray> {
    val primes = findPrimes(4u*n).toSet()
    val result = mutableSetOf<UIntArray>()
    if (n<1u)
        return setOf(uintArrayOf())
    val candidate = mutableListOf(1u)
    val options = (2u..2u*n).toMutableSet()
    candidate.add(options.removeFirst()!!)
    while (candidate.size>1) {
        while (options.isNotEmpty()) {
            var k = options.removeFirst()
            while (k!=null) {
                if (candidate.last()+k in primes) {
                    candidate.add(k)
                    break
                }
                k = options.next(k)
            }
            if (k==null)
                break
        }
        if (candidate.size==2*n.toInt()) {
            if (candidate.first()+candidate.last() in primes)
                result.add(candidate.toUIntArray())
        }
        while (candidate.size>1) {
            val k = options.next(candidate.removeLast())
            if (k!=null) {
                candidate.add(k)
                if (candidate.last()+candidate.preLast() in primes)
                    break
            }
        }
    }
    return result
}

fun <E> List<E>.preLast() = get(size-2)
