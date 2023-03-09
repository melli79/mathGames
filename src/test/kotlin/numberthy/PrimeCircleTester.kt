package numberthy

import kotlin.test.*

@OptIn(ExperimentalUnsignedTypes::class)
class PrimeCircleTester {
    @Test fun zero() {
        val result = findAll(0u)
        assertEquals(1, result.size)
        assertEquals(emptyList(), result.first().toList())
    }

    @Test fun one() {
        val result = findAll(1u)
        println(result)
        assertTrue(result.isNotEmpty())
        assertEquals(listOf(1u,2u), result.first().toList())
        assertEquals(1, result.size)
    }

    @Test fun two() {
        val result = findAll(2u)
        println(result)
        assertEquals(2, result.size)
    }

    @Test fun three() {
        val result = findAll(3u)
        println(result)
        assertEquals(2, result.size)
    }

    @Test fun smallNumbers() {
        for (n in 0u..8u) {
            val result = findAll(n)
            println("$n: ${result.size}" +if (n<5u) " -- "+result.joinToString { it.toList().toString() } else "")
            if (n>=2u)
                assertEquals(0, result.size%2)
        }
    }
}
