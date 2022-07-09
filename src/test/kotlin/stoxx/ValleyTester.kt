package stoxx

import kotlin.test.*

class ValleyTester {
    @Test fun noValley() {
        assertEquals(emptyList(), findValleys(listOf(1)))
    }

    @Test fun leftValley() {
        assertEquals(listOf(Pair(0,2)), findValleys(listOf(0,1,2)))
    }

    @Test fun twoValleys() {
        assertEquals(listOf(Pair(0,2), Pair(2,4)), findValleys(listOf(3,1,2,0,4)))
    }
}
