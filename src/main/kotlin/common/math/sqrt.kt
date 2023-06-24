package common.math

import partitions.abs
import kotlin.math.roundToInt
import kotlin.math.sqrt

fun isqrt(x :ULong) = sqrt(x.toDouble()).roundToInt().toUInt()

fun sqr(x :Double) = x*x

fun sqr(x :Int) :ULong = abs(x).toULong()*abs(x)
