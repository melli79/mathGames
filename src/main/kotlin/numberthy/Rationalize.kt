package numberthy

import algebra.QPolynomial
import common.math.*

/**
 * rationalize the denominator of 1/denom(\sqrt[d]{n})
 */
fun rationalize(denom : QPolynomial, n :Long, d :UByte =2u) : QPolynomial {
    if (denom== QPolynomial.ZERO)
        throw DivisionByZeroException()
    assert(denom.deg<d.toInt())
    assert(isPowerFree(abs(n), d))
    val p = QPolynomial.monom(d) - QPolynomial.const(Rational.of(n))
    val (num, _, f) = gcf(denom, p)
    assert(f.deg==0)
    return num * f.lc.inv()
}

fun gcf(a : QPolynomial, b : QPolynomial) :Triple<QPolynomial, QPolynomial, QPolynomial> {
    var a0 = a
    var f0 = QPolynomial.ONE; var f1 = QPolynomial.ZERO
    var a1 = b
    var g0 = QPolynomial.ZERO; var g1 = QPolynomial.ONE
    while (a1.lc!= Rational.ZERO) {
        val (d, a2) = a0.div(a1)
        val f2 = f0 -d*f1
        val g2 = g0 -d*g1
        assert(a2 == a*f2+b*g2)
        a0 = a1; f0 = f1; g0 = g1
        a1 = a2; f1 = f2; g1 = g2
    }
    return Triple(f0, g0, a0)
}

fun isPowerFree(n :ULong, d :UByte) :Boolean {
    for (p in findPrimes(isqrt(n))) {
        val pd = p.toULong().ipow(d)
        if (gcd(pd, n)==pd)
            return false
    }
    return true
}

fun ULong.ipow(e :UByte) :ULong {
    var result = 1uL
    var f = this
    var e = e.toInt()
    while (e>0) {
        if (e%2>0)
            result *= f
        f *= f
        e /= 2
    }
    return result
}
