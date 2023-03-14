package numberthy

import java.math.BigInteger
import kotlin.test.*

class DigitSumTester {
    @Test fun checkDigitSum() {
        val prod = (2..100).fold(BigInteger.ONE) { p :BigInteger, k -> p.times(k.toBigInteger()) }
        val result = computeDigitSum(prod)
        println("ds(100!) = $result")
        assertEquals(0u, result%9u)
    }
}

fun computeDigitSum(number :BigInteger) :UInt {
    assert(number>=BigInteger.ZERO)
    var result = 0u
    var remainder = number
    while (remainder> BigInteger.ZERO) {
        result += remainder.mod(BigInteger.TEN).toInt().toUInt()
        remainder /= BigInteger.TEN
    }
    return result
}
