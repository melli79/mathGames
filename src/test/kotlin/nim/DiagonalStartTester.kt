package nim

import kotlin.test.*

class DiagonalStartTester {
    val runner = DiagonalStart
    @Test fun zeroIsWon() {
        assertTrue(runner.canWin(0u))
    }

    @Test fun oneIsWon() {
        assertTrue(runner.canWin(1u))
    }

    @Test fun twoIsLost() {
        assertFalse(runner.canWin(2u))
    }

    @Test fun threeIsLost() {
        assertFalse(runner.canWin(3u))
    }

    @Test fun smallNumbers() {
        println((0u..20u).joinToString { it.toString()+": "+runner.canWin(it).toString() })
    }
}
