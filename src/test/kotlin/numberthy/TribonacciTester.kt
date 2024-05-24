package numberthy

import kotlin.test.*

class TribonacciTester {
    @Test fun start() {
        assertEquals(0uL, trib(0u))
        assertEquals(1uL, trib(1u))
        assertEquals(1uL, trib(2u))
    }

    @Test fun small() {
        val result = (0..10).map { trib(it.toUByte()) }
        println("The first tribonacci numbers are $result")
        assertEquals(listOf(0uL, 1u,1u, 2u,4u,7u,13u,24u,44u, 81u,149u), result)
    }
}
