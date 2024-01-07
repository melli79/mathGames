package common.math.geometry

import common.math.epsilon
import common.math.linspace
import common.math.sgn
import kotlin.math.abs
import kotlin.math.sqrt

fun List<Point2D>.bezier(t :Double) :Point2D {
    if (size==1)
        return first()
    var qs = windowed(2) { ps -> ps[0].affine(ps[1], t) }
    while (qs.size>1)
        qs = qs.windowed(2) { ps -> ps[0].affine(ps[1], t) }
    return qs.first()
}

fun List<Point2D>.findPosition(p :Point2D) :Position {
    val dt0 = sqrt(epsilon)
    var t = linspace(0.0, 1.0, 0.02).minByOrNull { (p-bezier(it)).norm2() }!!
    var off2 = (p-bezier(t)).norm2()
    var lastT = if (t>=0.01) t -0.01  else 0.01
    var lastOff2 = (p-bezier(lastT)).norm2()
    while (t-lastT > dt0) {
        val dt = t - lastT
        val dy = off2 - lastOff2
        if (abs(dy) < 5*dt0)
            break
        val t1 = t -off2/dy*dt
        lastT = t;  lastOff2 = off2
        t = t1;  off2 = (p-bezier(t)).norm2()
        if (lastOff2<off2) {
            t = lastT;  off2 = lastOff2
            break
        }
    }
    val p0 = bezier(t)
    val s = sgn((p - p0).dot((bezier(t + dt0) - p0).perp()))
    return Position(t, s * sqrt(off2))
}

fun List<Point2D>.computeDistanceTraveled(t1 :Double) :Double {
    var oldP = bezier(0.0)
    var l = 0.0
    if (t1 < sqrt(epsilon))
        return l
    for (t in linspace(0.0, t1, t1 / 100)) {
        val p = bezier(t)
        l += sqrt((p - oldP).norm2())
        oldP = p
    }
    return l
}

data class Position(val lambda :Double, val off :Double) {

}