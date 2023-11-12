package factorgame

class Server(val limit :UShort) {
    var firstPlayer :Player? =null
        private set
    var otherPlayer :Player? =null
        private set
    private lateinit var board :MutableSet<UInt>
    private var lastMove = 0u

    fun add(player :Player) {
        if (firstPlayer==null)
            firstPlayer = player
        else if (otherPlayer==null)
            otherPlayer = player
        else
            error("Already 2 players in the game.")
    }

    fun startGame() {
        check (firstPlayer==null || otherPlayer==null) { "Missing a Player before we can start" }
        board = (1u..limit.toUInt()).toMutableSet()
        lastMove = 0u
        firstPlayer!!.startGame(limit)
        otherPlayer!!.startGame(limit)
    }

    fun runGame() {
        startGame()
        while (board.isNotEmpty()) {
            val ended = when ((limit.toInt()-board.size)%2) {
                0 -> play(firstPlayer!!)
                else -> play(otherPlayer!!)
            }
            if (ended)
                break
        }
        when ((limit.toInt()-board.size)%2) {
            0 -> {
                otherPlayer!!.reward(Reward.WIN)
                firstPlayer!!.reward(Reward.LOSE)
            }
            else -> {
                otherPlayer!!.reward(Reward.LOSE)
                firstPlayer!!.reward(Reward.WIN)
            }
        }
    }

    private fun play(player :Player) :Boolean {
        println("${player.name}'s turn.")
        if (board.isLost(lastMove))
            return true
        while (true) {
            val choice = player.choose(board, lastMove)
            if (choice==0u) {
                println("${player.name} is giving up.")
                return true
            }
            if (choice in board && (lastMove==0u||choice%lastMove==0u||lastMove%choice==0u)) {
                if (lastMove==0u&&choice%2u!=0u)
                    continue
                println("${player.name}'s choice: $choice")
                board.remove(choice)
                lastMove = choice
                return board.isEmpty()
            }
        }
    }
}

fun Board.isLost(lastMove :UInt) = findOptions(lastMove).isEmpty()

fun Board.findOptions(lastMove :UInt) :Set<UInt> = when (lastMove) {
    0u -> filter { o -> o%2u == 0u }.toSet()
    else -> filter { it%lastMove==0u || lastMove%it==0u }.toSet()
}
