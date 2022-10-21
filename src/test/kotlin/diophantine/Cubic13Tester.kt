package diophantine

import kotlin.test.*

class Cubic13Tester {
    @Test fun zero() {
        val result = findTriples13(0u)
        assertEquals(setOf(setOf(0)), result)
    }

    @Test fun upTo1() {
        val result = findTriples13(1u)
        assertEquals(setOf(setOf(0), setOf(-1,0,1)), result)
    }

    @Test fun upTo2() {
        val result = findTriples13(2u)
        assertEquals(setOf(setOf(0), setOf(-1,0,1), setOf(-2,0,2)), result)
    }

    @Test fun upTo3() {
        val result = findTriples13(3u)
        assertEquals(setOf(setOf(0), setOf(-1,0,1), setOf(-2,0,2), setOf(1,2,3), setOf(-3,-2,-1), setOf(-3,0,3)), result)
    }

    @Test fun upTo100() {
        val result = findTriples13(100u)
            .map {
                val t = it.sorted()
                val first = t.first()
                Triple(first, t.getOrElse(1){first}, t.getOrElse(2){first})
            }.sortedWith(compareBy(Triple<Int, Int, Int>::first, Triple<Int, Int, Int>::second, Triple<Int, Int, Int>::third))
        println(result)
        assertEquals(103, result.size)
    }
}
