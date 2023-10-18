package cardgame

enum class Color {
    Diamonds, Hearts, Spades, Clubs;

    val symbol = name[0]

    companion object {
        fun of(c :Char) :Color? {
            val symbol = c.uppercase()[0]
            return entries.firstOrNull { it.symbol==symbol }
        }
    }
}

enum class Suite(val symbol :Char) {
    Two('2'), Three('3'), Four('4'), Five('5'), Six('6'), Seven('7'), Eight('8'), Nine('9'),
    Ten('T'), Jack('J'), Queen('Q'), King('K'), Ace('A');

    companion object {
        fun pred(value :Suite) = when (value) {
            Three -> Two
            Four -> Three
            Five -> Four
            Six -> Five
            Seven -> Six
            Eight -> Seven
            Nine -> Eight
            Ten -> Nine
            Jack -> Ten
            Queen -> Jack
            King -> Queen
            Ace -> King
            else -> null
        }

        fun of(c :Char) :Suite? {
            val symbol = c.uppercase()[0]
            return entries.firstOrNull { it.symbol==symbol }
        }

        val lowCards = setOf(Two, Three, Four, Five)
    }

    val pred :Suite?
        get() = pred(this)
}

data class Card(val value :Suite, val color :Color) {
    override fun toString() = "${value.symbol}${color.symbol}"

    fun describe() = "${value.name} of ${color.name}"

    companion object {
        fun of(value :String) :Card? {
            val symbols = value.trim()
            if (symbols.length<2)
                return null
            val suite = Suite.of(symbols[0]) ?: return null
            val color = Color.of(symbols[1]) ?: return null
            return Card(suite, color)
        }
    }
}

data class Hand(val cards :Set<Card>) {
    override fun toString() = cards.joinToString(prefix= "{", postfix= "}")

    fun rank() = Rank.of(cards)

    companion object {
        fun of(cards :String) :Hand {
            return Hand(cards.split("\\s+".toRegex())
                .mapNotNull { Card.of(it) }
                .toSet())
        }
    }
}
