package common.math

import kotlin.math.abs

class DivisionByZeroException :Throwable("Division by Zero") {}

data class Rational private constructor(val num :Long, val denom :ULong) :Comparable<Rational> {
    companion object {
        fun of(num :Long, denom :Long =1L) :Rational {
            if (denom==0L)
                throw DivisionByZeroException()
            val d = gcd(abs(num).toULong(), abs(denom).toULong())
            return if (denom<0)
                Rational(-num/d.toLong(), abs(denom).toULong()/d)
            else
                Rational(num/d.toLong(), denom.toULong()/d)
        }

        val ZERO = Rational(0L, 1uL)
        val ONE = Rational(1L, 1uL)
        val TWO = Rational(2L, 1uL)
        val MINUS_ONE = Rational(-1L, 1uL)
        val HALF = Rational(1L, 2uL)
        val TEN = Rational(10L, 1uL)

        // chain fraction approximation
        fun approx(v :Double, e :Double? = null) :Rational {
            assert(e==null || e>0.0)
            var num = v.toLong()
            var num1 = 1L
            var denom = 1uL
            var denom1 = 0uL
            var f = v - num.toDouble()
            var e = e ?: ((abs(v)+1.0)*1e-16)
            while (abs(f)>=e) {
                val inv = 1/f
                val an = inv.toLong()
                // https://en.wikipedia.org/wiki/Continued_fraction#Infinite_continued_fractions_and_convergents
                val num2 = num;  val denom2 = denom
                num = num*an +num1
                denom = an.toULong()*denom +denom1
                num1 = num2;  denom1 = denom2
                f = inv - an.toDouble()
                e *= sqr(inv)
            }
            return Rational(num, denom)
        }
    }

    override fun toString() :String {
        if (denom==1uL)
            return num.toString()
        return "$num/$denom"
    }

    fun toDouble() = num.toDouble()/denom.toLong()

    operator fun plus(s :Rational) :Rational {
        val d = gcd(denom, s.denom)
        val f = s.denom/d;  val f2 = denom/d
        return of(num*f.toLong() +s.num*f2.toLong(), (denom*f).toLong())
    }

    operator fun unaryMinus() = Rational(-num, denom)

    operator fun minus(s :Rational) = plus(-s)

    override fun compareTo(other :Rational) = minus(other).num.compareTo(0)

    operator fun times(f :Rational) :Rational {
        val d1 = gcd(abs(num).toULong(), f.denom)
        val d2 = gcd(denom, abs(f.num).toULong())
        return Rational(num/d1.toLong()*(f.num/d2.toLong()), denom/d2*(f.denom/d1))
    }

    fun inv() :Rational {
        if (num==0L)
            throw DivisionByZeroException()
        return Rational(denom.toLong()*sgn(num), abs(num).toULong())
    }

    operator fun div(d :Rational) = times(d.inv())
}

fun sgn(x :Long) = when {
    x>0L -> 1L
    x<0L -> -1L
    else -> 0L
}

fun gcd(a :ULong, b :ULong) :ULong {
    var a0=a;  var a1=b
    while (a1>0u) {
        val a2 = a0%a1
        a0 = a1
        a1 = a2
    }
    return a0
}

fun lcm(a :ULong, b :ULong) = a * (b/ gcd(a,b))
