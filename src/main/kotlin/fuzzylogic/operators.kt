package fuzzylogic

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun fuzzyAnd(x :Double, y :Double) = min(x, y)

fun fuzzyOr(x :Double, y :Double) = max(x, y)

fun fuzzyTranspose(x :Double, y :Double) = if (x+y<=1.0)
    x+y
else
    2 -(x+y)

fun fuzzyDiamond(x :Double, y :Double) = max(min(1-x, y), min(1-y, x))

fun fuzzyEq(x :Double, y :Double) = 1-abs(x - y)
