package dynamic

import kotlin.test.*

class MatchingTester {
    @Test fun empty() {
        val result = match(emptySet<Man>(), emptySet<Woman>())
        assertEquals(emptySet(), result.first)
        assertEquals(emptySet(), result.second)
        assertEquals(emptySet(), result.third)
    }

    data class Man(val qualities :Array<IntArray>, val id :Int) :Binary<Man, Woman> {
        override fun toString() = "m$id"
        override fun compare(b :Woman, b2 :Woman) = qualities[id-1][b.id-1] -qualities[id-1][b2.id-1]
        override fun equals(other :Any?) :Boolean {
            return other is Man && id == other.id
        }
        override fun hashCode() = 31+ id.hashCode()
    }

    data class Woman(val qualities :Array<IntArray>, val id :Int) :Binary<Woman, Man> {
        override fun toString() = "w$id"
        override fun compare(b :Man, b2 :Man) = qualities[b.id-1][id-1] -qualities[b2.id-1][id-1]
        override fun equals(other :Any?) :Boolean {
            return other is Woman && id == other.id
        }
        override fun hashCode() = 37+ id.hashCode()
    }

    @Test fun singlePair() {
        val qs = arrayOf(intArrayOf(1))
        val m1 = Man(qs, 1)
        val w1 = Woman(qs, 1)
        val result = match(setOf(m1), setOf(w1))
        assertEquals(emptySet(), result.second)
        assertEquals(emptySet(), result.third)
        assertEquals(setOf(Pair(m1, w1)), result.first)
    }

    @Test fun twoPairs() {
        val qs = arrayOf(intArrayOf(1,0), intArrayOf(0,1))
        val m1 = Man(qs, 1);  val m2 = Man(qs, 2)
        val w1 = Woman(qs, 1);  val w2 = Woman(qs, 2)
        val result = match(setOf(m1, m2), setOf(w1, w2))
        println("Matches: ${result.first}, leftovers: ${result.second}, ${result.third}")
        assertEquals(emptySet(), result.second)
        assertEquals(emptySet(), result.third)
        assertEquals(setOf(Pair(m1, w1), Pair(m2, w2)), result.first)
    }

    @Test fun twoPairsReversed() {
        val qs = arrayOf(intArrayOf(0,1), intArrayOf(1,0))
        val m1 = Man(qs, 1);  val m2 = Man(qs, 2)
        val w1 = Woman(qs, 1);  val w2 = Woman(qs, 2)
        val result = match(setOf(m1, m2), setOf(w1, w2))
        println("Matches: ${result.first}, leftovers: ${result.second}, ${result.third}")
        assertEquals(emptySet(), result.second)
        assertEquals(emptySet(), result.third)
        assertEquals(setOf(Pair(m1, w2), Pair(m2, w1)), result.first)
    }

    @Test fun bestCouple() {
        val qs = arrayOf(intArrayOf(1,2), intArrayOf(0,0))
        val m1 = Man(qs, 1);  val m2 = Man(qs, 2)
        val w1 = Woman(qs, 1);  val w2 = Woman(qs, 2)
        val result = match(setOf(m1, m2), setOf(w1, w2)).first
        assertEquals(2, result.size)
        assertTrue(Pair(m1, w2) in result, "Cannot find maximal choice")
    }

    @Test fun optimal() {
        val qs = arrayOf(intArrayOf(0, 4), intArrayOf(5, 10))
        val m1 = Man(qs, 1);  val m2 = Man(qs, 2)
        val w1 = Woman(qs, 1);  val w2 = Woman(qs, 2)
        val result = match(setOf(m1, m2), setOf(w1, w2)).first
        val quality = result.sumOf { p -> qs[p.first.id-1][p.second.id-1] }
        assertTrue(quality >= 10, "expected quality >= 10, but was $quality.")
    }
}
