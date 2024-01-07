package common.math

fun sgn(x :Double) = when {
    x > 0 -> 1
    x < 0 -> -1
    else -> 0
}