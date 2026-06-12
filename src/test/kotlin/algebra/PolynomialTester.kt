package algebra

import kotlin.test.*

class PolynomialTester {
    @Test fun zero() {
        val zero = Polynomial.ZERO
        println("0 = $zero")
        assertEquals(Polynomial.of(emptyList()), zero)
    }

    @Test fun one() {
        val one = Polynomial.const(1.0)
        println("1 = $one")
        assertEquals(Polynomial.of(listOf(1.0)), one)
    }

    @Test fun x() {
        val x = Polynomial.X
        println("x = $x")
        assertEquals(Polynomial.of(listOf(0.0, 1.0)), x)
    }

    @Test fun addition() {
        val p = Polynomial.of(listOf(1.0, 2.0, 3.0))
        val q = Polynomial.of(listOf(4.0, 5.0, 6.0))
        val result = p + q
        println("$p + $q = $result")
        assertEquals(Polynomial.of(listOf(5.0, 7.0, 9.0)), result)
    }

    @Test fun uminus() {
        val p = Polynomial.of(listOf(1.0, 2.0, 3.0))
        val result = -p
        println("-($p) = $result")
        assertEquals(Polynomial.of(listOf(-1.0, -2.0, -3.0)), result)
    }

    @Test fun minus() {
        val p = Polynomial.of(listOf(1.0, 2.0, 3.0))
        val result = p - p
        println("$p - ($p) = $result")
        assertEquals(Polynomial.ZERO, result)
    }

    @Test fun multiplication() {
        val x = Polynomial.X
        val p = x*x +2.0*x + 3.0
        println("$x^2 + 2*$x + 3 = $p")
        assertEquals(Polynomial.of(listOf(3.0, 2.0, 1.0)), p)
        val q = x +2.0
        val result = p*q
        println("($p) * ($q) = $result")
        assertEquals(Polynomial.of(listOf(6.0, 7.0, 4.0, 1.0)), result)
    }
}
