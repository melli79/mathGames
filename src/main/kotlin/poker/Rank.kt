package poker

sealed interface Rank :Comparable<Rank> {

    data class HighCard(val value :Suite) :Rank {
        override fun toString() = "a(n) $value"

        override fun compareTo(other: Rank) =
            if (other is HighCard)
                this.compareTo(other)
            else
                -1

        fun compareTo(other :HighCard) = value.compareTo(other.value)
    }

    data class OnePair(val value :Suite) :Rank {
        override fun toString() = "Pair of ${value}s"

        override fun compareTo(other: Rank) = when (other) {
            is HighCard -> 1
            is OnePair -> this.compareTo(other)
            else -> -1
        }

        fun compareTo(other :OnePair) = value.compareTo(other.value)
    }

    data class TwoPairs(val higherValue :Suite, val lowerValue :Suite) :Rank {
        override fun toString() = "Two pairs ${higherValue}s and ${lowerValue}s"

        override fun compareTo(other: Rank) = when (other) {
            is HighCard, is OnePair -> 1
            is TwoPairs -> this.compareTo(other)
            else -> -1
        }

        fun compareTo(other :TwoPairs) = Comparator
            .comparing<TwoPairs, Suite> { it.higherValue }
            .thenComparing<Suite> { it.lowerValue }
            .compare(this, other)
    }

    data class ThreeofaKind(val value :Suite) :Rank {
        override fun toString() = "Three ${value}s"

        override fun compareTo(other: Rank) = when (other) {
            is HighCard, is OnePair, is TwoPairs -> 1
            is ThreeofaKind -> this.compareTo(other)
            else -> -1
        }

        fun compareTo(other :ThreeofaKind) = value.compareTo(other.value)
    }

    data class Straight(val toValue :Suite) :Rank {
        override fun toString() = "Straight to $toValue"

        override fun compareTo(other: Rank) = when (other) {
            is Straight -> this.compareTo(other)
            is Flush, is FullHouse, is FourofaKind, is StraightFlush, is RoyalFlush -> -1
            else -> 1
        }

        fun compareTo(other :Straight) = toValue.compareTo(other.toValue)

        companion object {
            fun find(hand :Collection<Card>) :Straight? {
                if (hand.size<5) return null
                val values = hand.map { it.value }.toSet()
                val highestValue = values.max()
                if (nothing(highestValue, values))
                    return null
                return Straight(highestValue)
            }

            fun nothing(highestValue :Suite, values :Set<Suite>) :Boolean {
                if (highestValue in Suite.lowCards) return true
                if (highestValue.pred !in values) return true
                if (highestValue.pred!!.pred !in values) return true
                if (highestValue.pred!!.pred!!.pred !in values) return true
                return highestValue.pred!!.pred!!.pred!!.pred !in values
            }
        }
    }

    data class Flush(val highestValue :Suite, val color :Color) :Rank {
        override fun toString() = "Flush of $color with $highestValue"

        override fun compareTo(other: Rank) = when (other) {
            is Flush -> this.compareTo(other)
            is FullHouse, is FourofaKind, is StraightFlush, is RoyalFlush -> -1
            else -> 1
        }

        fun compareTo(other :Flush) = highestValue.compareTo(other.highestValue)

        companion object {
            fun find(hand :Collection<Card>) :Flush? {
                if (hand.size<5) return null
                val suite = hand.groupBy { it.color }.maxBy { it.value.size }
                if (suite.value.size<5) return null
                val color = suite.value.first().color
                val values = hand.map { it.value }.toSet()
                val toValue = values.max()
                return Flush(toValue, color)
            }
        }
    }

    data class FullHouse(val tripleValue :Suite) :Rank {
        override fun toString() = "Full House of ${tripleValue}s"

        override fun compareTo(other: Rank) = when (other) {
            is FullHouse -> this.compareTo(other)
            is FourofaKind, is StraightFlush, is RoyalFlush -> -1
            else -> 1
        }

        fun compareTo(other :FullHouse) = tripleValue.compareTo(other.tripleValue)
    }

    data class FourofaKind(val value :Suite) :Rank {
        override fun toString() = "Four ${value}s"

        override fun compareTo(other: Rank) = when (other) {
            is FourofaKind -> this.compareTo(other)
            is StraightFlush, is RoyalFlush -> -1
            else -> 1
        }

        fun compareTo(other :FourofaKind) = value.compareTo(other.value)
    }

    data class StraightFlush(val toValue :Suite) :Rank {
        override fun toString() = "Straight Flush up to $toValue"

        override fun compareTo(other: Rank) = when (other) {
            is StraightFlush -> this.compareTo(other)
            is RoyalFlush -> -1
            else -> 1
        }

