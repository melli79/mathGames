package common.math

import kotlin.math.roundToInt
import kotlin.math.sqrt

fun isqrt(x :ULong) = sqrt(x.toDouble()).roundToInt().toUInt()