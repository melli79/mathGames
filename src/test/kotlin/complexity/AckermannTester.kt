package complexity

import rational.ipow
import kotlin.test.*

class AckermannTester {
    @Test fun m1() {
        for (n in 0uL..10uL) {
            assertEquals(n+2uL, ackermann(1u, n))
        }
    }

    @Test fun m2() {
        for (n in 0uL..10uL) {
            assertEquals(2u*n+3uL, ackermann(2u, n))
        }
    }

    @Test fun m3() {
        for (n in 0u..8u) {
            assertEquals(2uL.ipow((n+3u).toUByte())-3uL, ackermann(3u, n.toULong()))
        }
    }

    @Test fun m4() {
        for (n in 0u..2u) {
            println("a(4, $n) = 2^^($n+3) -3 = ${ackermann(4u, n.toULong())}")
        }
    }

    @Test fun t1() {
        for (n in 1uL..10uL) {
            assertEquals(2u*n, t(1u, n))
        }
    }

    @Test fun t2() {
        for (n in 1u..10u) {
            assertEquals(2uL.ipow(n.toUByte()), t(2u, n.toULong()))
        }
    }

    @Test fun t3() {
        for (n in 1u..4u) {
            println("t(3, $n) = 2^^n = ${t(3u, n.toULong())}")
        }
    }
}
