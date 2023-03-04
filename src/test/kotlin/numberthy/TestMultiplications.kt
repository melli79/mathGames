package numberthy

import numberthy.Multiplication.optimize
import numberthy.Multiplication.getSequences
import numberthy.Multiplication.quickPower
import kotlin.test.*

class TestMultiplications {
    @Test fun firstOptimizations() {
        assertEquals(Pair(setOf(0u), 0u), optimize(0u))
        assertEquals(Pair(setOf(1u), 0u), optimize(1u))
    }

    @Test fun twoOptimized() {
        val result = getSequences(2u)
        println("Sequence to 2: ${result.first().size} steps  "+ result.joinToString(" or "))
        assertEquals(Pair(setOf(1u), 1u), optimize(2u))
        assertEquals(setOf(setOf(2u)), result)
    }

    @Test fun threeOptimized() {
        val result = getSequences(3u)
        println("Sequence to 3: ${result.first().size} steps  "+ result.joinToString(" or "))
        assertEquals(Pair(setOf(2u), 2u), optimize(3u))
        assertTrue(setOf(3u,2u) in result)
    }

    @Test fun fourOptimized() {
        val result = getSequences(4u)
        println("Sequence to 4 has ${result.first().size} steps:  "+ result.joinToString(" or "))
        assertEquals(Pair(setOf(2u), 2u), optimize(4u))
        assertTrue(setOf(2u,4u) in result)
    }

    @Test fun fiveOptimized() {
        val result = getSequences(5u)
        println("Sequence to 5 has ${result.first().size} steps:  "+ result.joinToString(" or "))
        val optima = optimize(5u)
        assertEquals(3u, optima.second)
        assertTrue(result.all { it.containsAll(setOf(5u,2u))})
        assertTrue(setOf(5u,4u,2u) in result)
        assertEquals(setOf(3u,4u), optima.first)
        assertTrue(setOf(5u,3u,2u) in result)
    }

    @Test fun smallOptimized() {
        for (n in 2u..32u) {
            val result = getSequences(n)
            val actual = result.first().size
            print("sequence up to $n has $actual steps")
            val limit = quickPower(n).size
            if (actual<limit)
                print(" -- better than quickPower:  ")
            else
                print(":  ")
            println(result.take(3).joinToString(" or ") +if (result.size>3) " or ..." else "")
            assertEquals(optimize(n).second, actual.toUInt())
            assertTrue(result.all { it.containsAll(setOf(2u, n))})
            assertTrue(actual <= limit)
        }
    }
}