        fun compareTo(other :StraightFlush) = toValue.compareTo(other.toValue)

        companion object {
            fun find(hand :Set<Card>) :StraightFlush? {
                if (hand.size<5)
                    return null
                val suites = hand.groupBy { it.color }
                val straight = suites.entries.firstOrNull { (_, seq :List<Card>) -> seq.size>=5 } ?: return null
                val cards = straight.value.map { it.value }.toSet()
                val highestValue = cards.max()
                if (Straight.nothing(highestValue, cards))
                    return null
                return StraightFlush(highestValue)
            }
        }
    }

    data class RoyalFlush(val color :Color) :Rank {
        override fun toString() = "Royal flush of $color"

        override fun compareTo(other: Rank) =
            if (other is RoyalFlush) 0
            else 1

        companion object {
            fun find(hand :Set<Card>) :RoyalFlush? {
                if (hand.size<5)
                    return null
                val ace = hand.firstOrNull { it.value== Suite.Ace } ?: return null
                val color = ace.color
                hand.firstOrNull { it.value== Suite.King && it.color==color } ?: return null
                hand.firstOrNull { it.value== Suite.Queen && it.color==color } ?: return null
                hand.firstOrNull { it.value== Suite.Jack && it.color==color } ?: return null
                hand.firstOrNull { it.value== Suite.Ten && it.color==color } ?: return null
                return RoyalFlush(color)
            }
        }
    }

    companion object {
        fun of(hand :Set<Card>) =
            canRoyalFlush(hand) ?:
            canTuples(hand) ?:
            canFlush(hand) ?:
            canStraight(hand) ?:
            findHighCards(hand)

        private fun canRoyalFlush(hand :Set<Card>) :List<Rank>? {
            val rf = RoyalFlush.find(hand)
            if (rf!=null)
                return listOf(rf)
            return null
        }

        private fun canTuples(hand :Set<Card>) :List<Rank>? {
            val groups: Map<Int, List<Pair<Int, Suite>>> = hand.groupBy { it.value }.entries
                .map { (v :Suite, cards) -> Pair(cards.size, v) }
                .groupBy { it.first }
            val maxTupleSize = groups.keys.max()
            return canFour(groups, maxTupleSize) ?:
                canTriple(groups, maxTupleSize) ?:
                canPairs(groups, maxTupleSize, hand)
        }

        private fun canFour(groups: Map<Int, List<Pair<Int, Suite>>>, maxTupleSize :Int) :List<Rank>? {
            if (maxTupleSize==4) {
                val value = groups[maxTupleSize]!!.first().second
                return listOf(FourofaKind(value))
            }
            return null
        }

        private fun canTriple(groups: Map<Int, List<Pair<Int, Suite>>>, maxTupleSize :Int) :List<Rank>? {
            if (maxTupleSize==3) {
                val triple = groups[maxTupleSize]!!.first().second
                return if (2 in groups.keys)
                    listOf(FullHouse(triple))
                else
                    listOf(ThreeofaKind(triple))
            }
            return null
        }

        private fun canPairs(groups :Map<Int, List<Pair<Int, Suite>>>, maxTupleSize :Int, hand :Set<Card>) :List<Rank>? {
            if (maxTupleSize==2) {
                val pairs = groups[maxTupleSize]!!
                val pair = pairs.first().second
                return if (pairs.size>1) {
                    val second = pairs[1].second
                    listOf(TwoPairs(pair, second))
                } else {
                    val highValue = hand.map { it.value }.filter { it!=pair }.max()
                    listOf(OnePair(pair), HighCard(highValue))
                }
            }
            return null
        }

        private fun canFlush(hand :Set<Card>) :List<Rank>? {
            val flush = Flush.find(hand)
            if (flush!=null)
                return listOf(StraightFlush.find(hand) ?: flush)
            return null
        }

        private fun canStraight(hand :Set<Card>) :List<Rank>? {
            val straight = Straight.find(hand)
            if (straight!=null)
                return listOf(straight)
            return null
        }

        private fun findHighCards(hand: Set<Card>): List<HighCard> {
            val values = hand.map { it.value }.sortedDescending()
            return values.map { HighCard(it) }
        }
    }
}

// Comparing for lexicographic ordering
operator fun <C :Comparable<C>> List<C>.compareTo(other :List<C>) :Int {
    val i = iterator();  val j = other.iterator()
    while (i.hasNext()&&j.hasNext()) {
        val cmp = i.next().compareTo(j.next())
        if (cmp!=0)
            return cmp
    }
    if (i.hasNext())
        return 1
    return if (j.hasNext()) -1  else 0
}