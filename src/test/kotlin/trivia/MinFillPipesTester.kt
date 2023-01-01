package trivia

import java.lang.System.currentTimeMillis
import kotlin.random.Random
import kotlin.system.measureNanoTime
import kotlin.test.*

class MinFillPipesTester {
    @Test fun emptyCascade() {
        assertEquals(0, computeMinFillPipes(emptyList(), 0u))
    }

    @Test fun impossible() {
        val volumes = listOf(4u)
        val fillTime = 3u
        assertTrue(computeMinFillTime(volumes) > fillTime)
        assertEquals(-1, computeMinFillPipes(volumes, fillTime))
    }

    @Test fun singleBasin() {
        assertEquals(1, computeMinFillPipes(listOf(3u), 4u))
    }

    @Test fun twoPipes() {
        assertEquals(2, computeMinFillPipes(listOf(3u, 2u), 4u))
    }

    @Test fun oneJointPipe() {
        assertEquals(1, computeMinFillPipes(listOf(3u, 2u), 5u))
    }

    val random = Random(currentTimeMillis())

    @Test fun performance() {
        assertTrue((1..10).map {
            val volumes = (1..200_000).map { random.nextInt().toUInt() }
            val time = volumes.max()
            val duration = measureNanoTime {
                assertTrue(computeMinFillPipes(volumes, time) < volumes.size)
            }
            duration <= 6_000_000L
        }.count { !it } <= 1)
    }
}
