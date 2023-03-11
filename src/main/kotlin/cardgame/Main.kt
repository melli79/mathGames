package cardgame

import xmasaoc.getInput
import xmasaoc.toReader

private val resourceName = "p054-poker.data"

fun main() {
    val input = getInput(null, resourceName).toReader()
    var wins = 0;  var ties = 0;  var losses = 0
    input.lines().forEach { game ->
        val cards = game.split("\\s+".toRegex()).map { card -> Card.of(card) }
        if (cards.any { it==null })
            println("Problems reading cards: '$game', got only $cards")
        else if (cards.size!=10)
            println("Incomplete game, expected: 10 cards,  got: ${cards.size} -- $cards")
        else {
            val hand1 = Hand((cards as List<Card>).take(5).toSet())
            val hand2 = Hand(cards.drop(5).toSet())
            val cmp = hand1.rank().compareTo(hand2.rank())
            when {
                cmp<0 -> losses++
                cmp>0 -> wins++
                else -> ties++
            }
        }
    }
    val total = wins + losses + ties
    println("$wins of $total" +if (ties>0) " -- $ties errors" else ", no errors")
}
