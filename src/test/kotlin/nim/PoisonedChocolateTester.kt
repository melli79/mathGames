package nim

import kotlin.test.*

class PoisonedChocolateTester {
    @Test fun lastPiece() {
        assertFalse(PoisonedChocolate.canWin(1u, 1u))
    }

    @Test fun for1row() {
        assertTrue(PoisonedChocolate.canWin(2u, 1u))
        assertTrue(PoisonedChocolate.canWin(3u, 1u))
        assertTrue(PoisonedChocolate.canWin(1u, 2u))
        assertTrue(PoisonedChocolate.canWin(1u, 3u))
    }

    @Test fun for2rows() {
        assertTrue(PoisonedChocolate.canWin(2u, 2u))
    }

    @Test fun smallSizes() {
        for (r in 1u..10u) {
            for (c in r..10u) {
                print("${r}x${c}: "+ PoisonedChocolate.canWin(r, c)+",  ")
                assertEquals(PoisonedChocolate.canWin(r, c), PoisonedChocolate.canWin(c, r))
            }
            println()
        }
    }
}
