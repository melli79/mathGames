package cardgame

sealed interface Rank :Comparable<Rank> {
    override fun compareTo(other :Rank) :Int {
        return when (this) {
            is HighCard -> when (other) {
                is HighCard -> this.compareTo(other)
                else -> -1
            }
            is OnePair -> when (other) {
                is HighCard -> 1
                is OnePair -> this.compareTo(other)
                else -> -1
            }
            is TwoPairs -> when (other) {
                is HighCard, is OnePair -> 1
                is TwoPairs -> this.compareTo(other)
                else -> -1
            }
            is ThreeofaKind -> when (other) {
                is HighCard, is OnePair, is TwoPairs -> 1
                is ThreeofaKind -> this.compareTo(other)
                else -> -1
            }
            is Straight -> when (other) {
                is Straight -> this.compareTo(other)
                is Flush, is FullHouse, is FourofaKind, is StraightFlush, is RoyalFlush -> -1
                else -> 1
            }
            is Flush -> when (other) {
                is Flush -> this.compareTo(other)
                is FullHouse, is FourofaKind, is StraightFlush, is RoyalFlush -> -1
                else -> 1
            }
            is FullHouse -> when (other) {
                is FullHouse -> this.compareTo(other)
                is FourofaKind, is StraightFlush, is RoyalFlush -> -1
                else -> 1
            }
            is FourofaKind -> when (other) {
                is FourofaKind -> this.compareTo(other)
                is StraightFlush, is RoyalFlush -> -1
                else -> 1
            }
            is StraightFlush -> when (other) {
                is StraightFlush -> this.compareTo(other)
                is RoyalFlush -> -1
                else -> 1
            }
            is RoyalFlush -> if (other is RoyalFlush) 0  else 1
        }
    }

    data class HighCard(val value :Suite) :Rank {
        override fun toString() = "a(n) $value"

        fun compareTo(other :HighCard) = value.compareTo(other.value)
    }

    data class OnePair(val value :Suite) :Rank {
        override fun toString() = "Pair of ${value}s"

        fun compareTo(other :OnePair) = value.compareTo(other.value)
    }

    data class TwoPairs(val higherValue :Suite, val lowerValue :Suite) :Rank {
        override fun toString() = "Two pairs ${higherValue}s and ${lowerValue}s"

        fun compareTo(other :TwoPairs) = Comparator
            .comparing<TwoPairs, Suite> { it.higherValue }
            .thenComparing<Suite> { it.lowerValue }
            .compare(this, other)
    }

    data class ThreeofaKind(val value :Suite) :Rank {
        override fun toString() = "Three ${value}s"

        fun compareTo(other :ThreeofaKind) = value.compareTo(other.value)
    }

    data class Straight(val toValue :Suite) :Rank {
        override fun toString() = "Straight to $toValue"

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

        fun compareTo(other :FullHouse) = tripleValue.compareTo(other.tripleValue)
    }

    data class FourofaKind(val value :Suite) :Rank {
        override fun toString() = "Four ${value}s"
    }

    data class StraightFlush(val toValue :Suite) :Rank {
        override fun toString() = "Straight Flush up to $toValue"

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
        fun of(hand :Set<Card>) :List<Rank> {
            val rf = RoyalFlush.find(hand)
            if (rf!=null)
                return listOf(rf)
            val groups = hand.groupBy { it.value }.entries
                .map { (v :Suite, cards) -> Pair(cards.size, v) }
                .groupBy { it.first }
            val size = groups.keys.max()
            if (size==4) {
                val value = groups[size]!!.first().second
                return listOf(FourofaKind(value))
            }
            if (size==3) {
                val triple = groups[size]!!.first().second
                if (2 in groups.keys)
                    return listOf(FullHouse(triple))
                else
                    return listOf(ThreeofaKind(triple))
            }
            if (size==2) {
                val pairs = groups[size]!!
                val pair = pairs.first().second
                if (pairs.size>1) {
                    val second = pairs[1].second
                    return listOf(TwoPairs(pair, second))
                } else {
                    val highValue = hand.map { it.value }.filter { it!=pair }.max()
                    return listOf(OnePair(pair), HighCard(highValue))
                }
            }
            val flush = Flush.find(hand)
            if (flush!=null)
                return listOf(StraightFlush.find(hand) ?: flush)
            val straight = Straight.find(hand)
            if (straight!=null)
                return listOf(straight)
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