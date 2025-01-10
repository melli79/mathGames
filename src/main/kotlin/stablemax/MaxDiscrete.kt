package stablemax

fun main() {
    val game = MaxDiscrete
    game.addPlayer(HighPlayer())
    game.addPlayer(HighPlayer())
    game.addPlayer(RandomPlayer())
    game.addPlayer(PoissonPlayer())
    val dynamicPlayer = DynamicPlayer()
    game.addPlayer(dynamicPlayer)

    val statistics = game.runGame(20u, 100_000u).entries.sortedByDescending { it.value.score }
    println(statistics.mapIndexed { rk, entry -> """${th(rk+1)}: ${entry.key.describe()}
      |   with ${entry.value.score} Pts (${entry.value.numWins} wins, ${entry.value.numOverbids} overbids, and ${entry.value.numLosses} losses)""".trimMargin("|")
    }.joinToString("\n"))
}

fun th(rk :Int) = if (rk/10 %10 == 1) "${rk}th"  else when (rk%10) {
    1 -> "${rk}st"
    2 -> "${rk}nd"
    3 -> "${rk}rd"
    else -> "${rk}th"
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
                    statistics[player]!!.lose()
                else
                    choices.computeIfAbsent(choice) { mutableListOf() }.add(player)
            }
            var won = false
            for ((_, pplayers) in choices.entries.sortedByDescending { it.key }) {
                if (won)
                    pplayers.forEach {
                        it.reward(limit, numPlayers, Reward.Overbid)
                        statistics[it]!!.overbid()
                    }
                else if (pplayers.size > 1)
                    pplayers.forEach {
                        it.reward(limit, numPlayers, Reward.Loss)
                        statistics[it]!!.lose()
                    }
                else {
                    pplayers.forEach {
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
        fun lose() = ++numLosses
    }
}
