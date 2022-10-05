package incidence

import common.math.gcd
import kotlin.math.abs

data class Plane(val n :UInt) {
    override fun toString() = "I_2($n)"
    override fun hashCode() = 31+ n.hashCode()
    override fun equals(other :Any?) :Boolean {
        if (other !is Plane) return false
        return n==other.n
    }

    operator fun contains(p :Point2D) = p.p==this
    operator fun contains(l :Line) = l.p==this

    val origin = Point2D.origin(this)
}

data class Point2D(val p :Plane, val x :UInt, val y :UInt) {
    companion object {
        fun of(p :Plane, x :UInt, y :UInt) = Point2D(p, x%p.n, y%p.n)
        fun origin(p :Plane) = Point2D(p, 0u, 0u)
    }

    override fun toString() = "($x; $y)"

    operator fun minus(o :Point2D) = Vector2D(x.toInt()-o.x.toInt(), y.toInt()-o.y.toInt())
}

data class Vector2D(val x :Int, val y :Int) {
    companion object {
        val ZERO = Vector2D(0, 0)
    }

    override fun toString() ="($x, $y)"

    fun dot(v :Vector2D) = x*v.x +y*v.y
    fun perp() = Vector2D(-y, x)
    fun normed() :Vector2D? {
        val f = gcd(abs(x).toULong(), abs(y).toULong()).toInt()
        if (f!=0)
            return Vector2D(x/f, y/f)
        return null
    }
}

data class Line(val p :Plane, val d :UInt, val v :Vector2D) {
    companion object {
        fun of(p :Plane, d :UInt, v :Vector2D) :Line? {
            if (v.x!=0) {
                val f = gcd(gcd(
                    p.n.toULong(), abs(v.x).toULong()), abs(v.y).toULong()).toInt()
                return Line(p, d%p.n, Vector2D((v.x/f)%p.n.toInt(), (v.y/f)%p.n.toInt()))

            } else if (v.y%p.n.toInt()!=0)
                return Line(p, d%p.n, Vector2D(0, 1))

            return null
        }

        fun of(p :Point2D, q :Point2D) :Line? {
            val v = (q-p).perp()
            val v0 = v.normed() ?: return null
            return Line(p.p, d(p, v0, p.p.n), v0)
        }

        private fun d(p :Point2D, v0 :Vector2D, n :UInt) :UInt {
            val r = (-(p -Point2D.origin(p.p)).dot(v0)) % n.toInt()
            return (n.toInt()+r).toUInt() % n
        }
    }

    override fun toString() = "l($d, $v)"
    operator fun contains(p :Point2D) = ((p- Point2D.origin(p.p)).dot(v)+d.toInt() ) %this.p.n.toInt() == 0
}
