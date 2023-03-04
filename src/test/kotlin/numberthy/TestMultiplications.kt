package numberthy

import numberthy.Multiplication.optimize
import numberthy.Multiplication.getSequence
import numberthy.Multiplication.quickPower
import kotlin.test.*

class TestMultiplications {
    @Test fun firstOptimizations() {
        assertEquals(Pair(0u, 0u), optimize(0u))
        assertEquals(Pair(1u, 0u), optimize(1u))
    }

    @Test fun twoOptimized() {
        val result = getSequence(2u)
        println("Sequence to 2: $result")
        assertEquals(Pair(1u, 1u), optimize(2u))
        assertEquals(setOf(2u), result)
    }

    @Test fun threeOptimized() {
        val result = getSequence(3u)
        println("Sequence to 3: $result")
        assertEquals(Pair(2u, 2u), optimize(3u))
        assertEquals(setOf(2u,3u), result)
    }

    @Test fun fourOptimized() {
        val result = getSequence(4u)
        println("Sequence upto 4: $result")
        assertEquals(Pair(2u, 2u), optimize(4u))
        assertEquals(setOf(2u,4u), result)
    }

    @Test fun fiveOptimized() {
        val result = getSequence(5u)
        println("Sequence to 5: $result")
        assertEquals(3u, optimize(5u).second)
        assertTrue(result.containsAll(setOf(5u,2u)))
    }

    @Test fun smallOptimized() {
        for (n in 2u..32u) {
            val result = getSequence(n)
            print("sequence up to $n: ${result.size} steps $result")
            assertEquals(optimize(n).second, result.size.toUInt())
            assertTrue(result.containsAll(setOf(2u, n)))
            val limit = quickPower(n).size
            if (result.size<limit)
                println(" -- better than quickPower")
            else
                println()
            assertTrue(result.size <= limit)
        }
    }
}
