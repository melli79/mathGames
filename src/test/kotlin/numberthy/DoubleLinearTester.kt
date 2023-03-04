package numberthy

import kotlin.test.*

class DoubleLinearTester {
    @Test fun generatesOrdered() {
        val result = generateDoubleLinear(200L)
        println(result)
        for (i in 0 until result.size-1) {
            assertTrue(result[i]<result[i+1], "Elements at $i and ${i+1} out of order.")
        }
    }

    @Test fun isConsistent() {
        val result = generateDoubleLinear(30L)
        val more = generateDoubleLinear(61L)
        assertEquals(more.slice(result.indices), result)
    }

    @Test fun generatesToIndex() {
        val result = getElement23(10)
        assertEquals(22L, result)
    }

    @Test fun numbers() {
        val sequence = generateDoubleLinear(100_000L)
        for (n in 0..50) {
            val num = 10*n
            println("u[$num] = ${sequence[num]}")
        }
    }

    @Test fun generatesToIndexIsSufficient() {
        for (index in listOf(0,1,2,3,4,5, 10,20,30,50, 100,200,300,500, 1000)) {
            val result = getElement23(index)
            println("u[$index] = $result")
        }
    }
}
