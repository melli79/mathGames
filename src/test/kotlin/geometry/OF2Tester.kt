package geometry

import kotlin.test.*

class OF2Tester {
    val p = 5u
    val s1 = (0u..< p).flatMap { v0 -> (0u..< p).map { v1 -> F2(p, v0, v1) } }.filter { v -> v.dot(v)==1u }

    @Test fun s1elements() {
        println("S1_F$p = $s1")
        assertEquals(4, s1.size)
    }

    val o2 = s1.flatMap { v0 -> s1.filter { v1 -> v0.dot(v1)==0u }.map { v1 -> EndF2(p, v0.v0,v0.v1, v1.v0,v1.v1) } }

    @Test fun o2elements() {
        println("O_{F_$p}(2) = $o2")
        println("|O_{F_$p}(2)| = ${o2.size}")
        assertTrue { 8 <= o2.size }
    }
}
