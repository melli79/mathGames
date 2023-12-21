package common.math.geometry

import common.math.gcd
import kotlin.math.abs

data class Vector2D(val x :Double, val y :Double) {
    companion object {
        val ZERO = Vector2D(0.0, 0.0)
    }

    override fun toString() ="(%+.2g, %+.2g)".format(x, y)

    fun dot(v :Vector2D) = x*v.x +y*v.y
    fun perp() = Vector2D(-y, x)
    fun normed() :Vector2D? {
        val f = gcd(abs(x).toULong(), abs(y).toULong()).toInt()
        if (f!=0)
            return Vector2D(x / f, y / f)
        return null
    }

    operator fun times(f :Double) = Vector2D(f*x, f*y)

    operator fun plus(v :Vector2D) = Vector2D(x+v.x, y+v.y)
}
