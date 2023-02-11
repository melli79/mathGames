package reedsolomon

data class Matrix(private val cs :DoubleArray, val n :UInt) {

    val m = cs.size.toUInt() / n

    init {
      assert (m*n == cs.size.toUInt())
    }

    fun clone() = Matrix(cs.clone(), n)

    fun toList() = cs.toList()

    override fun toString() = "("+ (0u until m).map { r ->
        (r*n until (r+1u)*n).map { offset -> cs[offset.toInt()] }.joinToString()
    }.joinToString(")\n(") +")"

    fun slice(rRange :UIntRange, cRange :UIntRange) :Matrix {
        val n1 = cRange.last - cRange.first +1u
        val r0 = rRange.first;  val c0 = cRange.first
        val ncs = DoubleArray(((rRange.last-rRange.first+1u) * n1).toInt())
        var offset0 = -c0.toInt()
        var offset = (r0 * n).toInt()
        for (r in rRange) {
            for (c in cRange)
                ncs[offset0 +c.toInt()] = cs[offset +c.toInt()]
            offset0 += n1.toInt()
            offset += n.toInt()
        }
        return Matrix(ncs, n1)
    }

    operator fun get(r :UInt, c :UInt) = cs[r.toInt()*n.toInt() +c.toInt()]

    operator fun set(r :UInt, c :UInt, value :Double) {
      cs[r.toInt()*n.toInt() +c.toInt()] = value
    }

    fun paste(r0 :UInt, c0 :UInt, ys :Matrix, rRange :UIntRange, cRange :UIntRange) {
        assert (r0 +rRange.last-rRange.first < m)
        assert (c0 +cRange.last -cRange.first < n)
        var offset0 = (r0*n +c0).toInt() -cRange.first.toInt()
        var offset = (rRange.first * ys.n).toInt()
        for (r in rRange) {
            for (c in cRange) {
                cs[offset0 +c.toInt()] = ys.cs[offset +c.toInt()]
            }
            offset0 += n.toInt()
            offset += ys.n.toInt()
        }
    }

    fun swap(r :UInt, r0 :UInt) {
        if (r==r0)
            return
        assert (r<m && r0<m)
        val offset = (r*n).toInt()
        val offset0 = (r0*n).toInt()
        for (c in 0 until n.toInt()) {
            val tmp = cs[offset +c]
            cs[offset +c] = cs[offset0 +c]
            cs[offset0 +c] = tmp
        }
    }

    fun scale(r :UInt, f :Double) {
        val offset = (r*n).toInt()
        for (c in 0 until n.toInt())
            cs[offset+c] *= f
    }

    fun add(r0 :UInt, r :UInt, f :Double) {
        val offset0 = (r0 * n).toInt()
        val offset = (r * n).toInt()
        for (c in 0 until n.toInt())
            cs[offset+c] += cs[offset0+c]*f
    }
}
