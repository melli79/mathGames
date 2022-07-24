package diophantine

import kotlin.test.*

class SumOfCubesTester {
    @Test fun upTo0() {
        assertEquals(mapOf(Pair(0u, setOf(Triple(0,0,0)))), findSumOfCubes(0u))
    }

    @Test fun upTo1() {
        val result = findSumOfCubes(1u)
        assertEquals(listOf(Triple(0,0,0), Triple(1, -1, 0), Triple(2, -2, 0)),
            result[0u]?.take(3))
        assertEquals(listOf(Triple(1, -1, 1), Triple(1,0,0), Triple(1, 1, -1)),
            result[1u]?.take(3))
    }

    @Test fun upTo2() {
        val result = findSumOfCubes(2u)
        assertEquals(setOf(Triple(1,1,0), Triple(7, -6, -5)), result[2u])
    }

    @Test fun upto42() {
        val result = findSumOfCubes(30u)
        for (s in 0u..42u) if (s%9u!=4u && s%9u!=5u) {
            if (s !in result.keys)
                println("Missing $s,")
            else
                println("$s: ${result[s]?.take(3)}")
        }
    }
}
