package diophantine

import kotlin.test.*

class HopperTester {
    @Test fun zeroHopper() {
        val result = findHopping(setOf(), setOf())
        assertEquals(setOf(emptyList()), result.first)
        assertEquals(1uL, result.second)
    }

    @Test fun oneHop() {
        val result = findHopping(setOf(1u), setOf())
        assertTrue(result.first.isNotEmpty())
        val solution = result.first.first()
        assertEquals(listOf(1u), solution)
        assertEquals(1, result.first.size)
        assertEquals(1uL, result.second)
    }

    @Test fun twoHops() {
        val input = setOf(1u, 2u)
        val avoids = setOf(1u)
        val result = findHopping(input, avoids)
        println("solutions: "+result.first.joinToString())
        assertTrue(result.first.isNotEmpty())
        result.first.forEach { solution ->
            assertEquals(input, solution.toSet())
            assertEquals(input.size, solution.size)
            val intermediates = solution.cumSum().dropLast(1)
            intermediates.forEach { s ->
                assertFalse(s in avoids)
            }
        }
        assertTrue(result.second >= result.first.size.toULong())
    }

    @Test fun smallNumbers() {
        for (n in 3u..10u) {
            val jumps = (1u..n).toSet()
            val avoid0 = (1u until n)
            val sum = avoid0.sum()
            val avoids = (1u..n).flatMap { f ->
                (0u until sum)
                    .filter { offset -> f*n+offset<sum }
                    .map { offset ->
                        avoid0.map { it*f }.toSet()
            }} + listOf((1u until n).map { 1u+ it*(it-1u)/2u }.toSet())
            // println(avoids.joinToString(";  "))
            avoids.forEach { avoid1 ->
                assertEquals(n.toInt()-1, avoid1.size)
            }
            var min = (2u ..n).fold(1uL){ p :ULong, f -> p*f.toULong() }
            for (avoid1 in avoids) {
                val result = findHopping(jumps, avoid1)
                if (result.second<min) {
                    min = result.second
                    println("solution for $n, $avoid1: ${result.second} -- " + result.first.joinToString())
                }
                result.first.forEach { solution ->
                    assertEquals(jumps, solution.toSet())
                    assertEquals(jumps.size, solution.size)
                    val intermediates = solution.cumSum().dropLast(1)
                    intermediates.forEach { s ->
                        assertFalse(s in avoid1)
                    }
                }
            }
        }
    }
}

private fun List<UInt>.cumSum() :List<UInt> {
    var sum = 0u
    return map { v -> sum += v; sum }
}
