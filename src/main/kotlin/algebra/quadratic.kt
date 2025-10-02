package algebra

import common.math.Rational
import numberthy.abs

fun qsolve(p :Rational, q :Rational) :Pair<Radical, Radical> {
    val d = sqr(p/2)-q
    val r = Radical.sqrt(d)
    return Pair(r-(p/2), -r-(p/2))
}

fun sqr(x :Rational) = x*x

operator fun Rational.div(d :Long) = this * Rational.of(1, d)

@ConsistentCopyVisibility
data class Radical private constructor(val m :Rational, val n :Long, val b :Rational) {
    companion object {
        val ZERO = Radical(Rational.ZERO, 1L, Rational.ZERO)
        val ONE = Radical(Rational.ZERO, 1L, Rational.ONE)
        val I = Radical(Rational.ONE, -1L, Rational.ZERO)

        fun sqrt(x :Rational) :Radical {
            if (x==Rational.ZERO)
                return ZERO
            val n = qcont(x.num);  val d = qcont(x.denom.toLong())
            val fd = x.denom/(d*d)
            return Radical(Rational.of(n.toLong(),(d*fd).toLong()), x.num/(n*n).toLong()*fd.toLong(), Rational.ZERO)
        }

        fun qcont(n :Long) :ULong {
            var result = 1uL
            var rem :ULong = abs(n)
            if (rem==0uL)
                return result
            var p = 2uL
            while (rem%(p*p)==0uL) {
                result *= p
                rem /= p*p
            }
            p++
            while (rem>=p*p) {
                while (rem%(p*p)==0uL) {
                    result *= p
                    rem /= p*p
                }
                p += 2uL
            }
            return result
        }
    }

    override fun equals(other :Any?) :Boolean {
        if (other !is Radical)
            return false
        return m==other.m && n==other.n && b==other.b
    }

    override fun hashCode() = b.hashCode() +31*m.hashCode() +997*n.hashCode()

    override fun toString() = when {
        m==Rational.ZERO -> b.toString()
        n==1L -> (m+b).toString()
        m==Rational.ONE && b==Rational.ZERO -> "sqrt($n)"
        m==Rational.ONE -> "sqrt($n)+$b"
        m==Rational.MINUS_ONE && b==Rational.ZERO -> "-sqrt($n)"
        m==Rational.MINUS_ONE -> "-sqrt($n)+$b"
        b==Rational.ZERO -> "$m*sqrt($n)"
        else -> "$m*sqrt($n)+$b"
    }

    operator fun plus(s :Rational) = Radical(m,n, b+s)
    operator fun unaryMinus() = Radical(-m, n, -b)
    operator fun minus(s :Rational) = Radical(m,n, b-s)
    fun toDouble() :Double {
        assert(n>=0)
        return m.toDouble()*kotlin.math.sqrt(abs(n).toDouble())+b.toDouble()
    }
}
