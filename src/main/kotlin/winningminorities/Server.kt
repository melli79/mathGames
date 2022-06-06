package winningminorities

import common.math.epsilon
import kotlin.math.abs

class Server(players0 :Collection<Player>) {
    private val players = mutableListOf<Player>()
    private val games = mutableListOf<Pair<UInt, UInt>>()
    private var scores = listOf<Double>()

    var numRounds = 1000

    init {
        players.addAll(players0)
    }

    fun reset() {
        games.clear()
        scores = (1..players.size).map { 0.0 }
        val numPlayers = players.size.toUInt()
        players.forEach { p -> p.reset(numPlayers) }
    }

    fun runGame() :Pair<List<Pair<UInt, UInt>>, List<Double>> {
        reset()
        for (round in 1..numRounds) {
            val result = playRound()
            scores = scores.mapIndexed { p, v -> v + result.second[p].value }
            games.add(result.first)
        }
        return Pair(games, scores)
    }

    fun playRound() :Pair<Pair<UInt, UInt>, List<Player.Reward>> {
        val numPlayers = players.size.toUInt()
        val choices = players.map { p -> p.choose(numPlayers) }
        val gos = choices.count { c -> c==Player.Choice.GO }
        val stays = numPlayers.toInt() -gos
        val mean = (gos-stays)/numPlayers.toDouble()
        val reward = 1.0 -mean
        val loss = 1.0 +mean
        val rewards = if (gos<=stays)
            choices.mapIndexed { i, c ->
                val r = if (c==Player.Choice.GO) Player.Win(reward)  else Player.Loss(loss)
                players[i].reward(r, numPlayers)
                r
            }
        else
            choices.mapIndexed { i, c ->
                val r = if (c==Player.Choice.STAY) Player.Win(loss)  else Player.Loss(reward)
                players[i].reward(r, numPlayers)
                r
            }

        return Pair(Pair(gos.toUInt(), stays.toUInt()), rewards)
    }

    fun describeGame() {
        val bestScore = scores.max()
        val bests = scores.mapIndexed { i, s -> if (s==bestScore) i else null }.filterNotNull()
        println("bests with $bestScore are :${describePlayers(bests)}")

        val secondScore = scores.filter { s -> s<bestScore }.maxOrNull() ?: Double.MIN_VALUE
        val seconds = scores.mapIndexed { i, s -> if (s==secondScore) i else null }.filterNotNull()

        val thirdScore = scores.filter { s -> s<secondScore }.maxOrNull() ?: Double.MIN_VALUE
        val thirds = scores.mapIndexed { i, s -> if (s==thirdScore) i  else null }.filterNotNull()

        val worstScore = scores.min()
        val worsts = scores.mapIndexed { i, s -> if (s==worstScore) i  else null }.filterNotNull()

        if (seconds.isNotEmpty() && seconds!=worsts)
            println("second with $secondScore are :${describePlayers(seconds)}")
        if (thirds.isNotEmpty() && thirds!=worsts)
            println("third with $thirdScore are :${describePlayers(thirds)}")
        println("Worst with $worstScore are: ${describePlayers(worsts)}")

        val balance = scores.sum()
        println("sum score: $balance, "+ if(abs(balance)<5*epsilon*numRounds) "Ok."  else "weak!")
    }

    private fun describePlayers(bests: List<Int>): String {
        if (bests.size==1)
            return players[bests.first()].describe()
        val results = bests.groupBy { b -> b.toString() }
        return results.entries.joinToString(", ") { e ->
            if (e.value.size==1) players[e.value.first()].describe()
            else "${e.value.size} x ${e.key}"
        }
    }
}
