package reedsolomon

import numberthy.euclid
import kotlin.math.max

data class Fp internal constructor(val p :ULong, val a :ULong) :Comparable<Fp> {
    companion object {
        fun of(p :ULong, a :Long) :Fp {
            assert (p>=2uL)
            assert (p==2uL || p%2uL>0uL)
            return if (a>0L)  Fp(p, a.toULong()%p)  else Fp(p, (p- (-a).toULong()%p)%p)
        }

        fun zero(p :ULong) = of(p, 0)
        fun one(p :ULong) = of(p, 1)
    }

    override fun toString() = "$a (mod $p)"

    operator fun inc() = Fp(p, (a+1uL)%p)

    operator fun dec() = Fp(p, (p+a-1uL)%p)

    operator fun plus(f :Fp) :Fp {
        assert (p==f.p)
        return  Fp(p, (a+f.a)%p)
    }

    operator fun unaryMinus() = Fp(p, (p-a)%p)

    operator fun minus(s :Fp) :Fp {
        assert (p==s.p)
        return Fp(p, (a+p-s.a)%p)
    }

    operator fun times(f :Fp) :Fp {
        assert (p==f.p)
        return  Fp(p, (a*f.a)%p)
    }

    fun inv() :Fp {
        assert(a!=0uL)
        return of(p, euclid(a.toLong(), p.toLong()).first)
    }

    operator fun div(d :Fp) = times(d.inv())

