package partitions

import kotlin.test.*

class NumberOfPairsTester {
    @Test fun singleChoice() {
        assertEquals(1, estimateNumberOfPairs(1u))
    }

    @Test fun twoOfThree() {
        assertEquals(3, estimateNumberOfPairs(2u))
    }

    @Test fun twoCombinations() {
        val result = designCards(2u)
        println("pairs: $result")
        for (first in result)  for (second in result)  if (first != second) {
            assertEquals(1, (first intersect second).size, "$first and $second have wrong intersection")
        }
    }

    @Test fun threeOfSeven() {
        assertEquals(7, estimateNumberOfPairs(3u))
    }

    @Test fun threeCombinations() {
        val result = designCards(3u)
        println("triples: $result")
        for (first in result)  for (second in result)  if (first != second) {
            assertEquals(1, (first intersect second).size, "$first and $second have wrong intersection")
        }
    }

    @Test fun fourCombinations() {
        val result = designCards(4u)
        println("quadruples: $result")
        for (first in result)  for (second in result)  if (first != second) {
            assertEquals(1, (first intersect second).size, "$first and $second have wrong intersection")
        }
    }

    @Test fun fiveCombinations() {
        val result = designCards(5u)
        println("pentuples: $result")
        for (first in result)  for (second in result)  if (first != second) {
            assertEquals(1, (first intersect second).size, "$first and $second have wrong intersection")
        }
    }

    @Test fun firstOnes() {
        println((1u..10u).map { k -> Pair(k, optimizeNumberOfCards(k.toUByte()).second) })
    }
}
