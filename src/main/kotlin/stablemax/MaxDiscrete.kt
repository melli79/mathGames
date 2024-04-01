package stablemax

fun main() {
    val game = MaxDiscrete
    game.addPlayer(HighPlayer())
    game.addPlayer(HighPlayer())
    game.addPlayer(RandomPlayer())
    game.addPlayer(RandomPlayer())
    val dynamicPlayer = DynamicPlayer()
    game.addPlayer(dynamicPlayer)

    val statistics = game.runGame(10u, 10_000u).entries.sortedByDescending { it.value.score }
    println(statistics.mapIndexed { rk, entry -> "${rk}th: ${entry.key.describe()} with ${entry.value.score} (${entry.value.numWins} wins, ${entry.value.numOverbids} overbids, and ${entry.value.numLosses} losses)" }.joinToString("\n")
            +"\n\n")
    game.removePlayer(dynamicPlayer)
}

object MaxDiscrete {
    private val players = mutableListOf<Player>()
    private val statistics = mutableMapOf<Player, Statistics>()

    private var limit = 0u
    private var playersChanged = false

    fun addPlayer(player: Player) {
        players.add(player)
        playersChanged = true
    }

    fun removePlayer(player: Player) {
        players.remove(player)
        playersChanged = true
    }

    fun runGame(limit: UInt = this.limit, numRounds :UInt =1000u) :Map<Player, Statistics> {
        val numPlayers = players.size.toUShort()
        if (playersChanged || limit!=this.limit) {
            this.limit = limit
            statistics.clear()
            players.forEach {
                statistics[it] = Statistics(0u, 0u, 0u)
                it.reset(limit, numPlayers)
            }
        }
        for (n in 1u..numRounds) {
            val choices = mutableMapOf<UInt, MutableList<Player>>()
            for (player in players) {
                val choice = player.choice(limit, numPlayers)
                if (choice>=limit)
                    statistics[player]!!.loss()
                else
                    choices.computeIfAbsent(choice) { mutableListOf() }.add(player)
            }
            var won = false
            for (entry in choices.entries.sortedByDescending { it.key }) {
                if (won)
                    entry.value.forEach {
                        it.reward(limit, numPlayers, Reward.Overbid)
                        statistics[it]!!.overbid()
                    }
                else if (entry.value.size > 1)
                    entry.value.forEach {
                        it.reward(limit, numPlayers, Reward.Loss)
                        statistics[it]!!.loss()
                    }
                else {
                    entry.value.forEach {
                        it.reward(limit, numPlayers, Reward.Win)
                        statistics[it]!!.win()
                    }
                    won = true
                }
            }
        }
        return statistics
    }

    data class Statistics(var numWins :UInt, var numLosses :UInt, var numOverbids :UInt) {
        val numGames :UInt
            get() = numWins+numLosses+numOverbids

        val score :Long
            get() = numWins.toLong()*Reward.Win.value.toLong() +numOverbids.toLong()*Reward.Overbid.value.toLong() +
                    numLosses.toLong()*Reward.Loss.value.toLong()

        fun win() = ++numWins
        fun overbid() = ++numOverbids
        fun loss() = ++numLosses
    }
}
