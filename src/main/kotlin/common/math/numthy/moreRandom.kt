package common.math.numthy

import kotlin.random.Random
import kotlin.random.nextUInt

@OptIn(ExperimentalUnsignedTypes::class)
fun Random.matp(p :UInt, m :Int, n :Int) = Matp(p, Array(m) {
    UIntArray(n) { nextUInt(p) }
})

@OptIn(ExperimentalUnsignedTypes::class)
fun Random.vecp(p :UInt, n :Int) = Vecp(p, UIntArray(n) { nextUInt(p) })
