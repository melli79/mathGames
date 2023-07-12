package numberthy

import grayDecode
import grayEncode
import kotlin.test.*

class GrayTester {
    @Test fun zero() {
        assertEquals(0uL, grayDecode("0"))
        assertEquals("0", grayEncode(0uL))
    }

    @Test fun one() {
        assertEquals(1uL, grayDecode("1"))
        assertEquals("1", grayEncode(1uL))
    }

    @Test fun firstNumbers() {
        for ((n, input) in listOf("0", "1", "11", "10", "110", "111", "101", "100", "1100").mapIndexed { n, input -> Pair(n.toULong(), input) }) {
            val result = grayDecode(input)
            println("$input = $result")
            assertEquals(n, result)
        }
    }

    @Test fun encodeFirstNumbers() {
        for (n in 0..15) {
            val result = grayEncode(n.toULong())
            println("$n = $result")
            assertEquals(n.toULong(), grayDecode(result))
        }
    }
}
