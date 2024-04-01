package common.math.statistics

import kotlin.math.exp
import kotlin.math.pow

fun poisson(k :UInt, lambda :Double) = lambda.pow(k.toInt())*exp(-lambda)/factorial(k)

fun factorial(n :UInt) :Double {
    var result = 1.0
    for (k in 2u..n)
        result *= k.toDouble()
    return result
}
