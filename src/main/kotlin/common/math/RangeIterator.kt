package common.math

fun linspace(x0 :Double, x1 :Double, dx :Double) = RangeIterator(x0, x1, dx)

data class RangeIterator(val x0 :Double, val x1 :Double, val dx :Double, private var x :Double =x0) : Iterator<Double> {
    override fun hasNext() = x < x1+ epsilon

    override fun next() :Double {
        val result = x
        x += dx
        return result
    }
}