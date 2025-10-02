package algebra

import kotlin.test.*

class EisensteinTester {
    @Test fun strings() {
        println(" 0 = ${Eisenstein.ZERO}")
        println(" 1 = ${Eisenstein.ONE}")
        println(" w = ${Eisenstein.w}")
        println(" w^2 = ${Eisenstein.w2}")
        println(" lambda = ${Eisenstein.lambda}")
        println(" units = ${Eisenstein.units}")
    }

    @Test fun ring() {
        assertEquals(Eisenstein.ZERO, Eisenstein.ZERO + Eisenstein.ZERO)
        assertEquals(Eisenstein.ONE, Eisenstein.ONE + Eisenstein.ZERO)
        assertEquals(Eisenstein.w, Eisenstein.ZERO + Eisenstein.w)
        assertEquals(Eisenstein.ZERO, Eisenstein.w * Eisenstein.ZERO)
        assertEquals(Eisenstein.w2, Eisenstein.w * Eisenstein.w)
        assertEquals(Eisenstein.ONE, Eisenstein.w2 * Eisenstein.w)
        for (u in Eisenstein.units) {
            assertEquals(1, u.norm())
        }
    }

    @Test fun primes() {
        assertFalse(Eisenstein.ZERO.isPrime()!!)
        for (u in Eisenstein.units) {
            assertFalse(u.isPrime()!!)
        }
        assertTrue(Eisenstein.lambda.isPrime()!!)
        val primes = mutableListOf<Eisenstein>()
        for (a in -5..5)  for (b in 0..5) {
            val p = Eisenstein(a, b)
            if (p.isPrime()==true)
                primes.add(p)
        }
        println("\n in total ${primes.size} primes: "+ primes.sortedBy { it.norm() }.joinToString { "${it}, ${-it}" })
    }
}
