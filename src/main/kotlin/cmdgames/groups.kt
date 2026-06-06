package cmdgames

/**
 * Groups game
 *
 * cards differ by Repetition, Shape, Shading, Color
 *
 * Three cards form a group iff they cannot be separated by any one feature
 * into 2 piles.
 */

import random
import kotlin.random.Random

// val random = Random(System.currentTimeMillis())

enum class Feature {
  REP, SHAPE, COLOR, SHADE;
}

enum class Repetition {
  Once, Twice, Thrice, FiveTimes;

  fun repeat(str :String) = when (this) {
    Once -> str
    Twice -> str+str
    Thrice -> str+str+str
    FiveTimes -> str+str+str+str+str
  }
}
fun Random.nextRepetition() = Repetition.entries.random(this)

enum class Shape(val symbol :Char) {
  Diamonds('D'), Heart('H'), Spade('S'), Club('C');
}
fun Random.nextShape() = Shape.entries.random(this)

enum class Color(val symbol :Char) {
  Black('k'), Blue('b'), Green('g'), Red('r');

  fun setColor() = when (this) {
    Black -> "\u001B[90;100m"
    Blue -> "\u001B[34;100m"
    Green -> "\u001B[32;100m"
    Red -> "\u001B[31;100m"
  }

  fun setInverseColor() = when (this) {
    Black -> "\u001B[30;47m"
    Blue -> "\u001B[30;44m"
    Green -> "\u001B[97;42m"
    Red -> "\u001B[30;41m"
  }

  companion object {
    fun reset() = "\u001B[39m\u001B[49m"
  }
}
fun Random.nextColor() = Color.entries.random(this)

enum class Shading {
  Solid, Inverse, Outlined, Dotted;

  fun shade(text :String, color :Color) = when (this) {
    Solid -> color.setColor()+text
    Inverse -> color.setInverseColor()+text
    Outlined -> color.setColor()+"|$text|"
    Dotted -> color.setColor()+":$text:"
  }
}
fun Random.nextShading() = Shading.entries.random(this)

data class Card(val shape :Shape, val rep :Repetition, val color :Color, val shade :Shading) {
  override fun toString() = shade.shade(rep.repeat(shape.symbol.toString()), color) +
    Color.reset()

  fun differ(other :Card) :Set<Feature> {
      val result = mutableSetOf<Feature>()
      if (shape!=other.shape) result.add(Feature.SHAPE)
      if (rep!=other.rep) result.add(Feature.REP)
      if (color!=other.color) result.add(Feature.COLOR)
      if (shade!=other.shade) result.add(Feature.SHADE)
      return result
  }
}
fun Random.nextCard() = Card(nextShape(), nextRepetition(), nextColor(), nextShading())

class Game(val deck :MutableList<Card> =createShuffledDeck(), val opens :MutableSet<Card> =mutableSetOf()) {
  companion object {
    fun isGroup(vararg cards :Card) :Boolean {
      if (cards.size!=3) return false
      val difference = cards[0].differ(cards[1])
      return difference == cards[0].differ(cards[2]) && difference == cards[1].differ(cards[2])
    }

    fun createShuffledDeck() :MutableList<Card> {
      val result =
        Shape.entries.take(3).flatMap { shape ->
          Repetition.entries.take(3).flatMap { rep ->
            Color.entries.drop(1).flatMap { color ->
              Shading.entries.take(3).map { Card(shape, rep, color, it) }
            }
          }
        }.toMutableList()
      result.shuffle(random)
      return result
    }
  }

  fun findGroup() :Set<Card>? {
    val sorted = mutableMapOf<Set<Feature>, MutableSet<Card>>()
    for (card in opens)
      for (card2 in opens) if (card!=card2) {
        val features = card.differ(card2)
        val set = sorted.computeIfAbsent(features) { mutableSetOf(card, card2) }
        set.add(card); set.add(card2)
        if (set.size>=3) {
          val triple = set.take(3).toTypedArray<Card>()
          if (isGroup(*triple))
            return triple.toSet()
          if (set.size>=4) {
            val fourth = set.first { it !in triple }
            val t2 = arrayOf(triple.first(), triple[1], fourth)
            if (isGroup(*t2))  return t2.toSet()
            val t3 = arrayOf(triple.first(), triple[2], fourth)
            if (isGroup(*t3))  return t3.toSet()
            val t4 = arrayOf(triple[1], triple[2], fourth)
            if (isGroup(*t4))  return t4.toSet()
          }
        }
      }
    return null
  }

  fun revealCard() :Card? {
    val result = deck.removeFirstOrNull()
    if (result!=null)
      opens.add(result)
    return result
  }
}

fun main() {
  val game = Game()
  while (true) {
    game.revealCard() ?: break
    val group = game.findGroup()
    println("Open cards:  ${game.opens} -- contains "+
            (group?.toString() ?: "no group")
    )
    if (group!=null) break
  }
}
