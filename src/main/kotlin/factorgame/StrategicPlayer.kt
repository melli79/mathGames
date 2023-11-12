package factorgame

import kotlin.system.exitProcess

class StrategicPlayer(override val name :String ="Strategic Player") :Player {
    private var limit :UShort = 0u
    private val values = mutableMapOf<Pair<Board, UInt>, Reward>()
    private var currentMove :Pair<Board, UInt>? =null

    override fun startGame(limit :UShort) {
        if (limit!=this.limit) {
            values.clear()
            this.limit = limit
        }
    }

    override fun choose(board :Board, lastMove :UInt) :UInt {
        val options = board.findOptions(lastMove).sorted().toMutableList()
        if (options.isEmpty()) {
            values[Pair(board, lastMove)] = Reward.LOSE
            currentMove = null
            return 0u
        }
        if (options.first()==1u) {
            options.remove(1u)
            if (options.isEmpty()) {
                currentMove = Pair(board, 1u)
                return 1u
            }
        }
        val evaluation = values[Pair(board, lastMove)]
        if (evaluation == Reward.LOSE) {
            currentMove = null
            return 0u
        }
        return options.findBestMove(board, evaluation, lastMove)
    }

    private fun List<UInt>.findBestMove(
        board: Board,
        evaluation: Reward?,
        lastMove: UInt
    ): UInt {
        var candidate: UInt? = null
        for (option in this) {
            val answer = estimate(board - setOf(option), option)
            if (answer == Reward.LOSE) {
                if (evaluation != Reward.WIN) {
                    values[Pair(board, lastMove)] = Reward.WIN
                    if (lastMove == 0u)
                        printWinningStrategyAndExit(option)
                }
                currentMove = null
                return option
            } else if (answer != Reward.WIN)
                candidate = if (candidate == null)
                    option
                else
                    null
        }
        val move = candidate ?: this.random(random)
        currentMove = Pair(board, move)
        return move
    }

    fun showWinningStrategy() {
        val board :Board = (1u..limit.toUInt()).toSet()
        for (s in 1u..(limit/2u)) {
            if (values[Pair(board, 2u*s)] == Reward.WIN)
                printWinningStrategyAndExit(2u*s)
        }
    }

    private fun printWinningStrategyAndExit(move :UInt) {
        println("There is a winning strategy from the beginning: $move")
        val board :Board = (1u..limit.toUInt()).filter { m -> m != move }.toSet()
        for (option in board.findOptions(move)) {
            println("when $option counter with ${choose(board-setOf(option), option)}")
        }
        exitProcess(0)
    }

    fun estimate(board :Board, lastMove :UInt, depth :UInt =2u) :Reward? {
        val key = Pair(board, lastMove)
        if (key in values)
            return values[key]
        if (depth==0u)  return null
        val options = board.findOptions(lastMove).sorted()
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

    override fun reward(r :Reward) {
        if (currentMove!=null) {
            values[currentMove!!] = r
        }
    }
}
