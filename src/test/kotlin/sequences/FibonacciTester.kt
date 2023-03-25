package sequences

import kotlin.test.*

class FibonacciTester {
    @Test fun start0() {
        assertEquals(0uL, fib(0u))
        assertEquals(1uL, fib(1u))
    }

    @Test fun small() {
        val result = (0..10).map { fib(it.toUByte()) }
        println("The first Fibonacci numbers are $result")
        assertEquals(listOf(0uL, 1uL, 1u, 2u, 3u, 5u, 8u, 13u, 21u, 34u, 55u), result)
    }
}
