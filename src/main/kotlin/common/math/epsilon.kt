package common.math

import kotlin.math.abs

val epsilon = 1e-12
fun approxEquals(a :Double, b :Double) = abs(a - b) < epsilon *(1+ abs(a) + abs(b))