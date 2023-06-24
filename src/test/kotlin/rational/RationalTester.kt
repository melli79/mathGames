package rational

import common.math.DivisionByZeroException
import org.junit.jupiter.api.assertThrows
import kotlin.math.pow
import kotlin.test.*

import common.math.Rational

class RationalTester {

    private val half = Rational.HALF
    private val third = Rational.of(1,3)

    @Test fun equality() {
        val otherHalf = Rational.of(2, 4)
        assertEquals(half, otherHalf)
    }

    @Test fun ordering() {
        assertTrue { third < half }
        assertTrue { Rational.TWO > Rational.ONE }
    }

    @Test fun addition() {
        assertEquals(half, half+ Rational.ZERO)
        assertEquals(Rational.of(5,6), half+third)
        assertEquals(Rational.of(1,6), half-third)
    }

    @Test fun multiplication() {
        assertEquals(Rational.of(1,6), half*third)
    }

    @Test fun division() {
        assertEquals(half, Rational.TWO.inv())
        assertEquals(Rational.of(3,2), half/third)
    }

    @Test fun zeroCheck() {
        assertThrows<DivisionByZeroException> { Rational.of(1,0) }
        assertThrows<DivisionByZeroException> { Rational.ZERO.inv() }
    }

    @Test fun doublePrecision() {
        val result = third.toDouble().toString()
        assertTrue(result.startsWith("0.3333333333333"))
    }

    @Test fun continuedFractions() {
        val pi :Double = Math.PI
        for (e in 1..(6*8)) {
            val error = 0.5.pow(e)
            val approx = Rational.approx(pi, error)
            println("$pi ≈ $approx ±%.1e".format(error))
            assertEquals(pi, approx.toDouble(), 1.5*error)
        }
    }
}
