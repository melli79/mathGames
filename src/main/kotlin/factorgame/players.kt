package factorgame

import kotlin.random.Random

val random = Random(System.currentTimeMillis())

class HumanPlayer(override val name: String) :BasicStrategy(), Player {
    override fun choose(board :Board, lastMove :UInt) :UInt {
        while (true) {
            print("$board, $lastMove, your choice (0 for giving up): ")
            val input = readln().trim()
            if (input.startsWith("?") || input.startsWith("h", true)) {
                println("Your options: ${findOptions(board, lastMove)}")
                continue
            }
            val choice = input.toInt()
            if (choice==0 || choice.toUInt() in board)
                return choice.toUInt()
        }
    }

    override fun startGame(limit :UShort) {
        println("We play upto $limit")
    }

    override fun reward(r :Reward) = when (r) {
        Reward.WIN -> println("$name won the game!")
        Reward.LOSE -> println("$name lost the game!")
    }
}

class MaxPlayer(override val name: String ="Max Player") :BasicStrategy(), Player {
    override fun choose(board :Board, lastMove :UInt) =
        findOptions(board, lastMove).maxOrNull() ?: 0u

    override fun startGame(limit :UShort) {}
    override fun reward(r :Reward) {}
}

class RandomPlayer(override val name :String ="Random Player") : BasicStrategy(), Player {
    override fun choose(board :Board, lastMove :UInt) :UInt {
        return findOptions(board, lastMove).toList().pickAnyOrNull() ?: 0u
    }

    override fun startGame(limit :UShort) {}
    override fun reward(r :Reward) {}
}

fun <E> List<E>.pickAnyOrNull() :E? {
    if (isEmpty())
        return null
    return get(random.nextInt(size))
}

