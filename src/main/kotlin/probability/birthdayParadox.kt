package probability

fun findThreshold(n :UInt, thresholds :DoubleArray = doubleArrayOf(0.5)) :DoubleArray {
    val p0s = thresholds.sortedDescending()
    val it = p0s.iterator()
    val result = mutableListOf<Double>()
    var p0 :Double
    while (true) {
        if (!it.hasNext())
            return result.toDoubleArray()
        p0 = it.next()
        if (p0 < 1.0)
            break
        result.add(0.0)
    }
    var p = 1.0;  var lastP = p
    for (k in 1u until n) {
        p *= 1-k.toDouble()/n.toInt()
        while (p<p0) {
            result.add(k.toDouble()+(lastP-p0)/(lastP-p))
            if (!it.hasNext())
                return result.toDoubleArray()
            p0 = it.next()
        }
        if (p==0.0)
            break
        lastP = p
    }
    p = 0.0;  val k = n
    while (true) {
        if (p0<0.0)
            result.add(n.toInt()+1.0)
        else
            result.add(k.toDouble()+(lastP-p0)/(lastP-p))
        if (!it.hasNext())
            break
        p0 = it.next()
    }
    return result.toDoubleArray()
}
