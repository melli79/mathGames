package algebra

import common.math.Rational
import kotlin.math.sqrt
import kotlin.test.*

class QuadraticTester {
    @Test fun imag() {
        val result = qsolve(Rational.ZERO, Rational.ONE)
        println(result)
        assertEquals(Radical.I, result.first)
        assertEquals(-Radical.I, result.second)
    }

    @Test fun phi() {
        val result = qsolve(Rational.MINUS_ONE, Rational.MINUS_ONE).first
        println("{\\phi, 1/\\phi} = ±${result.m}*sqrt(${result.n})+${result.b}")
        println("\\phi ≈ ${result.toDouble()}")
        assertEquals((sqrt(5.0)+1)/2, result.toDouble())
    }

    @Test fun w2() {
        val result = qsolve(Rational.ZERO, -Rational.TWO).first
        println("x^2-2: ±$result")
        assertEquals(sqrt(2.0), result.toDouble())
    }

    @Test fun x1() {
        val result = qsolve(Rational.ONE, Rational.ONE).first
        println("x^2+x+1: ${result.b}±${result.m}*sqrt(${result.n})")
    }
}
