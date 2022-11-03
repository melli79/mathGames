package reduction

import common.math.factor
import kotlin.test.*

class ReductionTester {
    @Test fun combining() {
        val n = 24uL
        val pFactors = factor(n)
        val result = ReductionGame.combine(pFactors.entries.toList().reversedIterator(), 12u)
        println("Smallest factors of $n are $result, ...")
        result.forEach { f ->
            assertEquals(0uL, n%f)
        }
        assertTrue(result.size > 3)
    }

    @Test fun factoring() {
        val n = 24uL
        val smallFactors = ReductionGame.factors(n)
        println("The small factors of $n are $smallFactors.")
        assertEquals(listOf(1u, 2u, 3u, 4u), smallFactors)
    }

    @Test fun smallValues() {
        val result = (1..40).map { n -> ReductionGame.predictMinimalValue(n.toULong()) }
        println("The first values are ${result.mapIndexed{ i, v -> "${i+1}: $v${ReductionGame.predictBestMove((i+1).toULong())}" }.joinToString()}")
    }

    @Test fun bigValue() {
        val n = 12345687uL
        val result = Pair(ReductionGame.predictMinimalValue(n), ReductionGame.predictBestMove(n))
        println("$n: ${result.first}${result.second}")
        assertTrue(result.first<3, "This is highly unlikely")
    }
}