    @OptIn(ExperimentalStdlibApi::class)
    fun range() :Range {
        return Range(p, 0uL, p)
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun rangeMult() :Range {
        return Range(p, 1uL, p)
    }

    @OptIn(ExperimentalStdlibApi::class)
    class Range(val p :ULong, private val begin :ULong, private val endEx :ULong) :OpenEndRange<Fp> {
        override val endExclusive :Fp
            get() = Fp(p, endEx)
        override val start :Fp
            get() = Fp(p, begin)

        fun toList() = (begin until endEx).map { a -> Fp(p, a) }

        fun asSequence() = (begin until endEx).asSequence().map { a -> Fp(p, a) }
    }

    override fun compareTo(other :Fp) :Int {
        val cmp = p.compareTo(other.p)
        return when(cmp) {
            0 -> a.compareTo(other.a)
            else -> 2*cmp
        }
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
data class FpPolynomial internal constructor(val p :ULong, val cs :ULongArray) :Comparable<FpPolynomial> {
    companion object {
        fun of(p :ULong, cs :List<Long>) = ofU(p, cs.map { c -> if (c<0&&p>0uL) p-(-c).toULong()%p  else c.toULong() })

        fun ofU(p :ULong, cs :List<ULong>) :FpPolynomial {
            assert (p>=2uL)
            assert (p==2uL || p%2uL>0uL)
            val coeffs :List<ULong> = cs.map { a -> a%p }
            val deg1 = coeffs.mapIndexed { d, a -> Pair(d, a) }.findLast { it.second!=0uL }?.first
                ?: return FpPolynomial(p, ulongArrayOf(0uL))
            return FpPolynomial(p, coeffs.subList(0, deg1+1).toULongArray())
        }

        fun of(c0 :Fp) = FpPolynomial(c0.p, ulongArrayOf(c0.a))

        fun zero(p :ULong) = of(Fp.zero(p))

        fun one(p :ULong) = of(Fp.one(p))

        fun x(p :ULong) = ofU(p, listOf(0u, 1u))

        fun xPow(p :ULong, d :UInt) :FpPolynomial {
            val cs = ULongArray(d.toInt()+1)
            cs[d.toInt()] = 1uL
            return FpPolynomial(p, cs)
        }
    }

    val deg = if (cs.size>1) cs.size-1  else if (cs.first()!=0uL) 0  else -1

    fun lc() = Fp(p, cs.last())

    override fun toString() :String {
        if (deg<1)
            return lc().toString()

        return cs.mapIndexed { d :Int, c :ULong -> Pair(d, c) }.reversed().filter { it.second != 0uL }
            .joinToString("+") { (d, c) -> when (d) {
                0 -> c.toString()
                1 -> if (c!=1uL) "$c*X"  else "X"
                else -> if (c!=1uL) "$c*X^$d" else "X^$d"
            } } +" (mod $p)"
    }

    fun clone() = FpPolynomial(p, cs.toList().toULongArray())

    override fun equals(other :Any?) :Boolean {
        if (other !is FpPolynomial)
            return false
        return p==other.p && deg==other.deg && cs contentEquals other.cs
    }

    override fun hashCode() = p.hashCode() +31* deg +997* cs.hashCode()

    operator fun plus(s :FpPolynomial) :FpPolynomial {
        assert(p==s.p)
        val cs = this.cs.zip(s.cs).map { (s1, s2) -> s1+s2 }
        if (deg>s.deg)
            return ofU(p, cs + this.cs.toList().subList(max(s.deg,0)+1, deg+1))
        if (deg<s.deg)
            return ofU(p, cs + s.cs.toList().subList(max(deg,0)+1, s.deg+1))
        return ofU(p, cs)
    }

    operator fun times(f :Fp) :FpPolynomial {
        assert(p==f.p)
        if (f.a==0uL)
            return zero(p)
        if (f.a==1uL)
            return this.clone()
        return FpPolynomial(p, cs.map { c -> (c*f.a)%p }.toULongArray())
    }

    operator fun unaryMinus() = FpPolynomial(p, cs.map { c -> (p-c)%p }.toULongArray())

    operator fun minus(s :FpPolynomial) :FpPolynomial {
        assert(p==s.p)
        val cs = this.cs.zip(s.cs).map { (s1, s2) -> (s1+p-s2)%p }
        if (deg>s.deg)
            return ofU(p, cs + this.cs.toList().subList(max(s.deg,0)+1, deg+1))
        if (deg<s.deg)
            return ofU(p, cs + s.cs.toList().subList(max(deg,0)+1, s.deg+1).map { p-it })
        return ofU(p, cs)
    }

    operator fun times(f :FpPolynomial) :FpPolynomial {
        assert(p==f.p)
        if (deg<0||f.deg<0)
            return zero(p)
        if (deg==0)
            return f*lc()
        if (f.deg==0)
            return times(f.lc())
        val result = ULongArray(deg+f.deg+1)
        if (deg<f.deg) {
            cs.forEachIndexed { d, c ->
                val s = f*Fp(p, c)
                s.cs.forEachIndexed { d1, c1 -> result[d+d1] = (result[d+d1]+c1)%p }
            }
        } else {
            f.cs.forEachIndexed { d, c ->
                val s = times(Fp(p, c))
                s.cs.forEachIndexed { d1, c1 -> result[d+d1] = (result[d+d1]+c1)%p }
            }
        }
        return FpPolynomial(p, result)
    }

    operator fun div(c :Fp) = times(c.inv())

    fun div(d :FpPolynomial) :Pair<FpPolynomial, FpPolynomial> {
        assert (p==d.p)
        var r = this
        var q = zero(p)
        if (d.deg<0)
            return Pair(q, r)
        if (d.deg==0)
            return Pair(div(d.lc()), zero(p))
        while (r.deg>=d.deg) {
            val q1 = r.lc()/d.lc();  val d1 = r.deg-d.deg
            val f = xPow(p, d1.toUInt()) * q1
            q += f;  r -= f*d
        }
        return Pair(q, r)
    }

    override fun compareTo(other :FpPolynomial) :Int = Comparator
        .comparing<FpPolynomial, Int> { it.deg }
        .thenComparing(Comparator.comparing<FpPolynomial, Fp> { it.lc() })
        .thenComparing { p :FpPolynomial, q :FpPolynomial ->
            p.cs.zip(q.cs).reversed().foldRight(0) {(px, qx), c :Int -> if (c!=0) c  else
                px.compareTo(qx) }
        }.compare(this, other)
}

fun genIrred(p :ULong, dMax :UByte, includeLower :Boolean=true) :List<FpPolynomial> {
    if (dMax.toInt()<1)
        return emptyList()
    val x = FpPolynomial.x(p)
    val constants = Fp.zero(p).range().toList()
    if (dMax.toInt()==1)
        return constants.map { c0 -> x+ FpPolynomial.of(c0) }
    val smaller = genIrred(p, (dMax/2u).toUByte())
    if (includeLower) {
        val result = smaller.toMutableList()
        var xd = FpPolynomial.xPow(p, dMax/2u +1u)
        for (d1 in (dMax/2u).toUByte() until dMax) {
            result.addAll(genAllPolys(p, d1.toUByte()).map { p1 -> xd + p1 }.filter { p1 ->
                smaller.all { d -> p1.div(d).second.deg >= 0 }
            })
            xd *= x
        }
        return result
    } else {
        val xd = FpPolynomial.xPow(p, dMax.toUInt())
        return genAllPolys(p, (dMax-1u).toUByte()).map { p1 -> xd +p1 }.filter { p1 ->
            smaller.all { d -> p1.div(d).second.deg >= 0 }
        }.toList()
    }
}

fun genAllPolys(p :ULong, d :UByte) :Sequence<FpPolynomial> {
    val constants = Fp.zero(p).range().toList()
    if (d.toInt()==0)
        return constants.asSequence().map { c -> FpPolynomial.of(c) }
    val lower = genAllPolys(p, (d-1u).toUByte())
    val xd = FpPolynomial.xPow(p, d.toUInt())
    return lower.flatMap { p1 -> constants.map { c -> xd*c +p1 } }
}

fun isIrr(p :FpPolynomial) :Boolean {
    if (p.deg<1)
        return false
    if (p.deg==1)
        return true
    val tests = genIrred(p.p, (p.deg/2).toUByte())
    return tests.all { p1 -> p.div(p1).second.deg >=0 }
}

fun factor(p :FpPolynomial) :List<FpPolynomial> {
    if (p.deg<1)
        return emptyList()
    if (p.deg==1)
        return listOf(p)
    val zero = FpPolynomial.zero(p.p)
    val test = genIrred(p.p, (p.deg/2).toUByte()).iterator()
    val result = mutableListOf<FpPolynomial>()
    var f = test.next()
    var r = p
    while (f.deg<=p.deg/2) {
        while (true) {
            val qr = r.div(f)
            if (qr.second != zero) break
            r = qr.first;  result.add(f)
        }
        if (!test.hasNext())
            break
        f = test.next()
    }
    if (r.deg>=1)
        result.add(r)
    return result
}

fun gcd(a :FpPolynomial, b :FpPolynomial) :FpPolynomial {
    assert (a.p==b.p)
    var a0 = a;  var a1 = b
    while (a1.deg>=0) {
        val a2 = a0.div(a1).second
        a0 = a1;  a1 = a2
    }
    return a0
}

fun euclid(a :FpPolynomial, b :FpPolynomial) :Triple<FpPolynomial, FpPolynomial, FpPolynomial> {
    assert (a.p==b.p)
    var a0 = a;  var fa = FpPolynomial.one(a.p); var fb = FpPolynomial.zero(a.p)
    var a1 = b;  var ga = fb;  var gb = fa
    while (a1.deg>=0) {
        val q = a0.div(a1).first
        val a2 = a0 -q*a1
        val ha = fa -q*ga;  val hb = fb -q*gb
        a0 = a1;  a1 = a2
        fa = ga;  ga = ha
        fb = gb;  gb = hb
    }
    return Triple(fa, fb, a0)
}
