package partitions

import org.junit.jupiter.api.assertThrows
import kotlin.test.*

class SquarePartitionTester {
    @Test fun zero() {
        assertEquals(emptyList(), findShortestSquarePartition(0u))
    }

    @Test fun one() {
        assertThrows<IllegalArgumentException> { findShortestSquarePartition(1u) }
    }

    @Test fun ten() {
        val result = findShortestSquarePartition(10u)
        println("10^2 = sum{x^2} $result")
        assertTrue(result.size<=5)
        assertEquals(sqr(10u), result.sumOf { sqr(it) })
    }

    @Test fun firstOnes() {
        for (n in 2u..20u) {
            val squarePartition = findShortestSquarePartition(n)
            println("$n^2 = sum{x^2} $squarePartition")
            assertEquals(sqr(n), squarePartition.sumOf { sqr(it) })
        }
    }
}
