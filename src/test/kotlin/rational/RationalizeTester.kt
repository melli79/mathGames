package rational

import common.math.Rational
import common.math.Rational.Companion.ONE
import common.math.Rational.Companion.TWO
import common.math.Rational.Companion.HALF
import common.math.Rational.Companion.ZERO
import rational.QPolynomial.Companion.X
import rational.QPolynomial.Companion.const
import rational.QPolynomial.Companion.monom
import kotlin.test.*

class RationalizeTester {
    @Test fun alreadyRational() {
        val denom = const(HALF)
        val result = rationalize(denom, n=3L)
        assertEquals(const(TWO), result)
    }

    @Test fun gcdFactors() {
        val p = QPolynomial(listOf(ONE, ZERO, ONE))
        val q = X
        val (fp, fq, d) = gcf(p, q)
        println("($fp)*($p) +($fq)*($q) = $d")
        assertEquals(d, fp*p+fq*q)
    }

    @Test fun gcdFactors1() {
        val p = X
        val q = QPolynomial(listOf(ONE, ONE, ONE))
        val (fp, fq, d) = gcf(p, q)
        println("($fp)*($p) +($fq)*($q) = $d")
        assertEquals(d, fp*p+fq*q)
    }

    @Test fun gcdFactors2() {
        val p = QPolynomial(listOf(ONE, ONE, TWO))
        val q = QPolynomial(listOf(-TWO, ZERO, ZERO, ONE))
        val (fp, fq, d) = gcf(p, q)
        println("($fp)*($p) +($fq)*($q) = $d")
        assertEquals(d, fp*p+fq*q)
    }

    @Test fun quadratic() {
        val denom = X
        val result = rationalize(denom, n=2)
        assertEquals(X*HALF, result)
    }

    @Test fun cubic() {
        val denom = X
        val result = rationalize(denom, n=5, d=3u)
        assertEquals(monom(2u) *Rational.of(1, 5), result)
    }

    @Test fun cubic2() {
        val denom = X*X
        val result = rationalize(denom, n=5, d=3u)
        assertEquals(X*Rational.of(1,5), result)
    }

    @Test fun demo3() {
        val denom = QPolynomial(listOf(ONE, ONE, TWO))
        val result = rationalize(denom, n=5, d=3u)
        println("1 /( ${denom.toString("\\cbrt(5)")} ) = ${result.toString("\\cbrt(5)")}")
        assertEquals(QPolynomial.ONE, (denom*result).div(monom(3u)- const(Rational.of(5))).second)
    }
}
