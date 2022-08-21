package partitions

import kotlin.test.*

class NumberOfPairsTester {
    @Test fun singleChoice() {
        assertEquals(1, estimateNumberOfPairs(1u))
    }

    @Test fun twoOfThree() {
        assertEquals(3, estimateNumberOfPairs(2u))
    }

    @Test fun threeOfSeven() {
        assertEquals(7, estimateNumberOfPairs(3u))
    }

    @Test fun firstOnes() {
        println((1u..10u).map { k -> Pair(k, optimizeNumberOfCards(k.toUByte()).second) })
    }
}
