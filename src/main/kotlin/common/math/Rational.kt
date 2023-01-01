package common.math

import kotlin.math.abs

class DivisionByZeroException :Throwable("Division by Zero") {}

data class Rational(val num :Long, val denom :ULong) :Comparable<Rational> {
    companion object {
        fun of(num :Long, denom :Long =1L) :Rational {
            if (denom==0L)
                throw DivisionByZeroException()
            val d = gcd(abs(num).toULong(), abs(denom).toULong())
            if (denom<0)
                return Rational(-num/d.toLong(), abs(denom).toULong()/d)
            else
                return Rational(num/d.toLong(), denom.toULong()/d)
        }

        val ZERO = Rational(0L, 1uL)
        val ONE = Rational(1L, 1uL)
        val TWO = Rational(2L, 1uL)
        val MINUS_ONE = Rational(-1L, 1uL)
        val HALF = Rational(1L, 2uL)
        val TEN = Rational(10L, 1uL)
    }

    override fun toString() :String {
        if (denom==1uL)
            return num.toString()
        return "$num/$denom"
    }

    operator fun plus(s :Rational) :Rational {
        val d = gcd(denom, s.denom)
        val f = s.denom/d;  val f2 = denom/d
        return of(num*f.toLong() +s.num*f2.toLong(), (denom*f).toLong())
    }

    fun minus() = Rational(-num, denom)

    operator fun minus(s :Rational) = plus(s.minus())

    override fun compareTo(other :Rational) = minus(other).num.compareTo(0)

    operator fun times(f :Rational) :Rational {
        val d1 = gcd(abs(num).toULong(), f.denom)
        val d2 = gcd(denom, abs(f.num).toULong())
        return Rational(num/d1.toLong()*(f.num/d2.toLong()), denom/d2*(f.denom/d1))
    }

    fun inv() = of(denom.toLong(), num)

    operator fun div(d :Rational) = times(d.inv())

    fun toDouble() :Double = num.toDouble()/denom.toDouble()
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
