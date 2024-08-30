package geometry

import kotlin.test.*

class OF3Tester {
    val p = 5u
    val s2 = (0u..< p).flatMap { v0 -> (0u..< p).flatMap { v1 -> (0u..< p).map { v2 -> F3(p, v0,v1,v2) } } }
        .filter { v -> v.dot(v)==1u }

    @Test fun s1elements() {
        println("S1 = $s2")
        println("|S1| = ${s2.size}")
        assertTrue { 12 < s2.size }
    }

    val s_2 = (0u..< p).flatMap { v0 -> (0u..< p).flatMap { v1 -> (0u..< p).map { v2 -> F3(p, v0,v1,v2) } } }
        .filter { v -> v.dot(v)==p-1u }

    @Test fun s_1elements() {
        println("S_1 = $s_2")
        println("|S_1| = ${s_2.size}")
        assertTrue { 12 < s_2.size }
    }

    @Test fun o3() {
        val one = EndF3.eye(p)
        val o3 = s2.flatMap { v0 -> s2.filter { v1 -> v0.dot(v1)==0u }.flatMap { v1 ->
            s2.filter { v2 -> v0.dot(v2)==0u && v1.dot(v2)==0u }.map { v2 -> EndF3(v0,v1,v2) } }
        }
        println("|O_{F_$p}(3)| = ${o3.size}")
        assertTrue { 12 < o3.size }
    }

    val eta = EndF3.eta12(p)
    val c12 = (0u..< p).flatMap { v0 -> (0u..< p).flatMap { v1 -> (0u..< p).map { v2 -> F3(p, v0, v1, v2) } } }
        .filter { v -> (eta*v).dot(v)==1u }

    @Test fun c12elements() {
        println("C_F$p(\\eta) = $c12")
        println("|C_F$p(\\eta)| = ${c12.size}")
        assertTrue { 8 < c12.size }
    }

    val c21 = (0u..< p).flatMap { v0 -> (0u..< p).flatMap { v1 -> (0u..< p).map { v2 -> F3(p, v0, v1, v2) } } }
        .filter { v -> (eta*v).dot(v)==p-1u }


    @Test fun o12() {
        val o12 = c12.flatMap { v0 ->
            val v0t = eta*v0
            c21.filter { v1 -> v0t.dot(v1)==0u }.flatMap { v1 ->
                val v1t = eta*v1
                c21.filter { v2 -> v0t.dot(v2)==0u && v1t.dot(v2)==0u }
                    .map { v2 -> EndF3(v0, v1, v2) }
            }
        }
        o12.forEach { R -> assertEquals(eta, R.transpose()*eta*R) }
        println("|O_F$p(1,2)| = ${o12.size}")
        assertTrue { 12 < o12.size }
    }
}
