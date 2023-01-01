package trivia

import kotlin.test.*

class AckermannTester {
    @Test fun m0() {
        println("0: "+(0..10).joinToString{ n -> ackermann(0u, n.toULong()).toString() })
    }

    @Test fun m1() {
        println("1: "+(0..10).joinToString{ n -> ackermann(1u, n.toULong()).toString() })
    }

    @Test fun m2() {
        println("2: "+(0..10).joinToString{ n -> ackermann(2u, n.toULong()).toString() })
    }

    @Test fun m3() {
        println("3: "+(0..13).joinToString{ n -> ackermann(3u, n.toULong()).toString() })
    }

    @Test fun m4() {
        println("4: "+(0..1).joinToString{ n -> ackermann(4u, n.toULong()).toString() })
    }
}
