import Prisoner.Choice.*
import kotlin.math.min

class Server {
    private val prisoners = mutableListOf<Prisoner>()
    private var tournamentSize = 10000
    private val tournament = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>>()
    private val scores = mutableListOf<Int>()

    fun addPrisoner(prisoner :Prisoner) = prisoners.add(prisoner)
    fun getPrisoners() :List<Prisoner> = prisoners
    fun getScores() :List<Int> = scores
    fun getTournament() :Map<Pair<Int, Int>, Pair<Int, Int>> = tournament
    fun getTournamentSize() :Int = tournamentSize

    fun longer() {
        tournamentSize += tournamentSize/5
    }

    fun shorter() {
        tournamentSize -= tournamentSize/6
        if (tournamentSize<5)  tournamentSize = 5
    }

    fun playTournament() :Pair<List<Int>, Map<Pair<Int, Int>, Pair<Int, Int>>> {
        val matches = (0 until prisoners.size).map { p ->
            ((0 until p).toList() + (p+1 until prisoners.size).toList()).toMutableSet()
        }
        tournament.clear()
        scores.clear();  (0 until prisoners.size).forEach { scores.add(0) }
        while (matches.any { m -> m.isNotEmpty() }) {
            val players = (0 until prisoners.size).toMutableSet()
            while (players.isNotEmpty()) { // every player a game
                val p = players.first()
                players.remove(p)
                val ps = matches[p]
                val q = ps.firstOrNull() ?: continue
                if (!players.remove(q)) {
                    (0 until prisoners.size).forEach { r -> if (r!=p&&r!=q) players.add(r) }
                }
                ps.remove(q); matches[q].remove(p)

                val result = battle(prisoners[p], prisoners[q])
                tournament[Pair(p, q)] = result.first
                scores[p] += result.first.first;  scores[q] += result.first.second
            }
        }
        return Pair(scores, tournament)
    }

    fun battle(p1 :Prisoner, p2 :Prisoner) :Pair<Pair<Int, Int>, List<Pair<Prisoner.Reward, Prisoner.Reward>>> {
        val match = mutableListOf<Pair<Prisoner.Reward, Prisoner.Reward>>()
        var v1 = 0;  var v2 = 0
        p1.restart();  p2.restart()
        for (turn in 0 until tournamentSize) {
            val c1 = p1.choose();  val c2 = p2.choose()
            val rewards = when {
                c1==QUIT || c2==QUIT -> Pair(Prisoner.Quit(tournamentSize-turn), Prisoner.Quit(tournamentSize-turn))
                c1==TRICK && c2==TRICK -> Pair(Prisoner.KnownReward.LOSE, Prisoner.KnownReward.LOSE)
                c1==COOPERATE && c2==COOPERATE -> Pair(Prisoner.KnownReward.JOIN, Prisoner.KnownReward.JOIN)
                c1==TRICK -> Pair(Prisoner.KnownReward.WIN, Prisoner.KnownReward.LOSE)
                else -> Pair(Prisoner.KnownReward.LOSE, Prisoner.KnownReward.WIN)
            }
            match.add(rewards)
            p1.reward(rewards.first); p2.reward(rewards.second)
            v1 += rewards.first.value;  v2 += rewards.second.value
            if (c1==QUIT || c2==QUIT)  break
        }
        p1.finish(v1, tournamentSize);  p2.finish(v2, tournamentSize)
        return Pair(Pair(v1, v2), match)
    }
}

fun Server.printResults() {
    val prisoners = getPrisoners()
    val tournament = getTournament()
    val scores = getScores()
    println("${prisoners.size} prisoners, ${getTournamentSize()} rounds, each against each other.\n")
    println("Prisoner".padEnd(12)+"\t" +
            prisoners.joinToString(separator = "\t"){ p ->
                val result = p.toString()
                result.substring(0 until min(5, result.length)).padEnd(5)
            } +"\t| Total\t Rank")
    repeat(prisoners.size+3) { print("---------") };  println()
    val ranks = scores.mapIndexed { i, s -> Pair(s, i) }.sortedByDescending { p -> p.first }
        .mapIndexed { r, p -> Pair(p.second, r) }.toMap()
    prisoners.forEachIndexed { p, prisoner ->
        val name = prisoner.toString()
        print("${name.substring(0 until min(11, name.length)).padEnd(12)}\t")
        for (q in prisoners.indices) {
            print((tournament[Pair(p, q)]?.first?.toString()
                ?: tournament[Pair(q, p)]?.second?.toString()
                ?: "---").padStart(5)+"\t")
        }
        println("| "+ scores[p].toString().padStart(6)+"\t"+rank(ranks[p]!!, prisoners.size))
    }
}

fun rank(rk :Int, size :Int) :Int = if (rk < size-3) rk+1 else rk-size
