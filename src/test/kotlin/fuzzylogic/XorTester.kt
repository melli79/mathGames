package fuzzylogic

import common.math.epsilon
import kotlin.math.abs
import kotlin.test.*

class XorTester {
    val sample = listOf(0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0)

    @Test fun symmetric() {
        for (x in sample) {
            for (y in sample) {
                assertEquals(fuzzyTranspose(x, y), fuzzyTranspose(y, x), "x=$x, y=$y")
            }
        }
    }

    @Test fun diamAssociative() {
        for (x in sample) {
            for (y in sample) {
                for (z in sample) {
                    assertTrue(abs(fuzzyDiamond(x, fuzzyDiamond(y, z)) - fuzzyDiamond(fuzzyDiamond(x, y), z)) < epsilon, "x=$x, y=$y, z=$z")
                }
            }
        }
    }
}
