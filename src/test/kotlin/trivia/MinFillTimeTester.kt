package trivia

import java.lang.System.currentTimeMillis
import kotlin.random.Random
import kotlin.system.measureNanoTime
import kotlin.test.*

class MinFillTimeTester {
    @Test fun emptyCascade() {
        assertEquals(0u, computeMinFillTime(emptyList()))
    }

    @Test fun singleBasin() {
        assertEquals(1u, computeMinFillTime(listOf(1u)))
    }

    @Test fun lowerFollowing() {
        assertEquals(2u, computeMinFillTime(listOf(2u, 1u)))
    }

    @Test fun jointFilling() {
        assertEquals(2u, computeMinFillTime(listOf(1u, 3u)))
    }

    @Test fun multipleJointFilling() {
        assertEquals(3u, computeMinFillTime(listOf(1u, 2u, 4u)))
    }

    val random = Random(currentTimeMillis())

    @Test fun performance() {
        assertTrue((1..10).map {
            val volumes = (1..200_000).map { random.nextInt().toUInt() }
            val duration = measureNanoTime {
                assertTrue(computeMinFillTime(volumes) <= volumes.max())
            }
            duration <= 18_000_000
        }.count { !it } <= 1)
    }
}
