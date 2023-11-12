package factorgame

import kotlin.random.Random

val random = Random(System.currentTimeMillis())

class HumanPlayer(override val name: String) :Player {
    override fun choose(board :Board, lastMove :UInt) :UInt {
        while (true) {
            print("$board, $lastMove, your choice (0 for giving up): ")
            val input = readln().trim()
            if (input.startsWith("?") || input.startsWith("h", true)) {
                println("Your options: ${board.findOptions(lastMove)}")
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

class MaxPlayer(override val name: String ="Max Player") :Player {
    override fun choose(board :Board, lastMove :UInt) =
        board.findOptions(lastMove).maxOrNull() ?: 0u
}

class RandomPlayer(override val name :String ="Random Player") :Player {
    override fun choose(board :Board, lastMove :UInt) :UInt {
        val options = board.findOptions(lastMove)
        return options.toList().pickAnyOrNull() ?: 0u
    }
}

fun <E> List<E>.pickAnyOrNull() :E? {
    if (isEmpty())
        return null
    return get(random.nextInt(size))
}

