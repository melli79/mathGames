package common.math.statistics

fun factorial(n :UShort) :ULong = (2u..n.toUInt()).fold(1uL) { p, i -> p*i }

fun choose(n :UShort, k :UShort) :ULong {
    val k = if (k<n/2u) k  else (n-k).toUShort()
    return ((n-k+1u)..n.toUInt()).fold(1uL) { p, i -> p*i } / factorial(k)
}
