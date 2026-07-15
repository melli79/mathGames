package geometry

import common.math.geometry.Point2D
import kotlin.math.sqrt
import kotlin.random.Random
import kotlin.test.*

class NoCrossingLinesTester {
    @Test fun empty() {
        val result = noCrossingLines(emptyList(), emptyList())
        println("matching empty lists: ${result}")
        assertEquals(emptySet(), result)
    }

    @Test fun singletons() {
        val blues = listOf(Point2D.ORIGIN)
        val reds = listOf(Point2D.ORIGIN)
        val result = noCrossingLines(blues, reds)
        println("matching singletons: ${result}")
        assertEquals(setOf(Pair(blues[0], reds[0])), result)
    }

    @Test fun halfs() {
        val blues = listOf(Point2D.ORIGIN)
        val reds = emptyList<Point2D>()
        val result = noCrossingLines(blues, reds)
        println("matching halfs: ${result}")
        assertEquals(emptySet(), result)
    }

    @Test fun otherHalfs() {
        val blues = emptyList<Point2D>()
        val reds = listOf(Point2D.ORIGIN)
        val result = noCrossingLines(blues, reds)
        println("matching halfs: ${result}")
        assertEquals(emptySet(), result)
    }

    @Test fun twoPairs() {
        val blues = listOf(Point2D.ORIGIN, Point2D(1.0, 0.0))
        val reds = listOf(Point2D.ORIGIN, Point2D(1.0, 1.0))
        val result = noCrossingLines(blues, reds)
        println("matching pairs: ${result}")
        assertEquals(setOf(Pair(blues[0], reds[0]), Pair(blues[1], reds[1])), result)
    }

    val random = Random(System.currentTimeMillis())

    @Test fun fivePairs() {
        val blues = listOf(Point2D.ORIGIN, Point2D(1.0, 0.0), Point2D(0.0, 1.0), Point2D(1.0, 1.0), Point2D(0.5, 0.5))
        val reds = (1..5).map { random.nextPoint() }
        val result = noCrossingLines(blues, reds)
        println("5 pairs: ${result}")
        assertEquals(5, result.size)
        val firsts = result.map { it.first }.toSet()
        blues.forEach { p ->
            assertTrue(p in firsts)
        }
        val seconds = result.map { it.second }.toSet()
        reds.forEach { p ->
            assertTrue(p in seconds)
        }
        result.forEach { p1 ->
            result.forEach { p2 -> if (p1.first != p2.first) {
                val d1 = (p1.first - p1.second).norm2()
                val d2 = (p2.first - p2.second).norm2()
                val x1 = (p1.first - p2.second).norm2()
                val x2 = (p2.first - p1.second).norm2()
                assertTrue(sqrt(d1) + sqrt(d2) <= sqrt(x1) + sqrt(x2), "Line segments $p1 and $p2 cross")
            } }
        }
    }
}

private fun Random.nextPoint() :Point2D = Point2D(nextDouble(), nextDouble())
