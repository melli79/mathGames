package numberthy

data class IMatrix(val cs :LongArray, val n :UInt) :Cloneable {
    companion object {
        fun zero(n :UInt) = IMatrix(LongArray((n*n).toInt()){ 0L }, n)

        fun id(n :UInt) :IMatrix {
            val cs = LongArray((n*n).toInt()){ 0L }
            (0u until n).forEach { c -> cs[(c*(n+1u)).toInt()] = 1L }
            return IMatrix(cs, n)
        }
    }

    init {
        assert(n*n == cs.size.toUInt())
    }

    override fun clone() = IMatrix(cs.clone(), n)

    override fun toString() = "(" +(0u until n).joinToString(if (n<4u) "; "  else ")\n(") { r ->
        cs.slice((r*n).toInt() until ((r+1u)*n).toInt()).joinToString()
    } +")"

    operator fun get(r :UInt, c :UInt) = cs[(r*n+c).toInt()]

    operator fun set(r :UInt, c :UInt, value :Long) {
        assert(r<=n)
        assert(c<=n)
        cs[(r*n +c).toInt()] = value
    }

    override fun equals(other :Any?) :Boolean {
        if (other !is IMatrix) return false
        return n==other.n && cs contentEquals other.cs
    }

    override fun hashCode() = 2 +n.hashCode() +31* cs.contentHashCode()

    fun copy(pad :UInt) :IMatrix {
        val cs = (0u until n).flatMap { r ->
            val rOff = (r*n).toInt()
            (0 until n.toInt()).map { c -> cs[rOff+c] } +(0u until pad).map { 0L }
        }
        return IMatrix(cs.toLongArray() + LongArray((pad*(n+pad)).toInt()) { 0L }, n+pad)
    }

    fun paste(r0 :UInt, c0 :UInt, src :IMatrix) {
        assert(r0+src.n <= n)
        assert(c0+src.n <= n)
        (0u until src.n).forEach { r ->
            val rOff = ((r0+r)*n +c0).toInt()
            val sOff = (r*src.n).toInt()
            (0 until src.n.toInt()).forEach { c ->
                cs[rOff +c] = src.cs[sOff +c]
            }
        }
    }

    operator fun plus(s :IMatrix) :IMatrix {
        assert(n == s.n)
        return IMatrix(cs.zip(s.cs).map { (s, s2) -> s+s2 }.toLongArray(), n)
    }

    private fun addRow(r :UInt, r0 :UInt, f :Long) {
        if (f==0L)
            return
        if (r==r0) {
            scaleRow(r, 1L -f)
            return
        }
        val rOff = (r*n).toInt()
        val sOff = (r0*n).toInt()
        for (c in 0 until n.toInt()) {
            cs[rOff+c] += f*cs[sOff+c]
        }
    }

    private fun scaleRow(r :UInt, f :Long) {
        assert(r<n)
        val rOff = (r*n).toInt()
        (0 until n.toInt()).forEach { c ->
            cs[rOff+c] *= f
        }
    }

    fun swap(r0 :UInt, r1 :UInt) {
        if (r0==r1)
            return
        assert(r0 < n)
        assert(r1 < n)
        val rOff = (r0*n).toInt()
        val sOff = (r1*n).toInt()
        (0 until n.toInt()).forEach { c ->
            val tmp = cs[rOff+c]
            cs[rOff+c] = cs[sOff+c]
            cs[sOff+c] = tmp
        }
    }

    operator fun times(f :Long) :IMatrix {
        if (f==1L)
            return this
        if (f==0L)
            return zero(n)
        return IMatrix(cs.map { it*f }.toLongArray(), n)
    }

    operator fun times(f :IMatrix) :IMatrix {
        assert(n == f.n)
        val cs = (0u until n).flatMap { r ->
            val rOff = (r*n).toInt()
            (0u until n).map { c ->
                (0 until n.toInt()).toList().foldRight(0L) { k :Int, s :Long -> s +
                        cs[rOff+k]*f.cs[k*n.toInt() +c.toInt()]
                }
            }
        }
        return IMatrix(cs.toLongArray(), n)
    }

