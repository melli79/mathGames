package search

import kotlin.test.*

@OptIn(ExperimentalUnsignedTypes::class)
class BribedSearchTester {
    @Test fun empty() {
        val input = uintArrayOf()
        val result = solve(input, 0u)
        assertEquals(0u, result.first)
        assertEquals(emptyList(), result.second)
    }

    @Test fun single() {
        val bribes = uintArrayOf(1u)
        val result = solve(bribes, 0u)
        assertEquals(1u, result.first)
        assertEquals(listOf(0u), result.second)
    }

    @Test fun steeplyRisingEnd() {
        val bribes = uintArrayOf(0u, 1u, 2u, 4u)
        val result = solve(bribes, 3u)
        println("Getting to ${result.second.last()} via ${result.second} by paying ${result.first} in total.")
        assertEquals(3u, result.second.last())
        assertTrue(result.first<7u)
    }

    @Test fun steeplyRisingMiddle() {
        val bribes = uintArrayOf(0u, 1u, 2u, 4u)
        val result = solve(bribes, 2u)
        println("Getting to ${result.second.last()} via ${result.second} by paying ${result.first} in total.")
        assertEquals(2u, result.second.last())
        assertTrue(result.first<7u)
    }

    @Test fun totalPriceSteeplyRising() {
        val bribes = uintArrayOf(0u, 1u, 2u, 4u, 8u)
        var totalPrice = 0u;  var totalNumBribes = 0
        for (solution in 0u until bribes.size.toUInt()) {
            val result = solve(bribes, solution)
            assertEquals(solution, result.second.last())
            if (solution==0u)
                println("Typical query path: ${result.second}")
            totalPrice += result.first
            totalNumBribes += result.second.size
        }
        println("Getting to all targets costs $totalPrice in total and needs $totalNumBribes bribes in total.")
    }
}
