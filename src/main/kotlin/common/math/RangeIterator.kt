package common.math

fun linspace(x0 :Double, x1 :Double, dx :Double) = RangeIterator(x0, x1, dx)

data class RangeIterator(val x0 :Double, val x1 :Double, val dx :Double, private var x :Double =x0) : Iterator<Double> {
    override fun hasNext() = x < x1+ epsilon

    override fun next() :Double {
        val result = x
        x += dx
        return result
    }

    fun toList() :List<Double> {
        val result = mutableListOf<Double>()
        for (t in this) {
            result.add(t)
        }
        return result
    }

    fun maxByOrNull(maximizer :(Double)->Double) :Double? {
        var result :Double? =null;  var m :Double? =null
        while (hasNext()) {
            val t = next()
            val v = maximizer(t)
            if (m==null || m<v) {
                result = t;  m = v
            }
        }
        return result
    }

    fun minByOrNull(minimizer :(Double)->Double) :Double? {
        var result :Double? =null;  var m :Double? =null
        while (hasNext()) {
            val t = next()
            val v = minimizer(t)
            if (m==null || v<m) {
                result = t;  m = v
            }
        }
        return result
    }
}
