import kotlin.random.Random

object Good :Prisoner {
    override fun toString() = "Good"
    override fun restart() {}

    override fun choose() = Prisoner.Choice.COOPERATE

    override fun reward(r :Prisoner.Reward) {}

    override fun finish(winnings :Int, numRounds :Int) {}
}

object Evil :Prisoner {
    override fun toString() = "Evil"
    override fun restart() {}

    override fun choose() = Prisoner.Choice.TRICK

    override fun reward(r :Prisoner.Reward) {}

    override fun finish(winnings :Int, numRounds :Int) {}
}

class Brutal :Prisoner {
    private var quit = false

    override fun toString() = "Brutal"
    override fun restart() {
        quit = false
    }

    override fun choose() = if (quit)
        Prisoner.Choice.QUIT
    else
        Prisoner.Choice.TRICK

    override fun reward(r :Prisoner.Reward) {
        if (r!= Prisoner.KnownReward.WIN)
            quit = true
    }

    override fun finish(winnings :Int, numRounds :Int) {}
}

object TitForTat :Prisoner {
    private var last = Prisoner.Choice.COOPERATE

    override fun toString() = "Tit for Tat"

    override fun restart() {
        last = Prisoner.Choice.COOPERATE
    }

    override fun choose() = last

    override fun reward(r :Prisoner.Reward) = when (r) {
        Prisoner.KnownReward.LOSE -> last = Prisoner.Choice.TRICK
        else -> last = Prisoner.Choice.COOPERATE
    }

    override fun finish(winnings :Int, numRounds :Int) {}
}

// Pattern length must be at least 1
class PatternPrisoner(val pattern :List<Prisoner.Choice>) :Prisoner {
    var next = 0

    override fun toString() = pattern.joinToString(separator = "") { c -> c.name[0].toString() }

    override fun restart() {
        next = 0
    }

    override fun choose() = pattern[next]

    override fun reward(r :Prisoner.Reward) {
        next = (next+1) % pattern.size
    }

    override fun finish(winnings :Int, numRounds :Int) {}
}

class TitForTatBilance :Prisoner {
    private var last = Prisoner.Choice.COOPERATE
    private var winnings = 0
    private var round = 0

    override fun toString() = "T4T Bilanced"
    override fun restart() {
        last = Prisoner.Choice.COOPERATE
        winnings = 0
        round = 0
    }

    override fun choose() = if (round<5||winnings>=1.9*round)
        last
    else
        Prisoner.Choice.QUIT

    override fun reward(r :Prisoner.Reward) {
        if (r !is Prisoner.Quit) {
            winnings += r.value
            round ++
        }
    }

    override fun finish(winnings :Int, numRounds :Int) {}
}

val random = Random(System.currentTimeMillis())

open class RandomPrisoner(var bias :Double = 0.5) :Prisoner {
    override fun toString() = """RandomPrisoner($bias)"""

    override fun restart() {}

    override fun choose() = if (random.nextDouble()<bias)
        Prisoner.Choice.COOPERATE
    else
        Prisoner.Choice.TRICK

    override fun reward(r :Prisoner.Reward) {}

    override fun finish(winnings :Int, numRounds :Int) {}
}

class SlowlyAdjustingPrisoner :RandomPrisoner() {
    var n = 0

    override fun choose() = if (bias<0.2)
        Prisoner.Choice.QUIT
    else if (random.nextDouble() <= bias)
        Prisoner.Choice.COOPERATE
    else
        Prisoner.Choice.TRICK
    override fun toString() = """Adjusting RandomPrisoner"""

    override fun restart() {
        bias = 0.5
        n = 1
    }

    override fun reward(r :Prisoner.Reward) {
        n++
        bias = bias*(n-1)/n + when (r) {
            Prisoner.KnownReward.LOSE -> 0.0
            else -> 1.1/n
        }
    }
}
