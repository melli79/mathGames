package reedsolomon

import common.math.ipow

@OptIn(ExperimentalUnsignedTypes::class)
data class Fq private constructor(val p :FpPolynomial, val xs :ULongArray) :Comparable<Fq> {
    companion object {
        var alpha = "𝛼"

        fun zero(p :FpPolynomial) :Fq {
            assert (p.deg>1)
            assert(isIrr(p))
            return Fq(p, (1..p.deg).map { 0uL }.toULongArray())
        }
        fun one(p :FpPolynomial) :Fq {
            assert (p.deg>1)
            assert(isIrr(p))
            return Fq(p, (listOf(1uL)+(2..p.deg).map { 0uL }).toULongArray())
        }

        fun alpha(p :FpPolynomial) :Fq {
            assert (p.deg>1)
            assert(isIrr(p))
            return Fq(p, (listOf(0uL, 1uL)+(3..p.deg).map { 0uL }).toULongArray())
        }

        fun gen(p :FpPolynomial) :Fq {
            assert (p.deg>1)
            assert(isIrr(p))
            return Fq(p, (1..p.deg).map { 1uL }.toULongArray())
        }

        fun of(x :Fq) = Fq(x.p, x.xs)
    }

    val deg = p.deg.toUByte()

    override fun equals(other :Any?) :Boolean {
        if (other !is Fq)
            return false
        return p==other.p && xs contentEquals other.xs
    }

    override fun toString() :String {
        if (xs.all { it==0uL })
            return "0"
        return xs.mapIndexed { d: Int, x: ULong -> Pair(x, d) }
            .filter { (x, _) -> x!=0uL }
            .joinToString("+") { (x, d) -> when(d) {
                0 -> x.toString()
                1 -> if (x==1uL) alpha  else "$x$alpha"
                else -> if (x==1uL) "$alpha^$d"  else "$x$alpha^$d"
            } }
    }

    operator fun plus(s :Fq) :Fq {
        assert(p==s.p)
        return Fq(p, xs.zip(s.xs).map { (s1, s2) -> (s1+s2)%p.p }.toULongArray())
    }

    operator fun unaryMinus() = Fq(p, xs.map { x -> (p.p-x)%p.p }.toULongArray())

    operator fun minus(s :Fq) :Fq {
        assert(p==s.p)
        return Fq(p, xs.zip(s.xs).map { (s1, s2) -> (s1+p.p-s2)%p.p }.toULongArray())
    }

    operator fun times(f :Fp) :Fq {
        assert(p.p==f.p)
        if (f.a==0uL)
            return zero(p)
        if (f.a==1uL)
            return this
        return Fq(p, xs.map { x -> (x*f.a)%p.p }.toULongArray())
    }

    fun isInFp() :Boolean {
        return xs.drop(1).all { it==0uL }
    }

    fun re() = Fp(p.p, xs[0])

    operator fun times(f :Fq) :Fq {
        assert(p==f.p)
        if (f.isInFp())
            return times(f.re())
        if (this.isInFp())
            return f.times(re())
        val result = (FpPolynomial(p.p, xs)*FpPolynomial(p.p, f.xs)).div(p).second
        val xs = result.cs + (result.cs.size until p.deg).map { 0uL }
        return Fq(p, xs.toULongArray())
    }

    fun multiplier() :FpMartrix {
        val result = xs.toMutableList()
        val alpha = alpha(p)
        var p = alpha
        for (d in 1 until deg.toInt()) {
            result.addAll(times(p).xs)
            p *= alpha
        }
        return FpMartrix(p.p.p, deg.toUInt(), result.toULongArray())
    }

    fun norm() = multiplier().det()

    fun inv() :Fq {
        if (isInFp())
            return Fq(p, (listOf(Fp(p.p, xs[0]).inv().a) +(1 until p.deg).map { 0uL }).toULongArray())
        val result = euclid(p, FpPolynomial.ofU(p.p, xs.toList()))
        assert(result.third.deg==0)
        return Fq(p, (result.second/result.third.lc()).cs)
    }

    fun range() = FqRange(p, 0u, p.p.ipow(p.deg.toUByte())-1uL)

    fun rangeMult() = FqRange(p, 1u, p.p.ipow(p.deg.toUByte())-1uL)

    @OptIn(ExperimentalStdlibApi::class)
    class FqRange(val p :FpPolynomial, private val begin :ULong, private val end :ULong) :OpenEndRange<Fq> {
        override val endExclusive :Fq
            get() = alpha(p)
        override val start = alpha(p)

        fun toList() :List<Fq> {
            val alpha = gen(p)
            var p = alpha
            val result = if (begin==0uL) mutableListOf(zero(alpha.p), p)  else mutableListOf(p)
            for (e in begin+1u until end) {
                p *= alpha
                result.add(p)
            }
            return result
        }
    }

    override fun compareTo(other :Fq) :Int = Comparator
        .comparing<Fq, FpPolynomial> { it.p }
        .thenComparing { alpha, beta ->
            alpha.xs.zip(beta.xs).foldRight(0){ (a,b), c :Int -> if (c!=0) c  else
                a.compareTo(b)
        } }.compare(this, other)
}
