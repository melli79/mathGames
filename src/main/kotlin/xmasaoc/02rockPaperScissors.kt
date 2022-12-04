package xmasaoc

import java.io.InputStream
import java.lang.System.currentTimeMillis
import kotlin.random.Random

enum class Hand {
    ROCK, PAPER, SCISSORS;

    fun win(other :Hand) = when(this) {
        ROCK -> when (other) {
            ROCK -> 0
            PAPER -> -1
            SCISSORS -> 1
        }
        PAPER -> when (other) {
            ROCK -> 1
            PAPER -> 0
            SCISSORS -> -1
        }
        SCISSORS -> when (other) {
            ROCK -> -1
            PAPER -> 1
            SCISSORS -> 0
        }
    }
}

private class Game(val player1 :Player, val player2 :Player) {
    interface Player {
        fun startGame() {}
        fun showHand(round :Short) :Hand
        fun reward(round :Short, win :Int) {}
        fun finishGame(winBy :Int) {}
    }

    companion object {
        const val DEFAULT_TOURNAMENT_LENGTH = 10
    }

    private var score1 = 0
    private var score2 = 0

    fun play(length :Int = DEFAULT_TOURNAMENT_LENGTH) :Pair<Int, Int> {
        score1 = 0;  score2 = 0
        player1.startGame();  player2.startGame()
        repeat(length) {
            playRound(it.toShort())
        }
        println("$player1 has $score1 points, $player2 has $score2 points.")
        player1.finishGame(score1-score2);  player2.finishGame(score2-score1)
        return Pair(score1, score2)
    }

    private fun playRound(round :Short) {
        val hand1 = player1.showHand(round)
        val hand2 = player2.showHand(round)
        val win = hand1.win(hand2)
        var score = score(hand1, win)
        player1.reward(round, score)
        score1 += score
        score = score(hand2, -win)
        player2.reward(round, score)
        score2 += score
    }

    private fun score(hand :Hand, win :Int) = when (hand) {
        Hand.ROCK -> 1
        Hand.PAPER -> 2
        Hand.SCISSORS -> 3
    } +3*(win+1)
}

val random = Random(currentTimeMillis())

private class RandomPlayer(val suffix :String ="") :Game.Player {
    override fun toString() = RandomPlayer::class.java.simpleName+suffix

    override fun showHand(round :Short) = random.nextOf(Hand.values())
}

private fun <T> Random.nextOf(values :Array<T>) = values[nextInt(values.size)]

private const val RESOURCE_NAME = "02rpsPlan.txt"

private class RiggedPlayer(plan :List<Pair<Char, Char>>, val mode :Int =1) :Game.Player {
    val plan :List<Hand>

    init {
        val moves = plan.map { hand(it.first) }
        this.plan = when (mode) {
            1 -> moves
            -2 -> balance(plan, moves)
            else -> plan.map { hand(it.second-23) }
        }
    }

    private fun balance(plan :List<Pair<Char, Char>>, moves :List<Hand>) = plan.zip(moves).map { (p, c) ->
        when (p.second) {
            'X' -> loseAgainst(c)
            'Y' -> tieAgainst(c)
            'Z' -> winAgainst(c)
            else -> throw IllegalArgumentException("invalid option2 in '$p'.")
        }
    }

    private fun hand(option :Char) = when (option) {
        'A' -> Hand.ROCK
        'B' -> Hand.PAPER
        'C' -> Hand.SCISSORS
        else -> throw IllegalArgumentException("invalid option1 on line '$option'.")
    }

    private fun winAgainst(choice :Hand) = when (choice) {
        Hand.ROCK -> Hand.PAPER
        Hand.PAPER -> Hand.SCISSORS
        Hand.SCISSORS -> Hand.ROCK
    }

    private fun tieAgainst(choice :Hand) = choice

    private fun loseAgainst(choice :Hand) = when (choice) {
        Hand.ROCK -> Hand.SCISSORS
        Hand.PAPER -> Hand.ROCK
        Hand.SCISSORS -> Hand.PAPER
    }

    private var move = 0

    override fun toString() = RiggedPlayer::class.java.simpleName+mode

    override fun startGame() {
        move = 0
    }

    override fun showHand(round :Short) = plan[move++]

    override fun finishGame(winBy :Int) {
        if (move<plan.size)
            println("Premature stop of the game.  Missing ${plan.size-move} moves.")
    }
}

fun main(args: Array<String>) {
    val input = getInput(args.firstOrNull(), RESOURCE_NAME)
    val plan = parsePlan(input)
    val game = Game(RiggedPlayer(plan), RiggedPlayer(plan, -2))
    game.play(plan.size)
}

private fun parsePlan(inputStream :InputStream) :List<Pair<Char, Char>> {
    val result = mutableListOf<Pair<Char, Char>>()
    val input = inputStream.toReader()
    input.forEachLine { line ->
        val choices = line.split(' ')
        val choice1 = choices[0][0]
        val choice2 = choices[1][0]
        result.add(Pair(choice1, choice2))
    }
    return result
}
