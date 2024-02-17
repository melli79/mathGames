package common.math.geometry

import common.math.epsilon
import common.math.sqr
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

data class Circle(val center :Point2D, val radius :Double) {
    fun intersect(c2 :Circle) :Set<Point2D> {
        val v = c2.center - center
        val d = sqrt(v.norm2())
        if (d < epsilon || min(radius, c2.radius) + d < max(radius, c2.radius) || radius + c2.radius < d)
            return emptySet()
        val l = (sqr(radius) - sqr(c2.radius)) / d
        val h2 = sqr(radius) - sqr(l)
        if (h2 < 3 * epsilon *(radius*abs(l) +1.0))
            return setOf(center.translate(v.normed() * l))
        val e = v*(1/d)
        val b = center.translate(e * l)
        val p = e.perp() * sqrt(h2)
        return setOf(b.translate(p), b.translate(-p))
    }
}
