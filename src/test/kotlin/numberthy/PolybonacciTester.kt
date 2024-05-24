package numberthy

import kotlin.test.*

class PolybonacciTester {
    @Test fun fibonacci() {
        val result = (0..10).map { polyb(it.toUByte(), 2u) }
        println("The first Fibonacci numbers are $result")
        val expected = (0..10).map { fib(it.toUByte()) }
        assertEquals(expected, result)
    }

    @Test fun tribonacci() {
        val result = (0..10).map { polyb(it.toUByte(), 3u) }
        println("The first tribonacci numbers are $result")
        val expected = (0..10).map { trib(it.toUByte()) }
        assertEquals(expected, result)
    }

    @Test fun quabonacci() {
        val result = (0..10).map { polyb(it.toUByte(), 4u) }
        println("The first quabonacci numbers are $result")
        val expected = listOf<ULong>(0u, 1u,1u,2u, 4u,8u,15u,29u, 56u,108u,208u)
        assertEquals(expected, result)
    }

    @Test fun exponential() {
        val result = (0..10).map { polyb(it.toUByte(), 10u) }
        println("The first exponential elements are $result")
        val expected = mutableListOf<ULong>(0u, 1u)
        repeat(9) { expected.add(expected.sum()) }
        assertEquals(expected, result)
    }
}