    fun l1() = cs.sumOf { abs(it) }

    fun frob2() = cs.sumOf { it*it }

    private fun divRow(r :UInt, d :Long) {
        assert(r < n)
        val rOff = (r*n).toInt()
        for (c in 0 until n.toInt()) {
            if (cs[rOff+c]%d != 0L)
                throw IMatrixException("row $r cannot be downscaled by $d")
            cs[rOff+c] /= d
        }
    }

    fun det() :Long = when(n) {
        0u -> 0L
        1u -> cs[0]
        2u -> cs[0]*cs[3] -cs[1]*cs[2]
        3u -> get(0u,0u)*get(1u,1u)*get(2u,2u) -get(0u,2u)*get(1u,1u)*get(2u,0u) +
                get(0u,1u)*get(1u,2u)*get(2u,0u) -get(0u,1u)*get(1u,0u)*get(2u,3u) +
                get(0u,2u)*get(1u,0u)*get(2u,1u) -get(0u,0u)*get(1u,2u)*get(2u,1u)
        else -> TODO("unimplemented")
    }

    fun inv() :IMatrix {
        if (abs(det()) != 1uL)
            throw IMatrixException("Cannot invert Matrix with non-invertible determinant.")
        return id(n).ldiv(this)
    }

    fun ldiv(d :IMatrix) :IMatrix {
        assert(n == d.n)
        val m = d.copy(n);  m.paste(0u, n, this)
        m.gaussElim(n)
        return m.backSubst(n)
    }

    private fun backSubst(n :UInt) :IMatrix {
        var c = n-1u
        for (r0 in (0u until n).reversed()) {
            for (r in 0u until r0) {
                addRow(r, r0, -get(r, c))
            }
            if (c>0uL)
                c--
            else
                break
        }
        return slice(0u, n, n)
    }

    fun slice(r0 :UInt, c0 :UInt, size :UInt) :IMatrix {
        if (r0==0u && c0==0u && size==n)
            return this
        assert(r0+size <= n)
        assert(c0+size <= n)
        val cs = (0u until size).flatMap { r ->
            val rOff = ((r+r0)*n +c0).toInt()
            (0 until size.toInt()).map { c -> cs[rOff+c] }
        }
        return IMatrix(cs.toLongArray(), size)
    }

    private fun gaussElim(n :UInt) {
        var r0 = 0u
        for (c in 0u until n) {
            val r1 = argMax(r0, c) { abs(it) } // { if (it>0uL) -it.toLong()  else Long.MIN_VALUE }
            swap(r0, r1)
            val d = get(r0, c)
            if (d==0L)
                throw IMatrixException("Matrix is singular")
            divRow(r0, d)
            (r0+1u until n).forEach { r ->
                addRow(r, r0, get(r, c))
            }
            r0++
        }
    }

    fun <T :Comparable<T>> argMax(r0 :UInt, c :UInt, max :(Long) -> T) :UInt {
        assert(r0<n)
        assert(c<n)
        var result = r0; var m = max(get(r0, c))
        for (r in r0+1u until n) {
            val m1 = max(get(r, c))
            if (m1>m) {
                result = r;  m = m1
            }
        }
        return result
    }

    fun rdiv(d :IMatrix) = transpose().ldiv(d.transpose()).transpose()

    private fun transpose() :IMatrix {
        val cs = (0u until n).flatMap { r ->
            (0u until n).map { c -> get(c, r) }
        }
        return IMatrix(cs.toLongArray(), n)
    }
}

class IMatrixException(msg :String ="Operation not permitted in integer matrices") :Throwable(msg) {}

fun IMatrix.isMplus() = cs.all { it >= 0L }

fun abs(x :Long) = when {
    x >= 0L -> x.toULong()
    else -> (-x).toULong()
}
