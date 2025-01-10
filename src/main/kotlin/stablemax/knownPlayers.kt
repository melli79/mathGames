package stablemax

import common.math.statistics.cumsum
import common.math.statistics.poisson
import kotlin.math.max
import kotlin.random.Random
import kotlin.random.nextUInt

val random = Random(System.currentTimeMillis())

class RandomPlayer(val name: String = "UniformRandomPlayer") :Player {
    override fun choice(limit :UInt, numPlayers :UShort) = random.nextUInt(limit)

    override fun toString() = name
}

class HighPlayer(val name: String = "HighPlayer") :Player {
    override fun choice(limit :UInt, numPlayers :UShort) = limit-1u

    override fun toString() = name
}

class PoissonPlayer(val name: String = "Poisson'Player", val lambda :Double =0.95) : Player {
    private var thresholds = listOf<Double>()
    override fun choice(limit :UInt, numPlayers :UShort) :UInt {
        val r = random.nextDouble()
        return thresholds.indexOfFirst { thr -> thr>=r }.toUInt()
    }

    override fun reset(limit :UInt, numPlayers :UShort) {
        thresholds = listOf(0.0) + ((0u..< limit-1u).reversed()).map { k -> poisson(k,lambda) }
        thresholds = thresholds.cumsum()
        val f = 1/thresholds.last()
        thresholds = thresholds.map { it*f }
    }

    override fun toString() = name
}

class DynamicPlayer(val name: String = "DynamicPlayer") : Player {
    private var values = mutableListOf<Double>()
    private var thresholds = listOf<Double>()
    private var lr = 0.05
    private val lrd = 0.999
    private var lastChoice = 0u

    override fun choice(limit :UInt, numPlayers :UShort) :UInt {
        val r = random.nextDouble()
        lastChoice = thresholds.indexOfFirst { thr -> thr >= r }.toUInt()
        return lastChoice
    }

    override fun reward(limit :UInt, numPlayers :UShort, reward :Reward) {
        values[lastChoice.toInt()] = max(0.0, values[lastChoice.toInt()] + lr*reward.value)
        updateThresholds()
        lr *= lrd
    }

    override fun reset(limit :UInt, numPlayers :UShort) {
        val lambda = 0.95
        values = ((0u..< limit).reversed()).map { k -> poisson(k,lambda) }.toMutableList()
        println(describe())
        updateThresholds()
    }

    private fun updateThresholds() {
        thresholds = values.cumsum()
        val f = 1 / thresholds.last()
        thresholds = thresholds.map { it * f }
    }

    override fun toString() = name
    override fun describe() :String {
        val f = 1/values.sum()
        return toString() + ": " + values.mapIndexed { ch, value -> Pair(ch, value) }.reversed()
                .joinToString(", ") { (ch, value) -> "$ch: %.3f".format(value*f) }
    }
}
