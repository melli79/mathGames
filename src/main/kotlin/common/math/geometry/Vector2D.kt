package common.math.geometry

import kotlin.math.sqrt

data class Vector2D(val x :Double, val y :Double) {
    companion object {
        val ZERO = Vector2D(0.0, 0.0)
    }

    override fun toString() ="(%+.2g, %+.2g)".format(x, y)

    operator fun times(f :Double) = Vector2D(f*x, f*y)

    operator fun plus(v :Vector2D) = Vector2D(x+v.x, y+v.y)
    operator fun unaryMinus() = Vector2D(-x, -y)

    fun dot(v :Vector2D) = x*v.x +y*v.y
    fun norm2() = x*x +y*y
    fun perp() = Vector2D(-y, x)
    fun normed() = times(1/sqrt(norm2()))
}
