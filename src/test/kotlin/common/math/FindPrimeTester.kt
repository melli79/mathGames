package common.math

import kotlin.test.*

class FindPrimeTester {
    @Test fun findSmallPrimes() {
        val result = findPrimes(2u)
        assertEquals(listOf(2u), result)
    }

    @Test fun findPrimesUpto10() {
        val result = findPrimes(10u)
        assertEquals(listOf(2u,3u,5u,7u), result)
    }

    @Test fun findPrimesUpto20() {
        val result = findPrimes(20u)
        assertEquals(listOf(2u,3u,5u,7u, 11u,13u,17u,19u), result)
    }

    @Test fun findPrimesUpto2_19() {
        val result = findPrimes(1u.shl(19))
        println("First primes are ${result.take(100)}")
    }
}
