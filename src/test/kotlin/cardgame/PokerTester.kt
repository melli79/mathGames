package cardgame

import kotlin.test.*

class PokerTester {
    @Test fun onePair() {
        val hand1 = Hand.of("5H 5C 6S 7S KD")
        println(hand1)
        val rank1 = hand1.rank()
        println(rank1)
        assertEquals(Rank.OnePair(Suite.Five), rank1.first())
        val hand2 = Hand.of("2C 3S 8S 8D TD")
        println(hand2)
        val rank2 = hand2.rank()
        println(rank2)
        assertEquals(Rank.OnePair(Suite.Eight), rank2.first())
        assertTrue(rank2 > rank1)
    }

    @Test fun highCards() {
        val hand1 = Hand.of("5D 8C 9S JS AC")
        val rank1 = hand1.rank()
        println("$hand1: $rank1")
        assertEquals(Rank.HighCard(Suite.Ace), rank1.first())
        val hand2 = Hand.of("2C 5C 7D 8S QH")
        val rank2 = hand2.rank()
        println("$hand2: $rank2")
        assertEquals(Rank.HighCard(Suite.Queen), rank2.first())
        assertTrue(rank1 > rank2)
    }

    @Test fun threeAndFlush() {
        val hand1 = Hand.of("2D 9C AS AH AC")
        val rank1 = hand1.rank()
        println("$hand1: $rank1")
        assertEquals(Rank.ThreeofaKind(Suite.Ace), rank1.first())
        val hand2 = Hand.of("3D 6D 7D TD QD")
        val rank2 = hand2.rank()
        println("$hand2: $rank2")
        assertEquals(Rank.Flush(Suite.Queen, Color.Diamonds), rank2.first())
        assertTrue(rank2 > rank1)
    }

    @Test fun pairsAndHighCards() {
        val hand1 = Hand.of("4D 6S 9H QH QC")
        val rank1 = hand1.rank()
        println("$hand1: $rank1")
        assertEquals(Rank.OnePair(Suite.Queen), rank1.first())
        assertTrue(rank1.size>1)
        assertEquals(Rank.HighCard(Suite.Nine), rank1[1])
        val hand2 = Hand.of("3D 6D 7H QD QS")
        val rank2 = hand2.rank()
        println("$hand2: $rank2")
        assertEquals(Rank.OnePair(Suite.Queen), rank2.first())
        assertTrue(rank2.size > 1)
        assertEquals(Rank.HighCard(Suite.Seven), rank2[1])
        assertTrue(rank1 > rank2)
    }

    @Test fun fullHouse() {
        val hand1 = Hand.of("2H 2D 4C 4D 4S")
        val rank1 = hand1.rank()
        println("$hand1: $rank1")
        assertEquals(Rank.FullHouse(Suite.Four), rank1.first())
        val hand2 = Hand.of("3C 3D 3S 9S 9D")
        val rank2 = hand2.rank()
        println("$hand2: $rank2")
        assertEquals(Rank.FullHouse(Suite.Three), rank2.first())
        assertTrue(rank1 > rank2)
    }

    @Test fun fourAndFullHouse() {
        val hand1 = Hand.of("KC KS KH KD 9S")
        val rank1 = hand1.rank()
        println("$hand1: $rank1")
        assertEquals(Rank.FourofaKind(Suite.King), rank1.first())
        val hand2 = Hand.of("AC AS AH QS QD")
        val rank2 = hand2.rank()
        println("$hand2: $rank2")
        assertEquals(Rank.FullHouse(Suite.Ace), rank2.first())
        assertTrue(rank2 < rank1)
    }
}
