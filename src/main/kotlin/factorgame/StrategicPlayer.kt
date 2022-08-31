package factorgame

import kotlin.system.exitProcess

class StrategicPlayer(override val name :String ="Strategic Player") :BasicStrategy(), Player {
    private var limit :UShort? =null
    private val values = mutableMapOf<Pair<Board, UInt>, Reward>()

    override fun startGame(limit :UShort) {
        if (limit!=this.limit) {
            values.clear()
            this.limit = limit
        }
    }

    override fun choose(board :Board, lastMove :UInt) :UInt {
        val options = findOptions(board, lastMove).sorted()
        if (options.isEmpty()) {
            values[Pair(board, lastMove)] = Reward.LOSE
            return 0u
        }
        val evaluation = values[Pair(board, lastMove)]
        if (evaluation== Reward.LOSE)  return 0u
        var candidate :UInt? =null
        for (option in options) {
            val answer = estimate(board - setOf(option), option)
            if (answer== Reward.LOSE) {
                if (evaluation!= Reward.WIN) {
                    values[Pair(board, lastMove)] = Reward.WIN
                    if (lastMove==0u)
                        printWinningStrategyAndExit(option)
                }
                return option
            } else if (answer!= Reward.WIN &&candidate==null)
                candidate = option
        }
        return candidate ?: options[random.nextInt(options.size)]
    }

    private fun printWinningStrategyAndExit(move :UInt) {
        println("There is a winning strategy from the beginning: $move")
        val board :Board = (1u..limit!!.toUInt()).filter { m -> m != move }.toSet()
        for (option in findOptions(board, move)) {
            println("when $option counter with ${choose(board-setOf(option), option)}")
        }
        exitProcess(0)
    }

    fun estimate(board :Board, lastMove :UInt, depth :UInt =2u) :Reward? {
        val key = Pair(board, lastMove)
        if (key in values)
            return values[key]
        if (depth==0u)  return null
        val options = findOptions(board, lastMove).sorted()
        if (options.isEmpty()) {
            values[key] = Reward.LOSE
            return Reward.LOSE
        }
        var canWin = false
        for (option in options) {
            val answer = estimate(board - setOf(option), option, depth-1u)
            if (answer== Reward.LOSE) {
                values[key] = Reward.WIN
                return Reward.WIN
            } else if (answer!=Reward.WIN)
                canWin = true
        }
        if (!canWin) {
            values[key] = Reward.LOSE
            return Reward.LOSE
        }
        return null
    }

    override fun reward(r :Reward) {}
}