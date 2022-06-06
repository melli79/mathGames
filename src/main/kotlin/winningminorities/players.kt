package winningminorities

import kotlin.random.Random

val random = Random(System.currentTimeMillis())

object optimist :Player {
    override fun toString() = "optimist"
    override fun describe() = "Optimist"

    override fun choose(numPlayers: UInt) = Player.Choice.GO

    override fun reward(r: Player.Reward, numPlayers: UInt) {}
    override fun reset(numPlayers: UInt) {}
}

object pessimist :Player {
    override fun toString() = "pessimist"
    override fun describe() = "Pessimist"

    override fun choose(numPlayers: UInt) = Player.Choice.STAY

    override fun reward(r: Player.Reward, numPlayers: UInt) {}
    override fun reset(numPlayers: UInt) {}
}

open class RandomPlayer(var bias :Double =0.5) :Player {
    override fun toString() = this::class.java.simpleName
    override fun describe() = "RandomPlayer($bias)"

    override fun choose(numPlayers :UInt) = if (random.nextDouble()<=bias)
        Player.Choice.GO
    else
        Player.Choice.STAY

    override fun reset(numPlayers :UInt) {}
    override fun reward(r :Player.Reward, numPlayers :UInt) {}
}

class AdjustingRandomPlayer :RandomPlayer() {
    override fun describe() = toString()

    private var n = 0

    override fun reset(numPlayers :UInt) {
        bias = 0.5
        n = 0
    }

    override fun reward(r :Player.Reward, numPlayers :UInt) {
        n++
        bias = bias +if(r is Player.Win) (1.0-bias)/n  else (-1.0-bias)/n
    }
}

class MoodyPlayer :Player {
    override fun describe() = "MoodyPlayer"
    override fun toString() = describe()

    private var nextChoice = Player.Choice.GO

    override fun reset(numPlayers :UInt) {
        nextChoice = Player.Choice.GO
    }

    override fun choose(numPlayers :UInt) = nextChoice

    override fun reward(r :Player.Reward, numPlayers :UInt) {
        nextChoice = when (r) {
            is Player.Win -> Player.Choice.GO
            else -> Player.Choice.STAY
        }
    }
}

class MultiStrategyPlayer(private val strategies :List<Strategy>) :Player {
    constructor(m :Int = 5, len :Short = 3)
        :this((1..m).map { Strategy(len) })

    val m :Int = strategies.size
    val len :Short = strategies.first().size.toShort()
    private var ws :MutableList<Int> = (1..m).map { 0 }.toMutableList()

    override fun toString() = "MultiStrategyPlayer($m, $len)"
    override fun describe() = ws.mapIndexed { i, w -> Pair(w, i) }.sortedBy { p -> -p.first }
        .map { p -> "${p.first}:${strategies[p.second]}" }
        .joinToString(separator = ", ", prefix = "MultiStrategyPlayer<", postfix = ">")

    private var last = (1..m).map { if (random.nextBoolean()) Player.Win(1.0) else Player.Loss(1.0) }
    private lateinit var lastChoice :Player.Choice

    override fun reset(numPlayers: UInt) {
        ws = (1..m).map { 0 }.toMutableList()
    }

    override fun choose(numPlayers :UInt) :Player.Choice {
        val m = ws.max()
        val bests = ws.mapIndexed { i, w -> if (w==m) i  else null }.filterNotNull()
        val gos = bests.count { i -> strategies[i].eval(last) == Player.Choice.GO }
        lastChoice = if (2* gos >= bests.size) Player.Choice.GO  else Player.Choice.STAY
        return lastChoice
    }

    override fun reward(r: Player.Reward, numPlayers: UInt) {
        if (r is Player.Win)
            (0 until m).map { i ->
                if (strategies[i].eval(last)==lastChoice)
                    ws[i]++
            }
        else
            (0 until m).map { i ->
                if (strategies[i].eval(last)==lastChoice)
                    ws[i]--
            }

        last = listOf(r)+last.subList(0, len-1)
    }

    // a deterministic Strategy (may only depend on the `len` last wins/losses)
    class Strategy(len :Short = 3) {
        private val ws = (1..len).map { random.nextDouble()-0.5 }

        override fun toString() = "$ws"
        val size = ws.size

        fun eval(last :List<Player.Reward>) = if (last.dot(ws)>=0.0)
            Player.Choice.GO
        else
            Player.Choice.STAY

        fun List<Player.Reward>.dot(ws: List<Double>) = ws.mapIndexed { i, w ->
            if (get(i) is Player.Win) w  else -w
        }.sum()
    }
}
